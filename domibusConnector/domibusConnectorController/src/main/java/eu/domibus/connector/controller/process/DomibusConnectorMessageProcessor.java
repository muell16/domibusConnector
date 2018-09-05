package eu.domibus.connector.controller.process;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public interface DomibusConnectorMessageProcessor {

	void processMessage(DomibusConnectorMessage message);

}