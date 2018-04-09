DEPLOY ON WEBLOGIC
------------------

domibusConnectorWebLogicStarter.war

The application expects a jndi DataSource configured with the name "domibusWebConnectorDS". Please configure a datasource 
with this name and deploy the application to your weblogic server. Make sure that the database is already initialized.

If you need to customize the deployment consult your application server documentation and create a custom deployment descriptor.
