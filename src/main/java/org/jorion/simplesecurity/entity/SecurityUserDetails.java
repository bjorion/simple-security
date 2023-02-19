package org.jorion.simplesecurity.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implement the class {@link UserDetails} and wraps the {@link Person} entity.
 */
public class SecurityUserDetails implements UserDetails {

    private final Person person;

    public SecurityUserDetails(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<? extends GrantedAuthority> authorities =
                person.getAuthorities()
                        .stream()
                        .map(a -> new SimpleGrantedAuthority(a.getName()))
                        .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
