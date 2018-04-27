DEPLOY ON TOMCAT
----------------

The suggested way to deploy the application on an tomcat server is to define a new context which

 * configures a datasource
 * loads the application
 * set some parameters for the application

The delivered zip contains a example application context, which has been tested on windows 7 with tomcat 7 & 8.0

For further information consult the tomcat documentation: [https://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Defining_a_context](https://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Defining_a_context)

### Tested Tomcat Version

The deployment has been tested on following tomcat versions:

 * Apache Tomcat/8.5.23 on Windows 7
 

### Step-by-Step

Copy the domibusConnectorTomcatStarter.war file to a location where the application server can read it. But **NOT** in the
webapps folder (or any other folder where tomcat is auto deploying from). Because tomcat will automatically deploy it and you cannot
define a custom context for this application anymore. Remember this path!

Open your tomcat home directory. And put the domibusConnectorWebAppModule.xml into <your tomcat root path\>/Catalina/localhost/. 
Edit the xml and change the data-source according to your needs. Don't forget to
put the according jdbc driver jar into the tomcat libs folder.

Restart your tomcat. During startup tomcat dedects the new context configuration and loads it. Tomcat initializes the datasource connection
and also deploys the domibusConnectorTomcatStarter.war from the specified path (docBase). Als the specified parameters are put into the servlet
context and are available to the starting application.


### Description of the example context

    <Context docBase="<path to domibusConnectorWebAppModule.war>">  
        <Parameter  name="spring.datasource.jndi-name" 
                    value="jdbc/domibusWebConnectorDS" override="false" />
        <Parameter  name="spring.config.location" 
                      value="<path to the folder containing the application.properties" 
                      override="false" />  
        <Resource name="jdbc/domibusWebConnectorDS" auth="Container"
                type="javax.sql.DataSource" 
                driverClassName="<jdbcDriverClass>"
                url="<databaseUrl>"
                username="<username>" 
                password="<password>" 
                maxActive="20" 
                maxIdle="10"
                maxWait="-1"/>   
    </Context>

The attribute 'docBase' contains the path of the war file, which should be deployed in this context.

### Example Step-By-Step Installation on a Centos 7

#### System preparation

Install java, tomcat and mysql:

> Note: mariadb is the opensource fork of mysql and in the standard centos repository available

    sudo yum install -y java-1.8.0-openjdk-headless mariadb tomcat mysql-connector-java

   * java-1.8.0-openjdk-headless java runtime environment
   * mariadb (the database)
   * tomcat tomcat web application container
   * mysql-connector-java jdbc driver for accessing the database

Make the database jdbc driver for tomcat available by linking it into the tomcat libs folder.
By linking you get the benifit, that the driver is updated by the centos distribution.

> Note: if you are using a different database like oracle, you have to copy the database jdbc driver jar
by yourself into the tomcat lib folder.

    cp /usr/share/java/mysql-connector-java.jar /usr/share/tomcat/lib/


Start the database service and enable it as system service. 

    sudo systemctl start mariadb
    sudo systemctl enable mariadb

Create a database and a database user:  

If the database has started successfully log into the datbase with the following command 
    
    sudo mysql
Execute the following commands on the database:
    
>Replace password with your own password  
    
    create database connector; --create a database named connector
    create user connector identified by 'password';
    grant all privileges on connector.* to connector;   
    
Execute the databaseInitializer:

>You can find the domibusConnectorDatabaseInitializer.jar in the folder database-scripts/databaseInitializer

    java -jar domibusConnectorDatabaseInitializer.jar \
        --changeLogFile=db/changelog/install/initial-4.0.xml \
        --driver=com.mysql.jdbc.Driver \
        --url=jdbc:mysql://localhost/connector \
        --username=connector \
        --password=password \        
        upgrade
          
After that your database is ready to use. Continue configuring and deploying the connector war.          
          

Create a folder for the domibusConnector:

    mkdir /opt/domibusConnector

After that copy the domibusConnector.war to /opt/domibusConnector/
    
    cp ${domibusConnectorWar} /opt/domibusConnector
    
Create a domibusConnectorWebAppModule.xml at /etc/tomcat/Catalina/localhost

    cp ${exampleContext}  /etc/tomcat/Catalina/localhost
    
   
Replace the databaseUrl, driverClass, eg. accordingly to your database settings:


>        <Context docBase="/opt/domibus/connectorWebApp.war">  
>             <Parameter  name="spring.datasource.jndi-name" 
>                         value="jdbc/domibusWebConnectorDS" override="false" />
>             <Parameter  name="spring.config.location" 
>                           value="/etc/opt/domibus/config" 
>                           override="false" />  
>             <Resource name="jdbc/domibusWebConnectorDS" auth="Container"
>                     type="javax.sql.DataSource" 
>                     driverClassName="com.mysql.jdbc.Driver"
>                     url="jdbc:mysql://localhost/connector"
>                     username="connector" 
>                     password="password" 
>                     maxActive="20" 
>                     maxIdle="10"
>                     maxWait="-1"/>   
>         </Context>

Copy the application example properties to /opt/domibus/config

    cp application.properties /opt/domibus/config 

Configure the application properties according to your needs. Especially the security stores [Configuring Trust- and Keystores](certificates.html).
