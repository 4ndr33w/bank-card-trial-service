FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /usr/src/
COPY . .
RUN mvn install -Dmaven.test.skip

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /usr/src/target/*.jar /app/app.jar
EXPOSE 700
CMD ["java", "-jar", "/app/app.jar"]