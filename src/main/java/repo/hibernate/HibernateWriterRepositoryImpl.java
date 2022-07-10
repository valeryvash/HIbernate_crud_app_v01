package repo.hibernate;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import model.Post;
import model.Tag;
import model.Writer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repo.WriterRepository;

import java.util.ArrayList;
import java.util.List;

import static util.SessionProvider.provideSession;

public class HibernateWriterRepositoryImpl implements WriterRepository {

    @Override
    public void add(Writer entity) {
        Transaction transaction = null;

        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /*
        The better way according to:
        https://www.baeldung.com/hibernate-initialize-proxy-exception
     */
    @Override
    public Writer get(Long aLong) {
        Writer writer = null;

        try (Session session = provideSession()) {

            writer = session.createQuery("""
                                            FROM Writer as writer
                                            LEFT JOIN FETCH writer.posts
                                            WHERE writer.id = ?1 """,
                                    Writer.class)
                            .setParameter(1, aLong)
                            .getSingleResult();

        }

        if (writer == null) {
            writer = new Writer();
        }

        return writer;
    }

    @Override
    public void update(Writer entity) {
        Transaction transaction = null;

        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Long aLong) {
        Transaction transaction = null;

        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            Writer writer = session.get(Writer.class, aLong);

            session.remove(writer);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Writer> getAll() {
        List<Writer> writerList = null;
        try (Session session = provideSession()) {

            writerList = session.createQuery(
                            """
                                    FROM Writer as writer 
                                    LEFT JOIN FETCH writer.posts""",
                            Writer.class)
                    .getResultList();

            return writerList;
        }
    }

    @Override
    public boolean containsId(Long id) {
        try (Session session = provideSession()) {

            Writer w = session.get(Writer.class, id);

            return w != null;
        }
    }

    @Override
    public boolean nameContains(String name) {
        try (Session session = provideSession()) {

            Writer w = session.createQuery(
                            """
                                    FROM Writer w 
                                    LEFT JOIN FETCH w.posts
                                    WHERE w.name = ?1 """,
                            Writer.class)
                    .setParameter(1, name)
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Writer getByName(String name) {
        Transaction transaction = null;
        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            List<Writer> list = session.createQuery(
                            """
                                    FROM Writer w 
                                    LEFT JOIN FETCH w.posts
                                    WHERE w.name = ?1 """,
                            Writer.class
                    )
                    .setParameter(1, name)
                    .getResultList();

            Writer w = null;

            if (!list.isEmpty()) {
                w = list.get(0);
            } else {
                w = new Writer();
            }

            transaction.commit();

            return w;
        }
    }
}
