# Domibus Web Connector 4.0


## Module description

The project contains multiple maven modules. The following list gives a short introduction about these modules.


| Module                                                     | Description                                                                             |
|------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| domibusConnectorTest                                       | Contains multiple child modules related to testing |
| domibusConnectorTest/domibusConnectorStarter               | Contains multiple child modules with different starting configurations |
| domibusConnectorTest/domibusConnectorStarter/domibusConnectorOnlyStarter | Starts only the connector without webAppModule, also packaged als spring boot .jar application. Starts its own tomcat and embedded database. Also uses coded reload and is used for development. Also used to have a environment for client development |