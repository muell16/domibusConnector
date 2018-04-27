# Backend message routing

How are the messages distributed to the different connectorClients?

The message routing is done at the class 
*eu.domibus.connector.backend.ws.link.impl.DeliverMessageFromControllerToBackendService*

![MessageRoutingActivityDiagram](../images/domibusConnectorBackendLink_messageRouting.gif)