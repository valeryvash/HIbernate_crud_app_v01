package repo;

import model.Post;
import model.PostStatus;
import model.Writer;

import java.util.List;

public interface PostRepository extends GenericRepository<Long, Post> {
    @Override
    Post add(Post entity);

    @Override
    Post get(Long aLong);

    @Override
    Post update(Post entity);

    @Override
    Post remove(Long aLong);

    @Override
    List<Post> getAll();

    boolean containsId(Long id);

    void deleteByStatus(PostStatus postStatus);

}
