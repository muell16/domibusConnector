package eu.domibus.connector.controller.processor;

import eu.ecodex.dc5.message.model.DC5Message;

/**
 * Connector Controller internal API
 *
 * used to process messages (business messages, confirmation messages)
 * going through the connector
 *
 */
public interface DomibusConnectorMessageProcessor {

	void processMessage(DC5Message message);

}