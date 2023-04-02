# simple-security

POC to test various authentication methods using Spring Security 6

### Documentation

* [Full Documentation](./doc/documentation.md)


### Authentication Methods

* Basic (http://localhost:8080/main)
* LoginForm (http://localhost:8080/)
* OAuth2 Resource Server (GET /main with Bearer Token = ...)
* OAuth2 Client Login (http://localhost:8080/) with a GitHub account


### Run the application

1. Select an authentication method in **Security Config**
2. Run the class **SimpleSecurityApp**
3. Go to http://localhost:8080/main
4. Use the credentials: *username = john; password = 12345*
5. http://localhost:8080/user => user info
6. http://localhost:8080/token => provide a JWT to be used in other requests

*See the **Login** section in the documentation for more information on how to log in*


### Accessing the H2 Console

* http://localhost:8080/h2-console
* JDBC URL: jdbc:h2:mem:testdb
* username: sa
* password: 
