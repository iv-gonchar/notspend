package com.notspend.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CRUD repository with queries restricted to specific user
 *
 * @param <T> Entity type
 * @param <ID> ID property type
 */
@NoRepositoryBean
public interface CrudByUserRepository<T, ID> extends Repository<T, ID> {

    Optional<T> getByIdAndUserUsername(ID id, String username);

    List<T> getAllByUserUsername(String username);

    <S extends T> S save(S entity);

    void deleteByIdAndUserUsername(ID id, String username);
}
