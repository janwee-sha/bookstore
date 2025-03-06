FROM openjdk:20-slim

LABEL maintainer="Janwee <janwee_sha@outlook.com>"

ADD target/book-0.0.1-SNAPSHOT.jar application.jar

EXPOSE 7100

ENTRYPOINT ["java","-jar","./application.jar"]