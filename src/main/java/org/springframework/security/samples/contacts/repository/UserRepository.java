package org.springframework.security.samples.contacts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.samples.contacts.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);

	@Query("SELECT u.username from User u")
	List<String> findAllUsernames();

}
