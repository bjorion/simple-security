package org.jorion.simplesecurity.entity;

import jakarta.annotation.Nonnull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implement the class {@link UserDetails} and wraps the {@link Person} entity.
 */
public record SecurityUser(Person person) implements UserDetails {

    @Nonnull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return person.getAuthorities()
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.getName()))
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public String toString() {

        return "SecurityUser [username=" + getUsername() + "]";
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Nonnull
    @Override
    public String getUsername() {
        return person.getUsername();
    }

}
