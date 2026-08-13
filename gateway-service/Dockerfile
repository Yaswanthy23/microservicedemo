# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy root pom and module pom
COPY pom.xml .
COPY gateway-service/pom.xml gateway-service/

# Copy sources for gateway-service
COPY gateway-service/src gateway-service/src

# Build the gateway-service
RUN mvn clean package -pl gateway-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user
RUN addgroup -S crm && adduser -S crm -G crm
USER crm

# Copy jar from builder stage
COPY --from=builder /app/gateway-service/target/gateway-service-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
