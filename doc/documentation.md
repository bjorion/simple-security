# Documentation

[[_TOC_]]

## Security Filters

1. org.springframework.security.web.session.DisableEncodeUrlFilter@85b9f9c,
1. org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@26a004ed,
1. org.springframework.security.web.context.SecurityContextPersistenceFilter@332610ad,
1. org.springframework.security.web.header.HeaderWriterFilter@7e577eed,
1. org.springframework.security.web.authentication.logout.LogoutFilter@4fae4c3b,
1. xxx
1. xxx
1. org.springframework.security.web.savedrequest.RequestCacheAwareFilter@294ab038,
1. org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@13e5c53c,
1. org.springframework.security.web.authentication.AnonymousAuthenticationFilter@21457b14,
1. org.springframework.security.web.session.SessionManagementFilter@5dc8448b,
1. org.springframework.security.web.access.ExceptionTranslationFilter@3615f8d9,
1. org.springframework.security.web.access.intercept.FilterSecurityInterceptor@3533d790

.

## Generate a key with OpenSSL

* KEYPAIR: openssl genrsa -out **keypair.pem** 2048
* PUBLIC KEY: openssl rsa -in **keypair.pem** -pubout -out **public.pem**
* PRIVATE KEY: openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in **keypair.pem** -out **private.pem**
* SYMMETRIC KEY: NimbusJwtDecoder.withJwkSetUri

.

## Spring Security Classes

### 1. Overview

* __UserDetails__: interface, provides core user information
* __UserDetailsService__: interface which loads user-specific data. (loadByUsername)
* __UserDetailsManager__: interface, extends of UserDetailsService to create/update users
* __User__: implementation of __UserDetails__
* ____
* __AbstractAuthenticationToken__: Base Class for Authentication objects
* __AbstractAuthenticationProcessingFilter__: filter for HTTP based authentication requests
* __AbstractUserDetailsAuthenticationProvider__: provider designed for UsernamePasswordAuthenticationToken
* __AuthenticationManager__: attemps to authenticate the given Authentication object, returning a fully popuated Authentication object (included authorities) if successful


### 2. AbstractAuthenticationProcessingFilter (extensions)

* __UsernamePasswordAuthenticationFilter__ (token = UsernamePasswordAuthenticationToken) ---> AbstractAuthenticationProcessingFilter 
* __(custom).JwtClientCredentialFilter__ (token = JwtClientCredentialToken) ---> AbstractAuthenticationProcessingFilter
* __OAuth2LoginAuthenticationFilter__ (token = OAuth2LoginAuthenticationToken) ---> AbstractAuthenticationProcessingFilter


### 3. AuthenticationManager (implementations)

* __ProviderManager__ (iterates an Authentication request through a list of AuthenticationProvider) ---> AuthenticationManager
	

### 4. AuthenticationProvider (implementations)

* __DaoAuthenticationProvider__ (provider that retrieves user details from a UserDetailsService) ---> AbstractUserDetailsAuthenticationProvider ---> AuthenticationProvider
* __JwtAuthenticationProvider__ ---> AuthenticationProvider


### 5. UserDetailsService (implementations)

* InMemoryUserDetailsManager ---> UserDetailsManager ---> UserDetailsService 
* JdbcUserDetailsManager ---> UserDetailsManager ---> UserDetailsService
* JpaUserDetailsService ---> UserDetailsService 
* (custom).UserDetailsServiceImpl ---> UserDetailsService

.

## Login

### 1. Basic

- GET /hello
	- Authorization: Basic am9objoxMjM0NQ==
	- Token class: UsernamePasswordAuthenticationToken

**BasicAuthenticationFilter** → ProviderManager → DaoAuthenticationProvider → JpaUserDetailsService

.

### 2. LoginForm

- POST /hello
	- Body = form-data
	- username = ...
	- password = ...
	- Token class: UsernamePasswordAuthenticationToken

**UsernamePasswordAuthenticationFilter** → ProviderManager → DaoAuthenticationProvider → JpaUserDetailsService

.

### 3. Auth2ResourceServer

- POST /token (with basic authentication) => returns JWT
- JWTDecoder: 
	- **symmetric** (NimbusJwtDecoder.withSecretKey) or 
	- **assymetric** (NimbusJwtDecoder.withPublicKey) (encryption = private key; decryption = public key)
- With the JWT:
- GET localhost:8080/main
	- Authorization: Bearer Token: (jwt) => returns main page
	- Token class: BearerTokenAuthenticationToken

**BearerTokenAuthenticationFilter** → ProviderManager → JwtAuthenticationProvider

.

## References

* Spring Security JWT: How to Secure your Spring Boot Rest API with JWT
	- https://www.youtube.com/watch?v=KYNR5js2cXE&list=WL&index=2&t=541s
	
