FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml /app/parent-pom.xml
COPY gateway/pom.xml /app/gateway/pom.xml

RUN mkdir -p /app/backend /app/frontend
COPY backend/pom.xml /app/backend/pom.xml
COPY frontend/pom.xml /app/frontend/pom.xml

WORKDIR /app
RUN mv parent-pom.xml pom.xml && \
    mvn dependency:go-offline -pl gateway -am -B

COPY gateway/src /app/gateway/src

RUN mvn clean package -pl gateway -am -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Ash7nly"
LABEL description="Ash7nly Gateway - Spring Cloud Gateway"
LABEL version="1.0"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/gateway/target/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:80/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -jar app.jar"]

