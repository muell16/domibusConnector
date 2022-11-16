package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DC5Message;

public abstract class Expression {
    abstract boolean evaluate(DC5Message message);
}
