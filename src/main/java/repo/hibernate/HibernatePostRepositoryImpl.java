package repo.hibernate;

import model.Post;
import model.PostStatus;
import org.hibernate.Session;
import repo.PostRepository;

import java.util.List;

import static util.SessionProvider.provideSession;

public class HibernatePostRepositoryImpl implements PostRepository {
    @Override
    public void add(Post entity) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
        }
    }

    /*
     *   The better way according to:
     *   https://www.baeldung.com/hibernate-initialize-proxy-exception
     */
    @Override
    public Post get(Long aLong) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Post post =
            session.createQuery(
                            "select p " +
                                    "from Post p " +
                                    "left join fetch p.tags " +
                                    "where p.id = ?1 ",
                            Post.class)
                    .setParameter(1, aLong)
                    .getSingleResult();

            session.getTransaction().commit();

            return post;
        }
    }

    @Override
    public void update(Post entity) {
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

            Post post = session.get(Post.class, aLong);

            session.remove(post);

            session.getTransaction().commit();
        }
    }

    @Override
    public List<Post> getAll() {
        try (Session session = provideSession()) {
            session.beginTransaction();

            List<Post> posts = session.createQuery(
                    "select a " +
                                    "from Post a " +
                                    "left join fetch a.tags",
                            Post.class)
                    .getResultList();

            session.getTransaction().commit();

            return posts;
        }
    }

    @Override
    public boolean containsId(Long id) {
        try (Session session = provideSession()) {
            session.beginTransaction();

            Post post = session.get(Post.class, id);

            session.getTransaction().commit();

            return post != null;
        }
    }

    @Override
    public void deleteByStatus(PostStatus postStatus) {
        if (postStatus != null) {
            try (Session session = provideSession()) {
                session.beginTransaction();


                List<Post> resultList = session
                        .createQuery("select p " +
                                        "from Post p " +
                                        "where p.postStatus = ?1 ",
                                Post.class)
                        .setParameter(1, postStatus)
                        .getResultList();


                if (!resultList.isEmpty()) {
                    resultList.forEach(session::remove);
                }

                session.getTransaction().commit();
            }
        }
    }

}
