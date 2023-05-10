#!/bin/sh

mvn clean install -DskipTests=true assembly:single
docker build -f ./Dockerfile --tag=openjdk_project:latest .