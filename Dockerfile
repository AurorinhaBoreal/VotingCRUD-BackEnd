FROM gradle:8.4.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN apt-get update && \
    apt-get install dos2unix && \
    apt-get clean
RUN dos2unix gradlew
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-slim AS release
EXPOSE 8080
COPY --from=build /app/build/libs/voting-2.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]