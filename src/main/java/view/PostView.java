package view;

import model.Post;
import model.PostStatus;
import model.Tag;
import repo.PostRepository;
import repo.TagRepository;
import repo.hibernate.HibernatePostRepositoryImpl;
import repo.hibernate.HibernateTagRepositoryImpl;
import service.PostService;
import service.TagService;
import util.EntityPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.EntityPrinter.print;

public class PostView {

    private static final String postViewMessage =
        """
             :.:.:.: Posts :.:.:.:
            Input the point for continue...
            1. Show all posts
            2. Get post by id
            3. Update post content by post id
            4. Update post tags by post id
            5. Update post status by post id
            6. Delete post by id
            7. Delete posts by status
            \t'q' for quit
            \t'p' for previous screen
            """;

    private static final Scanner sc = new Scanner(System.in);

    private static final PostRepository pr = new HibernatePostRepositoryImpl();
    private static final TagRepository tr = new HibernateTagRepositoryImpl();

    private final static PostService postService = new PostService(pr);
    private final static TagService tagService = new TagService(tr);

    public static void run() {
        System.out.println(postViewMessage);
        choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> showAllPosts(); // done
            case "2" -> getPostById();  // done
            case "3" -> updatePostContentById(); // done
            case "4" -> updatePostTagsById(); // done
            case "5" -> updatePostStatusById(); // done
            case "6" -> deletePostById(); // done
            case "7" -> deletePostsByStatus(); // done
            case "8" -> getPostsByStatus();

            case "q" -> System.exit(0);
            case "p" -> StartView.run();
            default -> System.out.println("Wrong point. Try another");
        }
        question();
    }

    private static void question() {
        System.out.print("Show 'PostView' again? 'y' for yes\t");
        if ("y".equalsIgnoreCase(sc.nextLine())) {
            PostView.run();
        } else {
            System.exit(0);
        }
    }

    private static List<Tag> getTagsList() {
        List<Tag> tagsToBeReturned = new ArrayList<>();

        System.out.println("Input tag name. 's' for skip, 'q' for quit");
        do {
            String s = sc.nextLine();

            if (s.equalsIgnoreCase("q")) System.exit(0);
            if (s.equalsIgnoreCase("s")) break;

            Tag t;
            if (tagService.nameContains(s)) {
                t = tagService.getByName(s);
            } else {
                t = new Tag();
                t.setName(s);
                tagService.add(t);
            }
            tagsToBeReturned.add(t);
        } while (true);
        return tagsToBeReturned;
    }
    private static void showAllPosts() {
        postService.getAll().forEach(EntityPrinter::print);
    }

    private static void getPostsByStatus() {
        PostStatus postStatus = getPostStatus();
        postService.getAll()
                .stream()
                .filter(p -> p.getPostStatus() == postStatus)
                .forEach(EntityPrinter::print);
    }

    private static  PostStatus getPostStatus() {
        System.out.println("""
                Choose post status which do you prefer
                1. ACTIVE
                2. DELETED
                 'q' for quit""");

        PostStatus postStatus = null;
        do {
            String s = sc.nextLine().toLowerCase();
            if (s.equals("q")) System.exit(0);

            switch (s) {
                case "1" -> {
                    postStatus = PostStatus.ACTIVE;
                }
                case "2" -> {
                    postStatus = PostStatus.DELETED;
                }
                default -> System.out.println("Wrong point. Input other");
            }
        } while (postStatus != null);
        return postStatus;
    }

    private static  Post getPostById() {
        Post p;
        System.out.println("Input existed post id \n 'q' for quit");
        do {
            String s = sc.nextLine();
            if (s.equalsIgnoreCase("q")) System.exit(0);
            try {
                Long id = Long.valueOf(s);
                if (postService.containsId(id)) {
                    p = postService.get(id);
                    print(p);
                    return p;
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Input a number please");
            }
            System.out.println("Such post doesn't exist! Try again");
        } while (true);
    }

    private static void updatePostContentById() {
        Post p = getPostById();
        System.out.println("Input new post content \n 'q' for quit");

        String s = sc.nextLine();
        if (s.equalsIgnoreCase("q")) System.exit(0);

        p.setContent(s);
        postService.update(p);

        System.out.println("Post content updated");
        print(p);
    }


    private static void updatePostTagsById() {
        Post p = getPostById();
        List<Tag> tags = getTagsList();
        p.setTags(tags);
        postService.update(p);
        System.out.println("Post tags updated");
        print(p);
    }

    private static void updatePostStatusById() {
        Post p = getPostById();
        PostStatus ps = getPostStatus();
        p.setPostStatus(ps);
        postService.update(p);
        print(p);
    }

    private static void deletePostById() {
        Post p = getPostById();
        postService.remove(p.getId());
        System.out.println("Post deleted");
    }

    private static void deletePostsByStatus() {
        PostStatus ps = getPostStatus();
        postService.deleteByStatus(ps);
        System.out.println("Posts deleted");
    }


}