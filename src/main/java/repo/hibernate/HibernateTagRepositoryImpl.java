package repo.hibernate;

import jakarta.persistence.NoResultException;
import model.Tag;
import org.hibernate.Session;
import repo.TagRepository;

import java.util.List;

import static util.SessionProvider.provideSession;

public class HibernateTagRepositoryImpl implements TagRepository {

    @Override
    public void add(Tag entity) {
        try (Session session = provideSession()) {
                session.beginTransaction();

                session.persist(entity);

                session.getTransaction().commit();
        }
    }

    @Override
    public Tag get(Long aLong) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Tag tag = session.get(Tag.class, aLong);

            session.getTransaction().commit();

            return tag;
        }
    }

    @Override
    public void update(Tag entity) {
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

            Tag tag = session.get(Tag.class, aLong);

            session.remove(tag);

            session.getTransaction().commit();
        }
    }

    @Override
    @Deprecated
    public List<Tag> getAll() {
        try (Session session = provideSession()) {
            session.beginTransaction();

            List<Tag> allTags = session
                    .createQuery(
                            "select t " +
                            "from Tag t",
                            Tag.class)
                    .getResultList();

            session.getTransaction().commit();
            return allTags;
        }
    }

    @Override
    public Tag getByName(String name) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Tag tag = session
                    .createQuery(
                            "select a " +
                                    "from Tag a " +
                                    "where a.name = ?1",
                            Tag.class)
                    .setParameter(1, name)
                    .getSingleResult();

            session.getTransaction().commit();

            return tag;
        }
    }

    @Override
    public boolean containsId(Long id) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Tag tag = session.get(Tag.class, id);

            session.getTransaction().commit();

            return tag != null;
        }
    }

    @Override
    public boolean nameContains(String name) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Tag tag = session.createQuery(
                            "select a " +
                                    "from Tag a " +
                                    "where a.name = ?1",
                            Tag.class
                    )
                    .setParameter(1, name)
                    .getSingleResult();

            session.getTransaction().commit();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

}
