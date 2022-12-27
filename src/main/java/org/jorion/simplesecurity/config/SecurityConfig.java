package org.jorion.simplesecurity.config;

import org.jorion.simplesecurity.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	enum LoginType {

		BASIC,

		FORM, 
		
		OAUTH2
	};

	@Autowired
	private RsaKeyProperties rsaKeys;

	@Autowired
	private JpaUserDetailsService jpaUserDetailsService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		// @formatter:off
        http
            // necessary to display the H2 console
            .headers(headers -> headers.frameOptions(config -> config.sameOrigin()))
            .csrf(csrf -> csrf.disable());
        
		setHttpLoginMethod(http, LoginType.BASIC);
		setHttpLoginMethod(http, LoginType.OAUTH2);

		http.authorizeHttpRequests(request -> request
			.requestMatchers("/error").permitAll()
            .requestMatchers("/done").permitAll()
            .requestMatchers("/access-denied").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/favicon.ico").permitAll()	
            .anyRequest().authenticated()
	    );
		
		http.userDetailsService(jpaUserDetailsService);
		
		http.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/done")
			.invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");
		// @formatter:on

		return http.build();
	}

	private void setHttpLoginMethod(HttpSecurity http, LoginType loginType) throws Exception {

		switch(loginType) {
		case BASIC:
			// with basic, there is no "successUrl"
			http.httpBasic(Customizer.withDefaults());
			http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			break;
		case FORM:
			http.formLogin(form -> form.defaultSuccessUrl("/main", true));
			// we need a session with the form login
			break;
		case OAUTH2:
			// jwt: requires a JwtDecoder
			http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
			break;
		default:
			break;
		}
	}

	@Bean
	PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	// Used by OAuth2ResourceServer
	@Bean
	JwtDecoder jwtDecoder() {

		// symetric key: .withSecretKey()
		// NimbusJwtDecoder.withSecretKey(null);
		return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
	}

	// Used by TokenService
	@Bean
	JwtEncoder jwtEncoder() {

		JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
}
