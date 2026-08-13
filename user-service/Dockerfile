# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy root pom and modules poms
COPY pom.xml .
COPY common-library/pom.xml common-library/
COPY user-service/pom.xml user-service/

# Copy sources for common-library and user-service
COPY common-library/src common-library/src
COPY user-service/src user-service/src

# Build only the user-service and its dependencies (common-library)
RUN mvn clean package -pl user-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user
RUN addgroup -S crm && adduser -S crm -G crm
USER crm

# Copy jar from builder stage
COPY --from=builder /app/user-service/target/user-service-*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
