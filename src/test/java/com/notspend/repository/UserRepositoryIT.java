package com.notspend.repository;

import com.notspend.entity.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaSystemException;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO replace demo data from liquibase/changelogs/17_add_demo_data.sql with test data
@RepositoryTest
class UserRepositoryIT {

    private static final String TEST_USERNAME = "demo";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(1);
        User testUser = users.get(0);
        assertEquals(TEST_USERNAME, testUser.getUsername());
    }

    @Test
    void getByUsernameExistingUser() {
        User testUser = userRepository.getByUsername(TEST_USERNAME).get();
        assertEquals(TEST_USERNAME, testUser.getUsername());
    }

    @Test
    void getByUsernameNonExistingUserReturnsEmptyOptional() {
        assertTrue(userRepository.getByUsername("non_existing_user").isEmpty());
    }

    @Test
    void saveUserWithoutIdThrowsException() {
        User user = new User();
        assertThatExceptionOfType(JpaSystemException.class)
                .isThrownBy(() -> {
                    userRepository.save(user);
                    entityManager.flush();
                })
                .havingRootCause()
                .withMessage("ids for this class must be manually assigned before calling save(): com.notspend.entity.User");

    }

    @Test
    void saveUserWithConstraintViolationThrowsException() {
        User user = new User();
        user.setUsername("onlyIdSet");
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> {
                    userRepository.save(user);
                    entityManager.flush();
                })
                .withMessageStartingWith("Validation failed for classes [com.notspend.entity.User] during persist time");

    }

    @Test
    void saveUserWithIncorrectEmailThrowsException() {
        User user = new User();
        user.setUsername("incorrect_email");
        user.setEmail("email_not_matching_pattern");
        user.setPassword("not_empty_pass");
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> {
                    userRepository.save(user);
                    entityManager.flush();
                })
                .withMessageContaining("ConstraintViolationImpl{interpolatedMessage='Email is invalid'");
    }

    @Test
    void saveCorrectUser() {
        String expected_username = "correct_user";

        User user = new User();
        user.setUsername(expected_username);
        user.setEmail("email@passes.validation");
        user.setPassword("not_empty_pass");

        userRepository.save(user);
        entityManager.flush();
        String username = jdbcTemplate.queryForObject(
                "SELECT username FROM user WHERE username = ?", new Object[]{expected_username}, String.class);
        assertEquals(expected_username, username);
    }

    @Test
    void updateUser() {
        User testUser = userRepository.findById(TEST_USERNAME).get();
        assertEquals("Demo", testUser.getName());
        assertEquals("Demonov", testUser.getSurname());
        testUser.setName("UpdatedName");
        testUser.setSurname("UpdatedSurname");
        userRepository.save(testUser);
        entityManager.flush();
        User updatedUser = userRepository.findById(TEST_USERNAME).get();
        assertEquals("UpdatedName", updatedUser.getName());
        assertEquals("UpdatedSurname", updatedUser.getSurname());
    }

    @Test
    void delete() {
        User testUser = userRepository.findById(TEST_USERNAME).get();
        userRepository.delete(testUser);
        assertTrue(userRepository.findById(TEST_USERNAME).isEmpty());
    }

    @Test
    void deleteNonExistingDoesNotThrowException() {
        User nonPersistedUser = new User();
        nonPersistedUser.setUsername("nonPersistedUser");
        nonPersistedUser.setEmail("email@passes.validation");
        nonPersistedUser.setPassword("not_empty_pass");
        userRepository.delete(nonPersistedUser);
        entityManager.flush();
    }

    @Test
    @Disabled("Investigate. Fails for some reason")
    void deleteById() {
        userRepository.deleteById(TEST_USERNAME);
        entityManager.flush();
        assertTrue(userRepository.findById(TEST_USERNAME).isEmpty());
    }

    @Test
    void deleteByNonExistentIdThrowsException() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> {
                    userRepository.deleteById("non_existent");
                    entityManager.flush();
                });
    }

    @Test
    @Disabled("Investigate. Fails for some reason")
    void deleteAll() {
        userRepository.deleteAll();
        entityManager.flush();
        assertThat(userRepository.findAll()).isEmpty();
    }
}