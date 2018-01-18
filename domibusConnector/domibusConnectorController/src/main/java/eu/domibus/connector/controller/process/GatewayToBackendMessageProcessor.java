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
		
	}

}
