package com.notspend.service.persistance.jpa;

import com.notspend.entity.User;
import com.notspend.repository.UserRepository;
import com.notspend.service.persistance.UserService;
import com.notspend.util.SecurityUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Profile({"default", "jpa"})
public class UserJpaService implements UserService {

    private final UserRepository userRepository;

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    // todo maybe remove this method as it may be not used
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        return userRepository.getByUsername(username).orElse(null);
    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.deleteById(username);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public String currentUser() {
        return SecurityUserHandler.getCurrentUser();
    }
}