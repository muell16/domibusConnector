# Database Preparation

The deliverable contains database scripts to create a new database or 
 upgrade the database from 3.5 connector to 4.0 connector.

## Supported Databases

During development we tested the following databases:
 
 * Mysql
 * Oracle
 
We also only providing create scripts for these two databases. 
 
 
## New Database creation
  
Execute the scripts which are located TODO!

### Using the scripts

### Using liquibase 


## Database Upgrade 3.5 to 4.0

Upgrading the connector database is possibly. But first of all create a backup
of your current connector database. Then you can choose to upgrade your database
manually by executing the scripts or let liquibase do the work.

Both methods are assuming that there are no changes or additional constraints,
indexes added compared to the 3.5 database script.

### Using the script

 * Create a backup of your current database
 * Drop all Foreign key constraints
 * Execute the upgrade script

### Using liquibase

It is also possibly to let [liquibase](https://www.liquibase.org/) upgrade your database. For this purpose the
distribution contains a jar named domibusConnectorDatabaseInitializer.jar which includes all the necessary scripts
and liquibase. You can update your database by executing the jar:

    java -jar domibusConnectorDatabaseInitializer-4.0.0-beta2-SNAPSHOT-jar-with-dependencies.jar  --changeLogFile=db/changelog/v004/upgrade-3to4.xml --driver=com.mysql.jdbc.Driver --url=jdbc:mysql://localhost/domibusconnector --username=domibus --password=domibus

You have to provide
 * --driver the jdbc driver name (is com.mysql.jdbc.Driver for mysql)
 * --url the jdbc url to access the database (consult the documentation of your jdbc driver)
 * --username the username to access the database (the database user needs the permission to make schema modifications)
 * --password the password of the database user
 * --classpath the path to an additional jar which contains the jdbc driver (the package already contains the mysql jdbc driver, 
 so this parameter is only needed to provide the oracle jdbc driver jar)
 


