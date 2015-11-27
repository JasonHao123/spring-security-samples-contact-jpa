package org.springframework.security.samples.contacts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.samples.contacts.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{

}
