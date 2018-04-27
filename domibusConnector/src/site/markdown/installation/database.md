# Database Preparation

The deliverable contains database scripts to create a new database or 
 upgrade the database from 3.5 connector to 4.0 connector.

## Supported Databases

The following databases are currently supported:
 
 * Mysql
 * Oracle
 
## New Database / Fresh Installation
  
If you are starting with a new installation you can just execute the provided scripts or use liquibase.

### Using the scripts

The documentation contains a folder database-scripts/initial. In this folder choose the database script four your database:
  
  * [MySql_4_0_initial.sql](../doc-resources/DbScripts/initial/MySql_4_0_initial.sql) for MySQL
  * [Oracle_4_0_initial.sql](../doc-resources/DbScripts/initial/Oracle_4_0_initial.sql) for Oracle

### Using liquibase 

It is also possibly to let [liquibase](https://www.liquibase.org/) create your database tables. Start reading the section "Upgrade with Liquibase" down below.


## Database Upgrade 3.5 to 4.0

Upgrading the connector database is possibly. But first of all **create a backup**
of your current connector database. Then you can choose to upgrade your database
manually by executing the scripts or let liquibase do the work.

Both methods are assuming that there are no changes or additional constraints,
indexes added compared to the 3.5 database script.

### Using the script

 * Create a backup of your current database
 * Drop all Foreign key constraints (the script will create them again!)
 * Execute the upgrade script which is is located in the folder database-scripts/migration
 use the script for your database:
    * [MySQL_Migrate_3.5_ConnectorDB_to_4.0.sql](../doc-resources/DbScripts/migration/MySQL_Migrate_3.5_ConnectorDB_to_4.0.sql) for mysql 
    * [Oracle_Migrate_3.5_ConnectorDB_to_4_0.sql](../doc-resources/DbScripts/migration/Oracle_Migrate_3.5_ConnectorDB_to_4_0.sql) for oracle database

### Using liquibase

If you want to use liquibase for database upgrade please continue with the next section "Upgrade with Liquibase".

## Upgrade with Liquibase

It is also possibly to let [liquibase](https://www.liquibase.org/) upgrade or create your database. Liquibase is a tool
which splits the database creation/upgrade into multiple changesets. In the future it will allow semi automatic database upgrades. 
You can also use liquibase to use unsupported databases like postgresql.

Liquibase is package into the jar named domibusConnectorDatabaseInitializer.jar which includes all 
the necessary database scripts:

 * Database Migrate 3.5 to 4.0 DB ChangeLog: "db/changelog/v004/upgrade-3to4.xml"
 * Database Create 4.0 DB ChangeLog:  "db/changelog/install/initial-4.0.xml"

You can execute the scripts in your database by executing the jar:

    java -jar domibusConnectorDatabaseInitializer.jar  --changeLogFile=${changeLogFile} \
    --driver=${sqlDriverName} \
    --url=${databaseUrl} \
    --username=${databaseUsername} \
    --password=${databasePassword} \
    --classpath=${jdbcDriverJar} \
    upgrade

   
 You have to provide
 
 * --driver the jdbc driver name 
    * com.mysql.jdbc.Driver for MySQL
    * oracle.jdbc.OracleDriver for Oracle (you have to provide the jar which contains the driver)
 * --url the jdbc url to access the database (consult the documentation of your jdbc driver)
    * jdbc:mysql://localhost/domibusconnector (for connecting to a local msql database named domibusconnector)
 * --username the username to access the database (the database user needs the permission to make schema modifications)
 * --password the password of the database user
 * --classpath the path to an additional jar which contains the jdbc driver (the package already contains the mysql jdbc driver, 
 so this parameter is only needed to provide the oracle jdbc driver jar)
 * --changeLogFile the change log liquibase should run against the database
 * --help will show the liquibase help 

 


