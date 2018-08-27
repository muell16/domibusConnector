FROM java:8
COPY maven /opt/

RUN mkdir /fsstorage

RUN chmod 770 /opt/*.sh


ENV DB_HOST db
ENV DB_PORT 3306
ENV server_port 8080

EXPOSE 8080

CMD /bin/bash /opt/wait-for-it.sh -h ${DB_HOST} -p ${DB_PORT} -t 20 && java -jar /opt/connector/connector.jar