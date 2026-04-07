# API Gateway

## Overview

Spring Cloud Gateway - The single entry point for all client requests. It routes incoming requests to the appropriate backend microservice using service discovery (Eureka).

## Features

- **Service Discovery**: Automatic service discovery via Eureka  
- **Request Routing**: Routes requests based on predicates (Path patterns)
- **Load Balancing**: Built-in client-side load balancing (`lb://` prefix)
- **Path Rewriting**: Transforms request paths before forwarding
- **Circuit Breaker**: Graceful failure handling with fallbacks
- **CORS**: Handled transparently

## Tech Stack

- **Framework**: Spring Cloud Gateway
- **Service Discovery**: Eureka Client
- **Port**: 8000

## Routing Table

| External Path           | Target Service | Rewrite Path        | Notes                    |
|-------------------------|-----------------|---------------------|--------------------------|
| `/user/**`              | user-service    | `/...` (remove prefix) | User authentication & info |

## Running

```bash
# From project root
docker compose up --build
```

## Routing Examples

```bash
# Call user login via gateway
curl -X POST http://localhost:8000/user/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"1234"}'

# Get user info via gateway  
curl -X GET http://localhost:8000/user/myInfo \
  -H "Authorization: Bearer <token>"

# Alternative: Call service directly (without gateway)
curl -X GET http://localhost:8080/user/myInfo
```

## Service Discovery

Services must register with Eureka. Check dashboard:
- **Eureka**: http://localhost:8761/

## Monitoring

- **Health**: http://localhost:8000/actuator/health
- **Routes**: http://localhost:8000/actuator/gateway/routes
- **Request logs**: Check console logs with DEBUG level

## Configuration

Edit `src/main/resources/application.yaml` to:
- Add/modify routes
- Change port
- Configure Eureka connection
- Set circuit breaker policies
