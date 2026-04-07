# ==============================================
# API Gateway Dockerfile
# Replace this template with your actual setup
# ==============================================
#
# Examples for common frameworks:
#
# ── Node.js (Express/Fastify) ──
# FROM node:20-alpine
# WORKDIR /app
# COPY package*.json ./
# RUN npm ci --production
# COPY src/ ./src/
# EXPOSE 8000
# CMD ["node", "src/index.js"]
#
# ── Python (FastAPI/Flask) ──
# FROM python:3.12-slim
# WORKDIR /app
# COPY requirements.txt .
# RUN pip install --no-cache-dir -r requirements.txt
# COPY src/ ./src/
# EXPOSE 8000
# CMD ["uvicorn", "src.main:app", "--host", "0.0.0.0", "--port", "8000"]
#
# ── Go ──
# FROM golang:1.22-alpine AS builder
# WORKDIR /app
# COPY go.* ./
# RUN go mod download
# COPY . .
# RUN go build -o gateway ./src
# FROM alpine:3.19
# COPY --from=builder /app/gateway /gateway
# EXPOSE 8000
# CMD ["/gateway"]
#
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src/ ./src/
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]