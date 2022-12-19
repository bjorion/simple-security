package org.jorion.simplesecurity.service;

import java.util.function.Supplier;

import org.jorion.simplesecurity.entity.SecurityUserDetails;
import org.jorion.simplesecurity.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserDetailsService}. It will retrieve the user by username from the database. It will return
 * it as an implementation of the UserDetails interface.
 */
@Service
@Transactional(readOnly = true)
public class JpaUserDetailsService implements UserDetailsService
{
	private static final Logger LOG = LoggerFactory.getLogger(JpaUserDetailsService.class);

	@Autowired
	private IUserRepository userRepository;

	/**
	 * Override {@code Object loadUserByUsername(...)}
	 */
	@Override
	public SecurityUserDetails loadUserByUsername(String username)
	{
		LOG.debug("Retrieving info about [{}]", username);
		Supplier<UsernameNotFoundException> s = () -> new UsernameNotFoundException("Problem during authentication! (user not found)");
		
		SecurityUserDetails su = userRepository
		        .findUserByUsername(username)
		        .map(SecurityUserDetails::new)
		        .orElseThrow(s);

		return su;
	}

}
