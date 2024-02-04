FROM openjdk:20-slim

LABEL maintainer="Janwee <janwee_sha@outlook.com>"

ADD target/config-server-0.0.1-SNAPSHOT.jar application.jar

EXPOSE 7000

ENTRYPOINT ["java","-jar","/application.jar"]
