FROM gradle:jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src/
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar ./economics-server-warehouse.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","economics-server-warehouse.jar"]