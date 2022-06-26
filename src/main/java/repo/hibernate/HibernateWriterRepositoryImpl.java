package repo.hibernate;

import jakarta.persistence.NoResultException;
import model.Post;
import model.Writer;
import org.hibernate.Session;
import repo.WriterRepository;

import java.util.List;

import static util.SessionProvider.provideSession;

public class HibernateWriterRepositoryImpl implements WriterRepository {

    @Override
    public void add(Writer entity) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
        }
    }

    /*
        The better way according to:
        https://www.baeldung.com/hibernate-initialize-proxy-exception
     */
    @Override
    public Writer get(Long aLong) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Writer writer = session.createQuery(
                            "select w " +
                                    "from Writer  w " +
                                    "left join fetch Post p " +
                                    "left join Tag t " +
                                    "where w.id =?1 ",
                            Writer.class
                    )
                    .setParameter(1, aLong)
                    .getSingleResult();

            session.getTransaction().commit();

            return writer;
        }
    }

    @Override
    public void update(Writer entity) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            session.merge(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Long aLong) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Writer writer = session.get(Writer.class, aLong);

            session.remove(writer);

            session.getTransaction().commit();
        }
    }

    @Override
    public List<Writer> getAll() {
        try (Session session = provideSession()) {
            session.beginTransaction();

            List<Writer> writerList = session.createQuery(
                            "select w from Writer w " +
                                    "left join fetch Post p " +
                                    "left join fetch Tag t",
                            Writer.class
                    )
                    .getResultList();

            session.getTransaction().commit();

            return writerList;
        }
    }

    @Override
    public boolean containsId(Long id) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Writer w = session.get(Writer.class, id);

            session.getTransaction().commit();

            return w != null;
        }
    }

    @Override
    public boolean nameContains(String name) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Writer w = session
                    .createQuery("select w " +
                                    "from Writer w " +
                                    "where w.name = ?1 ",
                            Writer.class)
                    .setParameter(1, name)
                    .getSingleResult();

            session.getTransaction().commit();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Writer getByName(String name) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Writer w = session.createQuery(
                            "select w " +
                                    "from Writer w " +
                                    "left join fetch w.posts " +
                                    "where w.name = ?1",
                            Writer.class
                    )
                    .setParameter(1, name)
                    .getSingleResult();

            session.getTransaction().commit();

            return w;
        }
    }
}
