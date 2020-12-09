# Users API
* Webflux and JWT
* Token expiration is set in src/resource/properties.yaml and is refreshed, thereby creating a timeout.

## Requirements
* Maven
* Java 11

## Compile
* mvn clean compile package

## Docker Run
* docker-compose up -d

## Java Run
* mvn spring-boot:run


### Postman
* Ensure Postman Interceptor installed and enabled

### Usage
* Postman tests exist via the link in the email
* Also, the TDD's should be very readable


### Endpoints
* The api base  : http://localhost:8080/api/v1
* register url  : http://localhost:8080/register
* login    url  : http://localhost:8080/login
* users    url  : http://localhost:8080/api/v1/users/
* actuator url  : http://localhost:8080/actuator/health
* swagger  url  : http://localhost:8080/v2/api-docs/
