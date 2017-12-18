DEPLOY ON TOMCAT
----------------

Copy the domibusConnectorTomcatStarter.war file to a location where the application server can read it. But **NOT** in the
webapps folder (or any other folder where tomcat is auto deploying from) Remember this path!

Open your tomcat home directory and move into [engine]/[hostname]/ (usually this path will be Catalina/localhost/) and 
put the domibusConnectorWebAppModule.xml there. Edit this xml and change the data-source according to your needs. Don't forget to
put the according jdbc driver jar into the tomcat libs folder.

Restart your tomcat. Now at the startup tomcat detexts the new context configuration and loads this configuration. Initializes the datasource
and also deploys the domibusConnectorTomcatStarter.war from the specified path (docBase). Als the specified parameters are put into the servlet
context and are available to the starting application.
