FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:8-jre-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/taskagent-0.0.1-SNAPSHOT.jar /app/taskagent-backend.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker-compose","-jar","/app/taskagent-backend.jar"]
