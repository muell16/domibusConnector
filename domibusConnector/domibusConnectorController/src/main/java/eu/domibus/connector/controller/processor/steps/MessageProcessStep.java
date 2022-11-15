package eu.domibus.connector.controller.processor.steps;

import eu.ecodex.dc5.message.model.DomibusConnectorMessage;

public interface MessageProcessStep {

    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage);

}
