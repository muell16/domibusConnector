# INSTALLATION / DEPLOYMENT / CONFIGURATION

The application is delivered as a .war web application. During development the application has been tested on tomcat 7 & 8.
SpringBoot is used to configure and start the web application. The application needs a database connection. This database connection
must be provided by the web application server.




### Database Setup

Setup your database by executing the provided Database scripts. [Set up Database](database.html)

### Configuration

The connector uses spring boot for wiring the components together.
Spring boot also provides a huge range of configuration values and flexibility. 
A overview of all configuration properties is [here](domibusConnectorWebAppModule/spring-properties.html) available.

The application is configured with reasonable defaults. But if you really need to change some configuration you can inject configuration 
by passing them as System Environment variables or by configuring them in the context [Tomcat Deployment Chapter](./tomcat_deploy.html). 

For further information see [spring common-application-properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
But be aware that we have only tested changing the in this documentation named configuration properties.

#### Logging Configuration

You can configure logging in your own log4j2.xml file by setting
    
    logging.config=<location of logging file>

or use the spring boot to configure the logging level by setting the property,
which will configure the root logging level.

    logging.level.root=WARN|INFO|DEBUG|TRACE
    
you can also set the logging level only for the connector components to DEBUG:

    logging.level.eu.domibus.connector=DEBUG
    
        



