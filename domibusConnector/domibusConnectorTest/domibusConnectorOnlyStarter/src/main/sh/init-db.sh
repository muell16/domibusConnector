#!/bin/bash
# script to init db with liquibase
#

#java -jar /opt/liquibase/liquibase-core-3.5.3.jar --changeLogFile=/opt/liquibase/mysql5innoDb-3.2.5.ddl --username=${DB_USER} --password=${DB_PASSWORD} --url="jdbc:mysql://${DB_HOST}:3306/${DB_NAME}" --driver=com.mysql.jdbc.Driver --classpath=/opt/domibus/lib --logLevel=DEBUG

/maven/connector/wait-for-it.sh ${DB_HOST}:3306 -t 20

if [ $? -eq 0 ]; then

CREATE_DATABASE="
CREATE DATABASE ${DB_NAME};
CREATE USER '${DB_USER}' IDENTIFIED BY '${DB_PASSWORD}';
GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO '${DB_USER}';
FLUSH PRIVILEGES;
"

mysql -u root --password=${MYSQL_ROOT_PASSWORD} -h ${DB_HOST} <<< $CREATE_DATABASE

#mysql --wait -u ${DB_USER} --password=${DB_PASSWORD} -h ${DB_HOST} ${DB_NAME} < /opt/init-db/mysql5innoDb-3.2.5.ddl

fi
