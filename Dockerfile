FROM maven:3.9.0-eclipse-temurin:17-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:19
COPY --from=build /target/IG.jar IG.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","IG.jar"]