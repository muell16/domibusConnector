package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DomibusConnectorMessage;

public abstract class Expression {
    abstract boolean evaluate(DomibusConnectorMessage message);
}
