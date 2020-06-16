package org.jorion.spring.simplesecurity.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.jorion.spring.simplesecurity.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implement the class {@link UserDetails} and wraps the {@link User} entity.
 */
public class CustomUserDetails implements UserDetails
{
	private final User user;

	// --- Methods ---
	public CustomUserDetails(User user)
	{
		this.user = user;
	}

	public User getUser()
	{
		return user;
	}

	// @Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		// @formatter:off
		Collection<? extends GrantedAuthority> authorities = 
				user.getAuthorities()
					.stream()
					.map(a ->  new SimpleGrantedAuthority(a.getName()))
		        	.collect(Collectors.toList());
		// @formatter:on
		return authorities;
	}

	@Override
	public String getPassword()
	{
		return user.getPassword();
	}

	@Override
	public String getUsername()
	{
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

}
