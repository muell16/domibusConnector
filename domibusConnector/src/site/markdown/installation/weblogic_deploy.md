DEPLOY ON WEBLOGIC
------------------

The connector application is a spring boot application which should run on a weblogic application
server. 

In its default configuration it expects a jndi DataSource configured with the name 
"domibusWebConnectorDS". 
Please configure a datasource with this name and deploy the application to your weblogic server. 
Make sure that the database is already initialized. You should also set the spring.config.location
parameter to your application.properties so the spring boot application can load the configured
settings.

To do this you need to create a custom deployment descriptor.

[comment]: <> (TODO: extend the description!)