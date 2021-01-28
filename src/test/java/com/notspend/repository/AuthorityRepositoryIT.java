package com.notspend.repository;

import com.notspend.entity.Authority;
import com.notspend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RepositoryTest
class AuthorityRepositoryIT {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    private final User user = new User();

    @BeforeEach
    void setUp() {
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Test
    @Transactional
    void save() {
        Authority authority = new Authority();
        authority.setAuthority("ROLE_TEST");
        authority.setUsername(user);
        authorityRepository.save(authority);
        entityManager.flush();

        String authorityName = jdbcTemplate.queryForObject(
                "SELECT authority FROM authority WHERE authority = 'ROLE_TEST'", String.class);
        assertEquals("ROLE_TEST", authorityName);
    }

    @Test
    @Transactional
    void saveThrowsExceptionWhenUserNull() {
        Authority authority = new Authority();
        authority.setAuthority("ROLE_TEST");
        assertThatExceptionOfType(PersistenceException.class)
                .isThrownBy(() -> {
                    authorityRepository.save(authority);
                    entityManager.flush();
                })
                .havingRootCause()
                .withMessage("Column 'username' cannot be null");
    }
}