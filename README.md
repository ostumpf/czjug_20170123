# Service Discovery via Netflix Eureka - examples presented at CZ JUG session on 2017-01-23

## Requirements
* JDK 1.8
* Maven

## Build and Run
cd to desired project directory and run:
```
mvn clean package exec:java
```

## eureka-server-spring
Runs the Eureka server on http://localhost:8080/. Leave the server running to test the applications below.

## eureka-client-java
Runs a sample application using native Netflix Eureka client. The application prints the hostnames of the registered
Eureka application instances and exits. While running, the application is registered in Eureka as *czjug-java*.

## eureka-client-spring
Runs a sample application using Spring Eureka client. The application creates a simple controller on 
http://localhost:8080/ - when / is queried, list of hostnames of registered Eureka instances is returned. 
While running, the application is registered in Eureka as *czjug-spring*.

## eureka-ribbon
Runs a sample application using Netflix Ribbon - the load balancing client attempts to send request to any Eureka 
instance, using just hostname as URL. While running, the application is registered in Eureka as *czjug-ribbon*.
