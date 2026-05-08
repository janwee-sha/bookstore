FROM gradle:9.0.0-jdk17 AS build

WORKDIR /workspace

COPY --chown=gradle:gradle . .

RUN gradle :eureka:bootJar --no-daemon


FROM eclipse-temurin:17-jre

LABEL maintainer="Janwee <janwee_sha@outlook.com>"

WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --home-dir /app --shell /usr/sbin/nologin app

COPY --from=build /workspace/eureka/build/libs/*.jar app.jar

USER app:app

EXPOSE 7010

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
