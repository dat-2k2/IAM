FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_PATH
ARG RESOURCE_PATH
COPY $JAR_PATH ./app.jar
COPY $RESOURCE_PATH .
ENTRYPOINT java -jar "-Xms512m -Xmx1024m -XX:+UseContainerSupport" ./app.jar
HEALTHCHECK CMD curl -f http://localhost:8888/ || exit 1