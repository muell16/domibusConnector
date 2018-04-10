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


## Database Upgrade 3.5 to 4.0

Upgrading the connector database is possibly. But first of all create a backup
of your current connector database. Then you can choose to upgrade your database
manually by executing the scripts or let liquibase do the work.

Both methods are assuming that there are no changes or additional constraints,
indexes added compared to the 3.5 database script.

### Using the scripts

 * Create a backup of your current database
 * Drop all Foreign key constraints
 * Execute the upgrade script

### Using liquibase

It is also possibly to let [liquibase](https://www.liquibase.org/) upgrade your database.






