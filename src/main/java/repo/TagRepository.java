package repo;

import model.Tag;

import java.util.List;

public interface TagRepository extends GenericRepository<Long, Tag> {

    @Override
    void add(Tag entity);

    @Override
    Tag get(Long aLong);

    @Override
    void update(Tag entity);

    @Override
    void remove(Long aLong);

    @Override
    List<Tag> getAll();

    Tag getByName(String name);

    boolean containsId(Long id);

    boolean nameContains(String name);
}
