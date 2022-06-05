package repo;

import java.util.List;

public interface GenericRepository<ID, T> {

    void add(T entity);

    T get(ID id);

    void update(T entity);

    void remove(ID id);

    List<T> getAll();
}
