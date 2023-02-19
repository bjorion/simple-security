package org.jorion.simplesecurity.service;

import lombok.extern.slf4j.Slf4j;
import org.jorion.simplesecurity.entity.SecurityUserDetails;
import org.jorion.simplesecurity.repository.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IPersonRepository personRepository;

    /**
     * Override {@code Object loadUserByUsername(...)}
     */
    @Override
    public SecurityUserDetails loadUserByUsername(String username) {

        Supplier<UsernameNotFoundException> supplierNotFound =
                () -> new UsernameNotFoundException("Problem during authentication! (user not found)");
        SecurityUserDetails securityUserDetails = personRepository
                .findPersonByUsername(username)
                .map(SecurityUserDetails::new)
                .orElseThrow(supplierNotFound);
        log.debug("Retrieved info about [{}]: [{}]", username, securityUserDetails.toString());
        return securityUserDetails;
    }

}
