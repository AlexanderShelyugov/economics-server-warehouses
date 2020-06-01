# The Economics' warehouses server

This is a repository of economics server, that maintains everything related to warehouses. Requires a connection to [PostgreSQL](https://www.postgresql.org) server.

It uses [Spring Boot](https://spring.io/projects/spring-boot), [Liquibase](https://www.liquibase.org), [ModelMapper](https://modelmapper.org). It's tested with [JUnit 5](https://junit.org/junit5), code is completely covered with tests.
## Table of contents
1. [Pre-build](#Pre-build)
2. [Build](#Build)
3. [Run](#Run)

## Pre-build
- If you want to launch the whole Economics infrastructure, follow steps in [the main repository](https://github.com/AlexanderShelyugov/economics-ci). This server will be built, configured and launched as well.
- In [`application.yml`](https://github.com/AlexanderShelyugov/economics-server-warehouses/blob/develop/src/main/resources/application.yml) consider editing everything related to the database connection.
**Note!** This server uses [Liquibase](https://www.liquibase.org), so there will be some edits on the database.

## Build
Follow these steps if you want to build this server only.
- build a Docker image using [`Dockerfile`](https://github.com/AlexanderShelyugov/economics-server-warehouses/blob/develop/Dockerfile) or [`build.sh`](https://github.com/AlexanderShelyugov/economics-server-warehouses/blob/develop/build.sh)
- or `./gradlew build` and then build a Docker image using [`Dockerfile.lite`](https://github.com/AlexanderShelyugov/economics-server-warehouses/blob/develop/Dockerfile.lite)

## Run
- `./gradlew bootRun`
- or [`run.sh`](https://github.com/AlexanderShelyugov/economics-server-warehouses/blob/develop/run.sh), or just run the Docker image manually