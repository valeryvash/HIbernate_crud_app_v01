package repo;

import model.Writer;

import java.util.List;

public interface WriterRepository extends GenericRepository<Long, Writer> {
    @Override
    void add(Writer entity);

    @Override
    Writer get(Long aLong);

    @Override
    void update(Writer entity);

    @Override
    void remove(Long aLong);

    @Override
    List<Writer> getAll();

    boolean containsId(Long id);

    boolean nameContains(String name);

    Writer getByName(String name);

}
