package com.notspend.repository;

import com.notspend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    List<User> getAll();

    Optional<User> getByUsername(String username);
}
