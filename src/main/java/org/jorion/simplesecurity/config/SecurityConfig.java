package org.jorion.simplesecurity.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.jorion.simplesecurity.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    enum LoginType {

        BASIC,

        FORM,

        /** OAUTH2 Resource Service: for authorization only */
        OAUTH2_RS,

        /** OAUTH2 Client: for authentication via OpenID */
        OAUTH2_CLIENT
    }

    @Autowired
    private RsaKeyProperties rsaKeys;

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @Value("jwt.symmetric-key")
    private String jwtSymmetricKey;

//	@Bean
//	public AuthenticationManager authManager(UserDetailsService userDetailsService) {
//		
//		var authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService);
//		return new ProviderManager(authProvider);
//	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // necessary to display the H2 console
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable);

        // Configure the authentication methods here
        setHttpLoginMethod(http, LoginType.BASIC);
        // setHttpLoginMethod(http, LoginType.FORM);
        // setHttpLoginMethod(http, LoginType.OAUTH2_CLIENT);
        setHttpLoginMethod(http, LoginType.OAUTH2_RS);

        // the session must be enabled when using the FORM authentication method
        // it's typically disabled when using OAUTH2
        disableSession(http);

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

        return http.build();
    }

    private void setHttpLoginMethod(HttpSecurity http, LoginType loginType) throws Exception {

        switch (loginType) {
            case BASIC -> {
                // with basic, there is no "successUrl"
                log.info("Setting up BASIC security");
                http.httpBasic(Customizer.withDefaults());
            }
            case FORM -> {
                log.info("Setting up FORM security");
                http.formLogin(form -> form.defaultSuccessUrl("/main", true));
            }
            // we need a session with the form login
            case OAUTH2_RS -> {
                // jwt: requires a JwtDecoder bean
                log.info("Setting up OAUTH2 RESOURCE SERVER security");
                http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
            }
            case OAUTH2_CLIENT -> {
                // use OAuth 2.0 and/or OpenID 1.0
                // Requires a bean of type ClientRegistrationRepository
                // In application.yml: spring.security.oauth2.client.registration: (...)
                log.info("Setting up OAUTH2 CLIENT security");
                http.oauth2Login().defaultSuccessUrl("/main", true);
            }
            default -> {
                log.error("Undefined login type");
            }
        }
    }

    private void disableSession(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    // Used by Spring to encode the password provider by the client
    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // Used by OAuth2ResourceServer
    @Bean
    @Primary
    JwtDecoder assymetricJwtDecoder() {

        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    // Used by TokenService
    @Bean
    @Primary
    JwtEncoder assymetricJwtEncoder() {

        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder symmetricJwtDecoder() {

        byte[] bytes = jwtSymmetricKey.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
        return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    JwtEncoder symmetricJwtEncoder() {

        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(jwtSymmetricKey.getBytes());
        return new NimbusJwtEncoder(jwkSource);
    }
}
