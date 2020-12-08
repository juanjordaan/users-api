# Users API

## Requirements
* Maven
* Java 11

## Compile

* mvn compile package

* docker-compose up -d

* docker-compose up -d app ( to rerun )


### Postman
* Ensure Postman Interceptor installed and enabled

### Usage
* The endpoint is at http://localhost:8080/api/v1/path/
* example : http://localhost:8080/api/v1/path/Users/juan/Downloads
* example : http://localhost:8080/api/v1/path/home/juan/Downloads
* example : http://localhost:8080/api/v1/path/c:/Juan/Downloads


### Run the Spring Boot app, Elastic Stack, Prometheus and Grafana

#### Elasitc
* Visit `http://localhost:5000/#!/` to use ElastiHQ interface to verify the content send to Elasticsearch (run `Query` for the `spring-boot-app-logs-YYYY.MM.dd` index):

#### Kibana
* Using the default username: `elastic` and password: `test`.
* Visit [Kibana](http://localhost:5601) to log in to Kibana
* Create `index` defined in the `logstash.conf` file and browse logs accumulated in the `logs/all.log` file (the file is automatically created on the application startup):
* Visit [Kibana] (http://localhost:5601/app/monitoring) to check out clusters monitoring:
`Standalone cluster` shows metrics for Filebeat, `docker-cluster` displays metrics for Elasticsearch, Kibana and Logstash:

#### Grafana
* Using the default username: `user` and password: `test`.
* You can also monitor the application state using [Grafana](http://localhost:3000/?orgId=1):
