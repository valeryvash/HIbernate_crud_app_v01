package repo.hibernate;

import jakarta.persistence.NoResultException;
import model.Post;
import model.PostStatus;
import model.Tag;
import org.hibernate.Session;
import repo.PostRepository;

import java.util.ArrayList;
import java.util.List;

import static util.SessionProvider.provideSession;

public class HibernatePostRepository implements PostRepository {
    @Override
    public void add(Post entity) {
        try (Session session = provideSession()) {
            session.beginTransaction();
            List<Tag> tagList = entity.getTags();
            if (tagList != null) {
                if (!tagList.isEmpty()) {
                    tagList.forEach(session::persist);
                }
            }
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    /*
        The better way according to:
        https://www.baeldung.com/hibernate-initialize-proxy-exception
     */
    @Override
    public Post get(Long aLong) {
        if (aLong == null) {
            return new Post();
        }
        try (Session session = provideSession()) {
            session.beginTransaction();
            Post returnedPost =
            session.createQuery(
                            "select u from Post u join  fetch  u.tags where u.id = ?1",
                            Post.class
                    )
                    .setParameter(1, aLong)
                    .getSingleResult();
            session.getTransaction().commit();
            return returnedPost;
        }
    }

    @Override
    public void update(Post entity) {
        try (Session session = provideSession()) {
            session.beginTransaction();
            List<Tag> tagList = entity.getTags();
            if (tagList != null) {
                if (!tagList.isEmpty()) {
                    tagList.forEach((tag) ->{
                        if (tag.getId() == 0L) {
                            session.persist(tag);
                        }
                    });
                }
            }
            session.merge(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Long aLong) {
        try (Session session = provideSession()) {
            session.beginTransaction();
            Post post= session.get(Post.class, aLong);
            session.remove(post);
            session.getTransaction().commit();
        }
    }

    @Override
    @Deprecated
    public List<Post> getAll() {
        try (Session session = provideSession()) {
            List<Post> posts = session
                    .createQuery("select a from Post a join fetch a.tags", Post.class)
                    .getResultList();
            return posts;
        }
    }

    @Override
    public boolean containsId(Long id) {
        try (Session session = provideSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void deleteByStatus(PostStatus postStatus) {

    }

    @Override
    public List<Post> getPostsForWriter(Long id) {
        return null;
    }

    @Override
    public Long getWriterId(Long postId) {
        return null;
    }
}
