# PLEASE ENSURE YOU FIRST BUILD THE PROJECT USING MAVEN BEFORE RUNNING THIS DOCKERFILE
FROM openjdk:22-jdk
ADD target/dns_project.jar dns_project.jar
ENTRYPOINT [ "java","-jar","dns_project.jar" ]
