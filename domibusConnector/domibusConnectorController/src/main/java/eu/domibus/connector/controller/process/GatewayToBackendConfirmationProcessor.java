package eu.domibus.connector.controller.process;

import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

@Component("GatewayToBackendConfirmationProcessor")
public class GatewayToBackendConfirmationProcessor implements DomibusConnectorMessageProcessor {

	public GatewayToBackendConfirmationProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage(DomibusConnectorMessage message) {
		// TODO Auto-generated method stub

	}

}
