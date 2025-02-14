

#### Introduction
   GIC Cinema booking system
   The system is built using Java, Spring Framework, and several utilities like 
   Scanner for user input. The design follows the Command design pattern, separating the concerns of user input handling, business logic, and data representation. It utilizes a service layer (CinemaService) to handle the core business logic, including checking booking information.

### Prerequisites

- Java JDK 17 (If running outside docker): [JDK](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
- Gradle

### Running Locally

You can run the service locally on your machine as a Java process.
        
        ./gradlew run


## Running tests
* Run all the unit core unit tests

        ./gradlew clean test


### Application logging (standard SLF4J logging)

```yaml
logging:
  level: INFO
  appenders:
   - type: grappler
     logFormat: "%-5p [%d{ISO8601,UTC}] [%t] %c: %m%n%rEx"
     environmentName: prod
```

### Sample screenshot