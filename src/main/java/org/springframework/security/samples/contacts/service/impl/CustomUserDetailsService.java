package org.springframework.security.samples.contacts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.samples.contacts.dao.UserDao;
import org.springframework.security.samples.contacts.entity.Role;
import org.springframework.security.samples.contacts.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A custom {@link UserDetailsService} where user information
 * is retrieved from a JPA repository
 */
@Service("customUserDetailsService")
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * Returns a populated {@link UserDetails} object. 
	 * The username is first retrieved from the database and then mapped to 
	 * a {@link UserDetails} object.
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			org.springframework.security.samples.contacts.entity.User domainUser = userRepository.findByUsername(username);
			if(domainUser==null) throw new UsernameNotFoundException("user not found!");
			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			
			return new User(
					domainUser.getUsername(), 
					domainUser.getPassword(),
					domainUser.isEnabled(),
					accountNonExpired,
					credentialsNonExpired,
					accountNonLocked,
					getGrantedAuthorities(domainUser.getRoles()));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	
	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * @param roles {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}
}
