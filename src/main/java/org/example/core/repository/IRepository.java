package org.example.core.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T , ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    Optional<T> findByUserName(String userName);
    List<T> findAll();
    void delete(ID id);

    void saveAll();
    void loadAll();

    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}
