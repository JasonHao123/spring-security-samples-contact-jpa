package org.springframework.security.samples.contacts.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.samples.contacts.dao.UserDao;
import org.springframework.security.samples.contacts.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoJpa implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public User findByUsername(String username) {
        Query query = entityManager.createQuery("select u from User u where u.username=:username");
        query.setParameter("username", username);
        return (User) query.getSingleResult();
    }

}
