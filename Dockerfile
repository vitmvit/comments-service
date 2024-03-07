FROM openjdk:17-alpine
COPY ./build/libs/comments-service-1.0.jar comments-service-1.0.jar
ENTRYPOINT ["java","-jar","comments-service-1.0.jar"]