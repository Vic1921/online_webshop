FROM amazoncorretto:20 as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Make sure the mvnw file has the correct line endings
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw package -DskipTests

# Use a second stage to create a smaller final image
FROM amazoncorretto:20-alpine

WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar
CMD ["java","-jar","app.jar"]
