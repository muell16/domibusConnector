FROM openjdk:8-jre
MAINTAINER Stephan Spindler <spind42@gmail.com>

RUN mkdir /app
RUN mkdir /app/bin
RUN mkdir /app/config
RUN mkdir /app/fsstorage
RUN mkdir /app/log
RUN mkdir /app/lib

ADD maven/connector.jar /app/bin
WORKDIR /app

CMD [java -jar /app/bin/connector.jar]


ENV SPRING_CLOUD_BOOTSTRAP_LOCATION=/app/config/bootstrap.properties
ENV SPRING_CONFIG_LOCATION=/app/config
ENV SPRING_CONFIG_NAME=connector

EXPOSE 8080
