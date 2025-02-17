# Step 1: Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Kopiraj samo pom.xml i maven wrapper kako bi se izbegla nepotrebna kopiranja
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Preuzmi zavisnosti
RUN ./mvnw dependency:go-offline

# Kopiraj ostatak koda
COPY src ./src

# Izgradnja aplikacije (skipTests se koristi za izbegavanje pokretanja testova tokom build-a)
RUN ./mvnw clean package -DskipTests

# Step 2: Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/hive5-0.0.1-SNAPSHOT.jar hive5.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hive5.jar"]