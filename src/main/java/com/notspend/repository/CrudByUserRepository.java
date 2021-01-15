package com.notspend.repository;

import com.notspend.entity.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CRUD repository with queries restricted to specific user
 *
 * @param <T> Entity type
 * @param <ID> ID property type
 */
public interface CrudByUserRepository<T, ID> extends Repository<T, ID> {

    Optional<T> getByIdAndUser(ID id, User user);

    List<T> getAllByUser(User user);

    <S extends T> S save(S entity);

    void deleteByIdAndUser(ID id, User user);
}
