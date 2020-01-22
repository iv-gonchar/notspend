package com.notspend.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T,E> {

    Optional<T> get(E id);
    List<T> getAll();
    void save(T t);
    void update(T t);
    void delete(E id);
}