# Changelog


## 4.0

 * Connector Framwork split into Connector, Connector Client
 * Different Connector Clients possibly, routing decision is made on service name 
 * Seperation between Connector core logic, communication to connectorClient and gateway plugin to make
    future support of different connection technologies possibly like jms
 * internal data model adapted to make streaming based message processing possibly in future releases to make 
 * spring boot integrated
 * database schema creation managed by liquibase
 * gateway plugin uses different AS4 profile for messages generation on the gateway due end of life 
 of the ecodex profile - this change makes the 4.0 connector incompatible with 3.x
 
## 4.1 (planned)

  * implement jms communication
  * admin interface rest
  * angular js admin interface
  
## in discussion / ideas

  * multiple storage implementations for message content (asic-s container, business files), not for
  the business xml 
    * file storage 
    * java content repository support for the message content
   
       
  
