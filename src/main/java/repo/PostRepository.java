package repo;

import model.Post;
import model.PostStatus;

import java.util.List;

public interface PostRepository extends GenericRepository<Long, Post> {
    @Override
    void add(Post entity);

    @Override
    Post get(Long aLong);

    @Override
    void update(Post entity);

    @Override
    void remove(Long aLong);

    @Override
    List<Post> getAll();

    boolean containsId(Long id);

    void deleteByStatus(PostStatus postStatus);

}
