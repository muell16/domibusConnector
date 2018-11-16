FROM openjdk:8-jre
MAINTAINER Stephan Spindler <spind42@gmail.com>

RUN mkdir /app
RUN mkdir /config
RUN mkdir /fsstorage
RUN mkdir /log
RUN mkdir /lib

ADD connector.jar /app
WORKDIR /app

CMD [java -jar connector.jar]


ENV SPRING_CLOUD_BOOTSTRAP_LOCATION=/config/bootstrap.properties
ENV SPRING_CONFIG_LOCATION=/config
ENV SPRING_CONFIG_NAME=connector

EXPOSE 8080
