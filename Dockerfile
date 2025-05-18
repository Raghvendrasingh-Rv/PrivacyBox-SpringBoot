# ---- Build Stage ----
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---- Run Stage ----
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/privacybox-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]