# Step 1: Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN ./mvn clean package -DskipTests

# Step 2: Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/hive5-0.0.1-SNAPSHOT.jar hive5.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hive5.jar"]