FROM gradle:8.4.1-jdk17 AS build
WORKDIR /workspace/app

# copy everything
COPY . /workspace/app

# build the application (no tests)
RUN gradle clean bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
