DEPLOY ON TOMCAT
----------------

The suggested way to deploy the application on an tomcat server is to define a new context which

 * configures a datasource
 * loads the application
 * set some parameters for the application

The delivered zip contains a example application context, which has been tested on windows 7 with tomcat 7 & 8.0

For further information consult the tomcat documentation: [https://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Defining_a_context](https://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Defining_a_context)

### Step-by-Step

Copy the domibusConnectorTomcatStarter.war file to a location where the application server can read it. But **NOT** in the
webapps folder (or any other folder where tomcat is auto deploying from). Because tomcat will automatically deploy it and you cannot
define a custom context for this application anymore. Remember this path!

Open your tomcat home directory. And put the domibusConnectorWebAppModule.xml into [engine]/[hostname]/ (usually this path will be \<your tomcat root path\>/Catalina/localhost/). 
Edit the xml and change the data-source according to your needs. Don't forget to
put the according jdbc driver jar into the tomcat libs folder.

Restart your tomcat. During startup tomcat dedects the new context configuration and loads it. Tomcat initializes the datasource connection
and also deploys the domibusConnectorTomcatStarter.war from the specified path (docBase). Als the specified parameters are put into the servlet
context and are available to the starting application.


### Description of the example context

    <Context docBase="<path to domibusConnectorWebAppModule.war>">  
        <Parameter  name="spring.datasource.jndi-name" 
                    value="jdbc/domibusWebConnectorDS" override="false" />
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

docBase ist the path to the war to deploy