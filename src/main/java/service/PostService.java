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

    private TagRepository tagRepository;

    private PostRepository postRepository;

    public PostService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    /**
     *  This method can be used as a template for add and update methods
     * @param h1 add/update action for entity
     * @param post entity which shall be added/updated
     */
    private void helper(Consumer<Post> h1, Post post) {
        if (post != null) {

            List<Tag> tags = post.getTags();
            if (!tags.isEmpty()) {
                tags.forEach( tag -> {

                    if (tag.getId() == 0L) {
                        this.tagRepository.add(tag);
                    }

                });

                post.setTags(tags);
            }
            h1.accept(post);
        }
    }

    public void add(Post entity) {
        if (entity != null) {
            List<Tag> tags = entity.getTags();
            if (!tags.isEmpty()) {
                tags.forEach( tag -> {
                    if (tag.getId() == 0L) {
                        tagRepository.add(tag);
                    }
                });
                entity.setTags(tags);
            }
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
            List<Tag> tags = entity.getTags();
            if (!tags.isEmpty()) {
                tags.forEach( tag -> {
                    if (tag.getId() == 0L) {
                        tagRepository.add(tag);
                    }
                });
                entity.setTags(tags);
            }
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
