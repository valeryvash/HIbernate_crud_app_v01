package service;

import jakarta.persistence.NoResultException;
import model.Post;
import model.PostStatus;
import model.Tag;
import repo.PostRepository;
import repo.TagRepository;

import java.util.List;
import java.util.function.Consumer;

public class PostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public void add(Post entity) {
        if (entity != null) {
            postRepository.add(entity);
        }
    }

    public Post get(Long aLong) {
        Post post = null;

        if (aLong != null) {
            try {
                post = postRepository.get(aLong);
            } catch (NoResultException ignored) {}
        }

        if (post == null) {
            post = new Post();
        }

        return post;
    }

    public void update(Post entity) {
        if (entity != null) {
            postRepository.update(entity);
        }
    }

    public void remove(Long aLong) {
        if (aLong != null) {
            postRepository.remove(aLong);
        }
    }

    public List<Post> getAll() {
        return postRepository.getAll();
    }

    public boolean containsId(Long id) {
        boolean result = false;

        if (id != null) {
            result = postRepository.containsId(id);
        }

        return result;
    }

    public void deleteByStatus(PostStatus postStatus) {
        if (postStatus != null) {
            postRepository.deleteByStatus(postStatus);
        }
    }
}
