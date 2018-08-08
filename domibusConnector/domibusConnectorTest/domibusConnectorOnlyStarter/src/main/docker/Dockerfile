FROM java:8
COPY maven /opt/


ENV DB_HOST db
ENV DB_PORT 3306


CMD /bin/bash /opt/connector/wait-for-it.sh ${DB_HOST}:${DB_PORT} -t 20 -- java -jar /maven/connector/connector.jar