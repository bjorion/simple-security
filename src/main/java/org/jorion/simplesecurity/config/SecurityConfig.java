package org.jorion.simplesecurity.config;

import org.jorion.simplesecurity.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception
    {
        // @formatter:off
        http
            .formLogin(form -> form.defaultSuccessUrl("/main", true))
            // necessary to display the H2 console
            .headers(headers -> headers.frameOptions(config -> config.sameOrigin()))
            // we dont use cookies
            .csrf(csrf -> csrf.disable())
            // .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .logout(logout -> logout.logoutSuccessUrl("/"));
        
		http.authorizeHttpRequests(request -> request
//            .anyRequest().permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/css/**").permitAll()
            .requestMatchers("/img/**").permitAll()
            .requestMatchers("/favicon.ico").permitAll()	
            .anyRequest().authenticated()
	    );
		
		http.userDetailsService(jpaUserDetailsService);
		
		// @formatter:on

        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}