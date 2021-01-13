package com.notspend.repository;

import com.notspend.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    User getByUsername(String username);
}
