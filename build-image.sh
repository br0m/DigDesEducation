#!/bin/sh

mvn clean install -DskipTests=true
docker build -f ./Dockerfile --tag=openjdk_project:latest .