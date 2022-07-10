package repo.hibernate;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Post;
import model.PostStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.postgresql.core.NativeQuery;
import repo.PostRepository;

import java.util.ArrayList;
import java.util.List;

import static util.SessionProvider.provideSession;

public class HibernatePostRepositoryImpl implements PostRepository {
    @Override
    public void add(Post entity) {
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

    @Override
    public Post get(Long aLong) {
        Post post = null;

        try (Session session = provideSession()) {

            post = session.get(Post.class, aLong);

        } catch (HibernateException e) {
            e.printStackTrace();
        }

        if (post == null) {
            post = new Post();
        }

        return post;
    }

    @Override
    public void update(Post entity) {
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

            Post post = session.get(Post.class, aLong);

            session.remove(post);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = null;

        try (Session session = provideSession()) {

            posts = session.createQuery("""
                                    FROM Post p
                                    LEFT JOIN FETCH p.tags """,
                            Post.class)
                    .getResultList();

        } catch (HibernateException e) {
            e.printStackTrace();
        }

        if (posts == null) {
            posts = new ArrayList<>();
        }

        return posts;
    }

    @Override
    public boolean containsId(Long id) {
        try (Session session = provideSession()) {

            Post post = session.get(Post.class, id);

            return post != null;
        }
    }

    @Override
    public void deleteByStatus(PostStatus postStatus) {
        Transaction transaction = null;

        try (Session session = provideSession()) {
            transaction = session.beginTransaction();

            List<Post> resultList = session.createQuery("""
                                    FROM Post p 
                                    WHERE p.postStatus = ?1 """,
                            Post.class)
                    .setParameter(1, postStatus)
                    .getResultList();

            if (!resultList.isEmpty()) {
                resultList.forEach(session::remove);
            }

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

}
