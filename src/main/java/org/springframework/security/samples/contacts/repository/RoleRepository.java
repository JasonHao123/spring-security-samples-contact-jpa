package org.springframework.security.samples.contacts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.samples.contacts.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
