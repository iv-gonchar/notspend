package com.notspend.dao.impl;

import com.notspend.TestConfig;
import com.notspend.TestDatabaseInitializer;
import com.notspend.dao.UserDao;
import com.notspend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
@ContextConfiguration(classes = TestConfig.class, initializers = TestDatabaseInitializer.class)
@TestPropertySource(locations = "classpath:test.properties")
class UserDaoImplIT {

    @Autowired
    UserDao userDao;

    @Test
    @Transactional
    void getAll() {
        List<User> users = userDao.getAll();
        assertThat(users.size()).isEqualTo(1);
        assertEquals("demo", users.get(0).getUsername());
    }
}