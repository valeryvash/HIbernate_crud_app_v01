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

    private WriterRepository writerRepo = null;

    public WriterService(WriterRepository writerRepo) {
        this.writerRepo = writerRepo;
    }

    public void add(Writer entity) {
        if (entity != null) {
            List<Post> posts = entity.getPosts();
            if (posts != null && !posts.isEmpty()) {
                posts.forEach(p -> {
                    if (p.getWriter() != entity) {
                        p.setWriter(entity);
                    }
                });
            }
            writerRepo.add(entity);
        }
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
        if (entity != null) {
            List<Post> posts = entity.getPosts();
            if (posts != null && !posts.isEmpty()) {
                posts.forEach(p -> {
                    if (p.getWriter() != entity) {
                        p.setWriter(entity);
                    }
                });
            }
            writerRepo.update(entity);
        }
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
