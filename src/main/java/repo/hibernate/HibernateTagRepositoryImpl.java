package repo.hibernate;

import jakarta.persistence.NoResultException;
import model.Tag;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repo.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static util.SessionProvider.provideSession;

public class HibernateTagRepositoryImpl implements TagRepository {


    @Override
    public Tag add(Tag entity) {
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
        return entity;
    }

    @Override
    public Tag get(Long aLong) {
        Tag tag = null;

        try (Session session = provideSession()) {
            tag = session.get(Tag.class, aLong);
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        if (tag == null) {
            tag = new Tag();
        }

        return tag;
    }

    @Override
    public Tag update(Tag entity) {
        Tag tag = null;
        Transaction transaction = null;

        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            tag = session.merge(entity);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return tag;
    }

    @Override
    public Tag remove(Long aLong) {
        Tag tag = null;
        Transaction transaction = null;
        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            tag = session.get(Tag.class,aLong);

            session.remove(tag);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return tag;
    }

    @Override
    @Deprecated
    public List<Tag> getAll() {
        List<Tag> toBeReturned = null;

        try (Session session = provideSession()) {

            toBeReturned = session.createQuery(
                            """
                                    FROM Tag t""",
                            Tag.class)
                    .getResultList();

        } catch (HibernateException e) {
            e.printStackTrace();
        }

        if (toBeReturned == null) {
            toBeReturned = new ArrayList<>();
        }

        return toBeReturned;
    }

    @Override
    public Tag getByName(String name) {
        Tag tag = null;
        Transaction transaction = null;

        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            tag = session.createQuery(
                            """
                                    FROM Tag a 
                                    WHERE a.name = ?1""",
                            Tag.class)
                    .setParameter(1, name)
                    .getResultList()
                    .get(0);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        if (tag == null) {
            tag = new Tag();
        }

        return tag;
    }

    @Override
    public boolean containsId(Long id) {
        Tag tag = null;

        try (Session session = provideSession()) {
            tag = session.get(Tag.class, id);
        }

        return tag != null && (tag.getId() == id);
    }

    @Override
    public boolean nameContains(String name) {
        List<Tag> list = null;
        Transaction transaction = null;

        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            list = session.createQuery(
                            """
                                    FROM Tag a
                                    WHERE a.name = ?1""",
                            Tag.class
                    )
                    .setParameter(1, name)
                    .getResultList();

            transaction.commit();
        } catch (NoResultException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return list != null && !list.isEmpty();
    }

}
