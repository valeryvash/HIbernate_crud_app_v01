import model.Post;
import model.Tag;
import model.Writer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class HelloWorld {
    public static void main(String[] args) {

        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure()
                        .build();

        SessionFactory sessionFactory =
                new MetadataSources(registry)
                        .addAnnotatedClass(Writer.class)
                        .addAnnotatedClass(Post.class)
                        .addAnnotatedClass(Tag.class)
                        .getMetadataBuilder()
                        .build()
                        .getSessionFactoryBuilder()
                        .build();


        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Writer writer = new Writer();
        writer.setName("Mikhalkovsky");
        writer.setPosts(new ArrayList<>());

        Post post = new Post();
        post.setContent("1st post content");
        post.setTags(new ArrayList<>());
        Tag tag = new Tag();
        tag.setName("1st tag for 1st post");
        post.getTags().add(tag);
        session.persist(tag);

        tag = new Tag();
        tag.setName("2nd tag for 1st post");
        post.getTags().add(tag);
        session.persist(tag);

        writer.getPosts().add(post);
        session.persist(post);

        post = new Post();
        post.setContent("2nd post content");
        post.setTags(new ArrayList<>());
        tag = new Tag();
        tag.setName("3rd tag for 2nd post");
        session.persist(tag);
        post.getTags().add(tag);

        tag = new Tag();
        tag.setName("4th tag for 2nd post");
        session.persist(tag);
        post.getTags().add(tag);

        session.persist(post);

        writer.getPosts().add(post);

        session.persist(writer);

        session.getTransaction().commit();
        session.close();

    }
}
