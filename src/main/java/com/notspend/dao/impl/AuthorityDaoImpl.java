package com.notspend.dao.impl;

import com.notspend.dao.AuthorityDao;
import com.notspend.entity.Authority;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("orm")
public class AuthorityDaoImpl implements AuthorityDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Authority get(String id) {
        return null;
    }

    @Override
    public List<Authority> getAll() {
        return null;
    }

    @Override
    public void save(Authority authority) {
        Session session = sessionFactory.getCurrentSession();
        session.save(authority);
    }

    @Override
    public void update(Authority authority) {

    }

    @Override
    public void delete(String id) {

    }
}
