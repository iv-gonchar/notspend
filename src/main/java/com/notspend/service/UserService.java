package com.notspend.service;

import com.notspend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void addUser(User user);
    List<User> getAllUsers();
    Optional<User> getUser(String username);
    void deleteUserByUsername(String username);
    void updateUser(User user);
}
