# Build the app with Maven. This stage is not included in the final image.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

# Run only the packaged app, using a small Java image that runs as non-root.
FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY --from=build /app/target/paycore-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["-jar", "app.jar"]
