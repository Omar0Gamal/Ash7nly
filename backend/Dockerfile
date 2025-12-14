FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml /app/parent-pom.xml
COPY backend/pom.xml /app/backend/pom.xml

RUN mkdir -p /app/gateway /app/frontend
COPY gateway/pom.xml /app/gateway/pom.xml
COPY frontend/pom.xml /app/frontend/pom.xml

WORKDIR /app
RUN mv parent-pom.xml pom.xml && \
    mvn dependency:go-offline -pl backend -am -B


COPY backend/src /app/backend/src
RUN mvn clean package -pl backend -am -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Ash7nly"
LABEL description="Ash7nly Backend - Spring Boot Monolith"
LABEL version="1.0"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=builder /app/backend/target/*.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -jar app.jar"]

