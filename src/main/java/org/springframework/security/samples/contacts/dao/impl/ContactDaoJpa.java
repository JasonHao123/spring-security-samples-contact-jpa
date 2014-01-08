package org.springframework.security.samples.contacts.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.samples.contacts.dao.ContactDao;
import org.springframework.security.samples.contacts.entity.Contact;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ContactDaoJpa implements ContactDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    

    @Override
    @Transactional
    public Contact create(Contact contact) {
        // TODO Auto-generated method stub
         entityManager.persist(contact);
         return contact;
    }

    @Override
    @Transactional
    public void delete(Long contactId) {
        // TODO Auto-generated method stub
       Contact contact =  entityManager.find(Contact.class, contactId);
        entityManager.remove(contact);
    }

    @Override
    public List<Contact> findAll() {
        Query query = entityManager.createQuery("select u from Contact u");
                                                               
        return query.getResultList();
    }

    @Override
    public List<String> findAllPrincipals() {
        Query query = entityManager.createQuery("select u.username from User u", String.class);
        return query.getResultList();
    }

    @Override
    public List<String> findAllRoles() {
        Query query = entityManager.createQuery("select u.name from Role u", String.class);
        return query.getResultList();
    }

    @Override
    public Contact getById(Long id) {
        
        return entityManager.find(Contact.class, id);
    }

    @Override
    @Transactional
    public void update(Contact contact) {
        // TODO Auto-generated method stub
        entityManager.merge(contact);
    }

    @Override
    @Transactional
    public void delete(Contact contact) {
        // TODO Auto-generated method stub
        entityManager.remove(contact);
    }

}
