package org.jorion.spring.simplesecurity.config;

import org.jorion.spring.simplesecurity.services.AuthenticationProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * Project general configuration.
 */
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter
{
    // --- Variables ---
    @Autowired
    private AuthenticationProviderService authenticationProvider;

    // --- Methods ---
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder()
    {
        return new SCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception
    {
        http.formLogin().defaultSuccessUrl("/main", true);

        // necessary to display the H2 console
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();

        // @formatter:off
		http.authorizeRequests()
			.antMatchers("/error").permitAll()
			.antMatchers("/actuator/**").permitAll()
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/css/**").permitAll()
		    .antMatchers("/img/**").permitAll()
	        .antMatchers("/favicon.ico").permitAll()
	        // .antMatchers("/**").permitAll()
	        .anyRequest().authenticated();
		// @formatter:on

        http.logout().logoutSuccessUrl("/");
    }

}