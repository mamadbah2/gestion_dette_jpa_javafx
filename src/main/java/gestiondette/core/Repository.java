package gestiondette.core;

import java.util.List;

public interface Repository<T> {
    void insert(T entity);
    T selectById(int id);
    List<T> selectAll();
    void update(T entity);
    void delete(T entity);
}
