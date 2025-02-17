# Step 1: Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Kopiraj pom.xml i preuzmi zavisnosti, kao i .mvn direktorijum
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Kopiraj ostatak koda
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Step 2: Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/hive5-0.0.1-SNAPSHOT.jar hive5.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hive5.jar"]