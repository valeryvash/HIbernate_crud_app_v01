package service;

import jakarta.persistence.NoResultException;
import model.Post;
import model.Tag;
import model.Writer;
import repo.PostRepository;
import repo.TagRepository;
import repo.WriterRepository;

import java.util.List;
import java.util.function.Consumer;

public class WriterService {

    private TagRepository tagRepo = null;
    private PostRepository postRepo = null;
    private WriterRepository writerRepo = null;

    public WriterService(TagRepository tagRepo, PostRepository postRepo, WriterRepository writerRepo) {
        this.tagRepo = tagRepo;
        this.postRepo = postRepo;
        this.writerRepo = writerRepo;
    }

    /**
     *  The method purpose is to check and persist new objects inside of
     *  writers and posts collections
     * @param action - final action (add or update) for entity
     * @param entity - element which collections need to be checked for new elements
     */
    private void helper(Consumer<Writer> action, Writer entity) {
        if (entity != null) {

            List<Post> posts = entity.getPosts();
            if (!posts.isEmpty()) {
                posts.forEach(p -> {

                    List<Tag> tags = p.getTags();
                    if (!tags.isEmpty()) {
                        tags.forEach(t -> {
                            if (t.getId() == 0L) {
                                this.tagRepo.add(t);
                            }
                        });
                        p.setTags(tags);
                    }

                    if (p.getId() == 0L) {
                        this.postRepo.add(p);
                    }
                });
            }
            entity.setPosts(posts);
            action.accept(entity);
        }
    }

    public void add(Writer entity) {
        helper(writerRepo::add, entity);
    }

    public Writer get(Long aLong) {
        Writer writer = null;

        if (aLong != null) {
            try {
                writer = writerRepo.get(aLong);
            } catch (NoResultException ignored) {}
        }

        if (writer == null) {
            writer = new Writer();
        }

        return writer;
    }

    public void update(Writer entity) {
        helper(writerRepo::update, entity);
    }

    public void remove(Long aLong) {
        if (aLong != null) {
            writerRepo.remove(aLong);
        }
    }

    public List<Writer> getAll() {
        return writerRepo.getAll();
    }

    public boolean containsId(Long id) {
        boolean result = false;

        if (id != null) {
            result = writerRepo.containsId(id);
        }

        return result;
    }

    public boolean nameContains(String name) {
        boolean result = false;

        if (name != null) {
            result = writerRepo.nameContains(name);
        }

        return result;
    }

    public Writer getByName(String name) {
        Writer writer = null;

        if (name != null) {
            writer = writerRepo.getByName(name);
        }

        if (writer == null) {
            writer = new Writer();
        }

        return writer;
    }
}
