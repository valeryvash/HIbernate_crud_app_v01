package util;

import model.Post;
import model.Tag;
import model.Writer;

import java.util.List;

public class EntityPrinter {

    public static void print(Object object) {
        if (object != null) {
            if (object instanceof Writer) {
                print((Writer) object);
            } else if (object instanceof Post) {
                print((Post) object);
            } else if (object instanceof Tag) {
                print((Tag) object);
            } else {
                System.out.println("It is unknown instance");
            }
        }
    }

    private static void print(Writer writer) {
        System.out.printf(
                "Writer: [ id: %0,10d | name: %-20s ]\n", writer.getId(), writer.getName()
        );


        List<Post> posts = writer.getPosts();

        if (posts == null || posts.isEmpty()) {
            System.out.println("Zero posts");
        } else {
            System.out.printf("%d posts: \n", posts.size());
            posts.forEach(EntityPrinter::print);
        }
    }

    private static void print(Post post) {
        System.out.printf(
                "Post:   [ id: %0,10d | content: %-30s | Status: %-5s ]\n",
                post.getId(),post.getContent(),post.getPostStatus().toString()
        );

        List<Tag> tags = post.getTags();

        if (tags == null || tags.isEmpty()) {
            System.out.println("Zero tags");
        } else {
            tags.forEach(EntityPrinter::print);
        }
    }

    private static void print(Tag tag) {
        System.out.printf(
                "Tag:[id: %0,4d | name: %s ]\n", tag.getId(), tag.getName()
        );

    }
}
