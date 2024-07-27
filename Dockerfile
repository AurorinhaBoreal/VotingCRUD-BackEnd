# FROM gradle:8.4.0-jdk17 AS build
# WORKDIR /app
# COPY . .
# RUN chmod +x ./gradlew
# RUN ./gradlew bootJar --no-daemon

# FROM openjdk:17-jdk-slim AS release
# EXPOSE 8080
# COPY --from=build /app/build/libs/voting-2.jar app.jar

# ENTRYPOINT [ "java", "-jar", "app.jar" ]

FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install gradle -y
RUN gradle clean install 

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/voting-2.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]