package org.springframework.security.samples.contacts.dao;

import org.springframework.security.samples.contacts.entity.User;



public interface UserDao {

    User findByUsername(String username);

}
