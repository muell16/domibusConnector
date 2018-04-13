# Architectural Overview


## Overall ECodex Architecture

The domibusConnector (connector) is placed behind the Gateway ([Possibly GW Solutions](https://ec.europa.eu/cefdigital/wiki/display/CEFDIGITAL/e-SENS+AS4+conformant+solutions) - usually the Domibus GW is used). 
The connector connects the GW (Gateway) with the national implementation. The national implementation can be
a whole and complex system or just a single application.

![EXEC Architecture Overview](../images/EXEC_architecture_overview.gif)




## Connector Architecture Overview

![Connector Overview](../images/domibusConnectorOverview.gif)
(Reference Setup)

The connectorSuite consists mainly 3 components:

* ConnectorClient
* Connector itself
* Domibus Gateway Connector Plugin

### ConnectorClient 
Connector Client is the bridge between the national system or application and the connector. It is
 responsibly for mapping (if necessary) the international message to the national message. 

The connector client can call the service interface on the connector directly or can use the 
provided ConnectorClientLib. 

For further information read the connectorClient documentation.

### Domibus Gateway Connector Plugin

The Gateway Plugin is installed as plugin at the Domibus Gateway and communicates with the connector 
over a webservice. 
