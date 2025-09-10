# simple-security

POC to test various authentication methods using Spring Security 6

## Documentation

* [Security](./doc/security.md)

## Authentication Methods

* Basic (http://localhost:8080/main)
* LoginForm (http://localhost:8080/login)
* OAuth2 Login (http://localhost:8080/login) with a GitHub account
* OAuth2 Resource Server (Already authenticated, GET /main with Bearer Token = ...)
* OAuth2 Client (one application calling directly another one without human intervention)

## Run the application

1. Select an authentication method in **Security Config**
2. Run the class **SimpleSecurityApp**
3. Go to http://localhost:8080/main
4. Use the credentials: *username = john; password = 12345*
5. http://localhost:8080/user => user info
6. http://localhost:8080/token => provide a JWT to be used in other requests

*See the **Login** section in the [documentation](./doc/security.md#1-login) for more information on how to log in*

## Accessing the H2 Console

* http://localhost:8080/h2-console
* JDBC URL: jdbc:h2:mem:testdb
* username: sa
* password: 

## Spring Modulith

* Go to SimpleSecurityAppTest
* Execute the unit tests
