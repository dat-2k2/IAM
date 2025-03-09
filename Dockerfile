FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_PATH
ARG RESOURCE_PATH
ARG JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseContainerSupport"
COPY $JAR_PATH ./app.jar
COPY $RESOURCE_PATH .
ENTRYPOINT ["java", "-jar", "$JAVA_OPTS", "$WORKDIR/app.jar"]