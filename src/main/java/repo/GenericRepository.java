package repo;

import java.util.List;

public interface GenericRepository<ID, T> {

    T add(T entity);

    T get(ID id);

    T update(T entity);

    T remove(ID id);

    List<T> getAll();
}
