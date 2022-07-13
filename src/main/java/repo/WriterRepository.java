package repo;

import model.Writer;

import java.util.List;

public interface WriterRepository extends GenericRepository<Long, Writer> {
    @Override
    Writer add(Writer entity);

    @Override
    Writer get(Long aLong);

    @Override
    Writer update(Writer entity);

    @Override
    Writer remove(Long aLong);

    @Override
    List<Writer> getAll();

    boolean containsId(Long id);

    boolean nameContains(String name);

    Writer getByName(String name);

}
