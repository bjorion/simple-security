# simple-security

POC to test various authentication methods using Spring Security 6

[[_TOC_]]

### Authentication Methods

* Basic (http://localhost:8080/main)
* LoginForm (http://localhost:8080/)
* OAuth2 Resource Server (GET /main with Bearer Token = ...)
* OAuth2 Client Login (http://localhost:8080/) with a GitHub account


### Documentation

* [Full Documentation](./doc/documentation.md)


### Start

* localhost:8080/
* user = john; password = 12345


### Accessing the H2 Console

* http://localhost:8080/h2-console
* JDBC URL: jdbc:h2:mem:testdb
* username: sa
* password: 
