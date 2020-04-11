FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=build/libs/economics-server-warehouse-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","app.jar"]