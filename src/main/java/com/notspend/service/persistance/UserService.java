package com.notspend.service.persistance;

import com.notspend.entity.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    User currentUser();
    List<User> getAllUsers();
    User getUser(String username);
    void deleteUserByUsername(String username);
    void updateUser(User user);
}
