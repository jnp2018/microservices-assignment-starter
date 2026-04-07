# Eureka Server

## Overview

Eureka Server is the service registry for microservice discovery. All services register themselves here and can discover other services.

## Key Features

- **Service Registration**: All microservices auto-register on startup
- **Service Discovery**: Services can find each other dynamically
- **Health Monitoring**: Continuous heartbeat checks from registered services
- **Load Balancing**: Enables client-side load balancing

## Configuration

- **Port**: 8761
- **Dashboard**: http://eureka-server:8761/

## Running

```bash
# From project root
docker compose up eureka-server --build
```

## How Services Register

Services must have these dependencies:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

And configuration in `application.yaml`:
```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    hostname: user-service
```

## Viewing Services

Visit: `http://localhost:8761/` on your browser to see all registered services.
