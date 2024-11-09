FROM eclipse-temurin:21 as builder
WORKDIR application
COPY target/*.jar application