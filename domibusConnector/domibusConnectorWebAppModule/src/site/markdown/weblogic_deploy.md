DEPLOY ON WEBLOGIC
------------------

domibusConnectorWebLogicStarter.war

The application exepects a jndi DataSource configured with the name "jdbc/domibusWebConnectorDS". Please configure a datasource 
with this name and deploy the application to your weblogic server.

The packaged war contains a minimal default weblogic.xml descriptor and also a web.xml which passes some environment variables to the
application. The application is configured to start under /domibusWebConnector
