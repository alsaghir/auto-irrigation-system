# Automatic irrigation system

The Watering Service is a RESTful API that provides endpoints for managing plots and their watering time slots. The service is built with Spring Boot and uses an in-memory H2 database to store the data.

### Prerequisites
- Java 17 or later
- Maven

## Getting Started

Clone the repository:

```sh
git clone https://github.com/alsaghir/auto-irrigation-system.git
```

Build the project:

```sh
cd auto-irrigation-system
mvn clean package
```

Run the service:

```sh
java -jar target/auto-irrigation-system-0.0.1-SNAPSHOT.jar
```

Use the service:

The service is now running on `http://localhost:8080`. You can use your favorite REST client (e.g. Postman, curl) to interact with the service.

Here are some examples of how to use the service:

Create a new plot:

```sh
curl --location 'http://localhost:8080/api/v1/land/plots' --header 'Content-Type: application/json' --data '{"name": "Test Plot", "cultivatedArea": 10.0, "cropType": "Test Crop"}'
```

Get all plots details

```sh
curl --location 'http://localhost:8080/api/v1/land/plots'
```