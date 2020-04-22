FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR opt/app/dependency
ARG APPJAR=/build/libs/*.jar
COPY ${APPJAR} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
