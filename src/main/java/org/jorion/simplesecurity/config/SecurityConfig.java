package org.jorion.simplesecurity.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.jorion.simplesecurity.config.filter.AuthenticationLoggingFilter;
import org.jorion.simplesecurity.security.CustomAuthenticationSuccessHandler;
import org.jorion.simplesecurity.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SecurityConfig {

    enum LoginType {

        BASIC,

        FORM,

        /**
         * OAUTH2 Login: for authentication via OpenID.
         */
        OAUTH2_LOGIN,

        /**
         * OAUTH2 Resource Server: for authorization only (not authentication)
         */
        OAUTH2_RS,

        /**
         * OAUTH2 Client: to implement the Client Credentials grant type
         */
        OAUTH2_CLIENT;
    }

    @Autowired
    private RsaKeyProperties rsaKeys;

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private HandlerMappingIntrospector introspector;

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

        log.debug("SecurityFilterChain configuration");
        http
                // necessary to display the H2 console
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // disable CSRF filter
                .csrf(AbstractHttpConfigurer::disable);

        // --- Configure the authentication methods here
        setHttpLoginMethod(http, LoginType.BASIC);
        setHttpLoginMethod(http, LoginType.FORM);
        // setHttpLoginMethod(http, LoginType.OAUTH2_CLIENT);
        // setHttpLoginMethod(http, LoginType.OAUTH2_RS);

        // the session must be enabled when using the FORM authentication method
        // it's typically disabled when using OAUTH2
        // disableSession(http);

        http.authorizeHttpRequests(request -> request
                // open
                .requestMatchers(new MvcRequestMatcher(introspector, "/public")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/error")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/done")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/access-denied")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/actuator")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/actuator/**")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/h2-console/**")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/favicon.ico")).permitAll()
                // restricted
                .requestMatchers(new MvcRequestMatcher(introspector, "/write")).hasAuthority("WRITE")
                .anyRequest().authenticated()
        );

        // --- Set a custom Authentication Provider
        // default implementation calls the User Details Service
        // http.authenticationProvider(authenticationProvider);

        // --- Set a custom User Details Service
        http.userDetailsService(jpaUserDetailsService);

        // filters
        http.addFilterAfter(new AuthenticationLoggingFilter(), AnonymousAuthenticationFilter.class);

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/done")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        return http.build();
    }

    private void setHttpLoginMethod(HttpSecurity http, LoginType loginType) throws Exception {

        switch (loginType) {
            case BASIC -> {
                log.info("Setting up BASIC security");
                // this method adds "BasicAuthenticationFilter" to the filter chain
                http.httpBasic(basicConfig -> {
                    basicConfig.realmName("BASIC-REALM");
                    // basicConfig.authenticationEntryPoint(new CustomEntryPoint());
                });
            }
            case FORM -> {
                // we need a session with the form login
                log.info("Setting up FORM security");
                // this method adds "UsernamePasswordAuthenticationFilter" to the filter chain
                http.formLogin(formConfig -> {
                    formConfig.defaultSuccessUrl("/main", true);
                    // formConfig.successHandler(customAuthenticationSuccessHandler);
                });
            }
            case OAUTH2_LOGIN -> {
                // use OAuth 2.0 and/or OpenID 1.0
                // Requires a bean of type ClientRegistrationRepository
                // In application.yml: spring.security.oauth2.client.registration: (...)
                log.info("Setting up OAUTH2 CLIENT security");
                // this method adds "OAuth2LoginAuthenticationFilter" to the filter chain
                // this filter intercepts requests and applies the needed logic for OAuth2 authentication
                http.oauth2Login(oauth2Config -> {
                    // oauth2Config.defaultSuccessUrl("/main", true);
                });
            }
            case OAUTH2_RS -> {
                // jwt: requires a JwtDecoder bean
                log.info("Setting up OAUTH2 RESOURCE SERVER security");
                // this method adds "BearerTokenAuthenticationFilter" to the filter chain
                // this method will validate the bound JWT token against the IDP server
                http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults())
                );
            }
            case OAUTH2_CLIENT -> {
                // configure the app as OAuth 2 client
                // grant type must be set as "Client Credentials" in Client Registration
                http.oauth2Client(Customizer.withDefaults());
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
