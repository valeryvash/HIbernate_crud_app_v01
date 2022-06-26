package view;

import model.Post;
import model.Tag;
import model.Writer;
import repo.PostRepository;
import repo.TagRepository;
import repo.WriterRepository;
import repo.hibernate.HibernatePostRepositoryImpl;
import repo.hibernate.HibernateTagRepositoryImpl;
import repo.hibernate.HibernateWriterRepositoryImpl;
import service.PostService;
import service.TagService;
import service.WriterService;
import util.EntityPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.EntityPrinter.print;

public class WriterView {

    private static final String writerViewMessage =
            """
                    :.:.:.: Writers :.:.:.:
                    Input the point for continue...
                    1. Create a new one writer
                    2. Get writer by id
                    3. Update writer name
                    4. Delete writer\s
                    5. Print writer posts
                    6. Print all writers info
                    7. Create post for writer
                    
                    \t'q' for quit
                    \t'p' for previous screen
                    """;

    private static final Scanner sc = new Scanner(System.in);

    private static final WriterRepository wr = new HibernateWriterRepositoryImpl();
    private static final PostRepository pr = new HibernatePostRepositoryImpl();
    private static final TagRepository tr = new HibernateTagRepositoryImpl();

    private final static WriterService writerService = new WriterService(tr, pr, wr);

    private final static PostService postService = new PostService(tr, pr);

    private final static TagService tagService = new TagService(tr);

    public static void run() {
        System.out.println(writerViewMessage);
        choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> writerCreate();//done
            case "2" -> getWriter();//done
            case "3" -> updateWriterName();//done
            case "4" -> writerDelete();//done
            case "5" -> getWriterPosts();//done
            case "6" -> getAllWriters();//done
            case "7" -> createPost();

            case "q" -> System.exit(0);
            case "p" -> StartView.run();
            default -> System.out.println("Wrong point. Try another");
        }
        question();
    }

    private static void createPost() {
        Writer w = getWriter();
        System.out.println("Input new post content \n 'q' for quit");
        Post p;

            String s = sc.nextLine();
            if (s.equalsIgnoreCase("q")) System.exit(0);
            p = new Post();
            p.setContent(s);
            postService.add(p);

            List<Tag> tags = getTagsList();
            if (!tags.isEmpty()) {
                p.setTags(tags);
                postService.update(p);
            }

            List<Post> posts = w.getPosts();
            posts.add(p);

            w.setPosts(posts);
    }

    private static List<Tag> getTagsList() {
        List<Tag> tagsToBeReturned = new ArrayList<>();
        System.out.println("Input tag name. 's' for skip, 'q' for quit");
        do {
            String s = sc.nextLine();

            if (s.equalsIgnoreCase("q")) System.exit(0);
            if (s.equalsIgnoreCase("s")) break;

            Tag t ;
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

    private static void question() {
        System.out.print("Show 'WriterView' again? 'y' for yes\t");
        if ("y".equalsIgnoreCase(sc.nextLine())) {
            WriterView.run();
        } else {
            System.exit(0);
        }
    }

    private static void writerCreate() {
        Writer w = new Writer();
        System.out.println("Input new writer name \n 'q' for quit");
        do {
            String s = sc.nextLine();
            if (s.equalsIgnoreCase("q")) System.exit(0);
            if (!writerService.nameContains(s)) {
                w.setName(s);
                writerService.add(w);
                System.out.println("New writer created");
                print(w);
                break;
            } else {
                System.out.println("Writer name already exist! Try another");
            }
        } while (true);
    }

    private static Writer getWriter() {
        Writer w;
        System.out.println("Input exist writer id or name \n 'q' for quit");
        do {
            String s = sc.nextLine();
            if (s.equalsIgnoreCase("q")) System.exit(0);
            if (!writerService.nameContains(s)) {
                try {
                    Long id = Long.valueOf(s);
                    if (writerService.containsId(id)){
                        w = writerService.get(id);
                        print(w);
                        return w;
                    }
                } catch (NumberFormatException ignored) {}
            } else {
                w = writerService.getByName(s);
                print(w);
                return w;
            }
            System.out.println("Such writer doesn't exist! Try again");
        } while (true);
    }

    private static void updateWriterName() {
        Writer w = getWriter();
        System.out.println("Input new writer name \n 'q' for quit");
        do {
            String s = sc.nextLine();
            if (s.equalsIgnoreCase("q")) System.exit(0);
            if (!writerService.nameContains(s)) {
                w.setName(s);
                writerService.update(w);
                System.out.println("Writer updated");
                print(w);
                break;
            } else {
                System.out.println("Such writer name already exist! Try another ");
            }
        } while (true);
    }

    private static void writerDelete() {
        Writer w = getWriter();
        do{
            System.out.println("""
                    Are you sure want to delete this writer?
                     All writer posts will be deleted
                    \s
                     'y' for yes\s
                     'q' for quit""");
            String s = sc.nextLine().toLowerCase();
            if (s.equalsIgnoreCase("q")) System.exit(0);
            if (s.equalsIgnoreCase("y")) {
                writerService.remove(w.getId());
                System.out.println("Writer deleted.");
                break;
            }
        } while(true);
    }

    private static void getWriterPosts() {
        Writer w = getWriter();
        List<Post> writerPosts = w.getPosts();
        if (!writerPosts.isEmpty()){
            writerPosts.forEach(EntityPrinter::print);
        } else {
            System.out.println("Writer has no posts");
        }
    }

    private static void getAllWriters() {
        writerService.getAll().forEach(EntityPrinter::print);
    }

}