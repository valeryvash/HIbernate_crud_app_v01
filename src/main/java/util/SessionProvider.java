package util;

import model.Post;
import model.Tag;
import model.Writer;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionProvider {

    private static StandardServiceRegistry registry = null;

    private static SessionFactory sessionFactory = null;

    static {
        registry =
                new StandardServiceRegistryBuilder()
                        .configure()
                        .build();
        sessionFactory =
                new MetadataSources(registry)
                        .addAnnotatedClass(Writer.class)
                        .addAnnotatedClass(Post.class)
                        .addAnnotatedClass(Tag.class)
                        .getMetadataBuilder()
                        .build()
                        .getSessionFactoryBuilder()
                        .build();
    }

    public static Session provideSession() {
        Session sessionToBeReturned = sessionFactory.openSession();
        if (sessionToBeReturned != null) {
            return sessionToBeReturned;
        } else {
            throw new SessionException("SessionProvider class doesn't provide session");
        }
    }

}
