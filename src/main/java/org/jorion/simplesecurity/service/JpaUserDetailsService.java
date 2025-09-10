package org.jorion.simplesecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jorion.simplesecurity.entity.SecurityUser;
import org.jorion.simplesecurity.repository.IPersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * Implementation of {@link UserDetailsService}. It will retrieve the user by username from the database.
 * It will return it as an implementation of the {@link UserDetails} interface.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final IPersonRepository personRepository;

    /**
     * Override {@code UserDetails loadUserByUsername(...)}
     */
    @Override
    public SecurityUser loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Supplier<UsernameNotFoundException> supplierNotFound =
                () -> new UsernameNotFoundException("Problem during authentication! (user not found)");
        var securityUser = personRepository
                .findPersonByUsername(username)
                .map(SecurityUser::new)
                .orElseThrow(supplierNotFound);
        log.info("Retrieved info about [{}]: [{}]", username, securityUser.toString());
        return securityUser;
    }

}
