FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=./build/libs/*.jar
# Add into docker container
COPY ${JAR_FILE} app.jar
# Access permission to wait-for-it.sh
COPY ./scripts/deploy/wait-for-it.sh scripts/wait-for-it.sh
RUN chmod +x scripts/wait-for-it.sh
# Execute app.jar when running docker
ENTRYPOINT ["scripts/wait-for-it.sh", "database:3306", "--", "java", "-jar", "app.jar",  "--spring.profiles.active=dev", "--spring.config.additional-location=resources/application-mail.yml, resources/application-jwt.yml"]
