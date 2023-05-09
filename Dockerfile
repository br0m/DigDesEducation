FROM openjdk:18.0.2.1-slim-buster
WORKDIR /opt/project
COPY ./app/target/project-exec.jar ./
CMD java -jar project-exec.jar