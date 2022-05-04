FROM java:8

MAINTAINER key "janwee_sha@outlook.com"

ADD target/eureka-server-0.0.1-SNAPSHOT.jar erksvr.jar

EXPOSE 7100

ENTRYPOINT ["java","-jar","/erksvr.jar"]
