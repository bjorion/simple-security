package org.jorion.simplesecurity.config;

import org.jorion.simplesecurity.service.JpaUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implements custom authentication logic.
 */
@Service
public class SimpleAuthenticationProvider implements AuthenticationProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleAuthenticationProvider.class);

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails user = userDetailsService.loadUserByUsername(username);

        switch (user.getUser().getAlgorithm()) {
        case BCRYPT:
            return checkPassword(user, password, new BCryptPasswordEncoder());
        default:
            break;
        }

        throw new BadCredentialsException("Bad credentials (unknown algorithm)");
    }

    private Authentication checkPassword(CustomUserDetails user, String rawPassword, PasswordEncoder encoder)
    {
        Authentication a;
        if (encoder.matches(rawPassword, user.getPassword())) {
            a = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        }
        else {
            throw new BadCredentialsException("Bad credentials (password does not match)");
        }
        LOG.debug("checkPassword successful");
        return a;
    }

}
