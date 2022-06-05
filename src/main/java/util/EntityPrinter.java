package util;

import model.Post;
import model.Tag;
import model.Writer;

import java.util.List;

public class EntityPrinter {

    public static void print(Object object) {
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

    private static void print(Writer writer) {
        System.out.println(
                "\nWriter:" +
                        "\nid: " + writer.getId() +
                        "\nname: " + writer.getName()
        );

        List<Post> posts = writer.getPosts();

        if (posts.isEmpty()) {
            System.out.println("Writer has no posts");
        } else {
            posts.forEach(EntityPrinter::print);
        }
    }

    private static void print(Post post) {
        System.out.println(
                "Post:" +
                        "\nid: " + post.getId() +
                        "\ncontent: " + post.getContent() +
                        "\nstatus:" + post.getPostStatus().toString()
        );

        List<Tag> tags = post.getTags();

        if (tags.isEmpty()) {
            System.out.println("Post has no tags");
        } else {
            tags.forEach(EntityPrinter::print);
        }
    }

    private static void print(Tag tag) {
        System.out.println(
                "Tag: " + tag.getId() + " , " + tag.getName()
        );
    }
}
