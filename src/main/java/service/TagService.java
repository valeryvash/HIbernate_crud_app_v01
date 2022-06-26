package service;

import jakarta.persistence.NoResultException;
import model.Post;
import model.Tag;
import repo.TagRepository;

import java.util.List;

public class TagService {

    private TagRepository tagRepo = null;

    public TagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    public void add(Tag entity) {
        if (entity != null) {
            tagRepo.add(entity);
        }
    }

    public Tag get(Long aLong) {
        Tag tag = null;

        if (aLong != null) {
            tag = tagRepo.get(aLong);
        }

        if (tag == null) {
            tag = new Tag();
        }

        return tag;
    }

    public void update(Tag entity) {
        if (entity != null) {
            tagRepo.update(entity);
        }
    }

    public void remove(Long aLong) {
        if (aLong != null) {
            tagRepo.remove(aLong);
        }
    }

    public List<Tag> getAll() {
        return tagRepo.getAll();
    }

    public Tag getByName(String name) {
        Tag tag = null;

        if (name != null) {
            try {
                tag = tagRepo.getByName(name);
            } catch (NoResultException ignored) {}
        }

        if (tag == null) {
            tag = new Tag();
        }

        return tag;
    }

    public boolean containsId(Long id) {
        boolean result = false;

        if (id != null) {
            result = tagRepo.containsId(id);
        }

        return result;
    }

    public boolean nameContains(String name) {
        boolean result = false;

        if (name != null) {
            result = tagRepo.nameContains(name);
        }

        return result;
    }
}
