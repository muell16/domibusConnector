package eu.domibus.connector.controller.process;

import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

@Component("GatewayToBackendMessageProcessor")
public class GatewayToBackendMessageProcessor implements DomibusConnectorMessageProcessor {
	
	/* (non-Javadoc)
	 * @see eu.domibus.connector.controller.process.DomibusConnectorMessageProcessor#processMessage(eu.domibus.connector.domain.model.DomibusConnectorMessage)
	 */
	@Override
	public void processMessage(DomibusConnectorMessage message) {
		
		
		//create RelayREMMDEvidence and send it back to sender
		
		
		// call of security toolkit to unpack ASIC-S container
		
		// check for Connector 2 Connector Test
			// if, then send back delivery evidence
			// else, send message to backend
		
		// if success, mark message as delivered in database
	}

}
