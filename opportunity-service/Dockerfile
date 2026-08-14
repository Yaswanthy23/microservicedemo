# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy root pom and module poms for full reactor resolution
COPY pom.xml .
COPY common-library/pom.xml common-library/
COPY auth-service/pom.xml auth-service/
COPY user-service/pom.xml user-service/
COPY lead-service/pom.xml lead-service/
COPY customer-service/pom.xml customer-service/
COPY contact-service/pom.xml contact-service/
COPY opportunity-service/pom.xml opportunity-service/
COPY quotation-service/pom.xml quotation-service/
COPY invoice-service/pom.xml invoice-service/
COPY task-service/pom.xml task-service/
COPY notification-service/pom.xml notification-service/
COPY file-service/pom.xml file-service/
COPY report-service/pom.xml report-service/
COPY audit-service/pom.xml audit-service/
COPY gateway-service/pom.xml gateway-service/

# Copy sources for common-library and opportunity-service
COPY common-library/src common-library/src
COPY opportunity-service/src opportunity-service/src

# Build only opportunity-service and its dependencies
RUN mvn clean package -pl opportunity-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user
RUN addgroup -S crm && adduser -S crm -G crm
USER crm

# Copy jar from builder stage
COPY --from=builder /app/opportunity-service/target/opportunity-service-*.jar app.jar

EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
