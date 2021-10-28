package eu.domibus.connector.ui.service;

import org.springframework.stereotype.Service;

import eu.domibus.connector.test.service.DCConnector2ConnectorTestService;

@Service("webConnectorTestService")
public class WebConnectorTestService {

	private final DCConnector2ConnectorTestService connectorTestService;
	
	public WebConnectorTestService(DCConnector2ConnectorTestService connectorTestService) {
		this.connectorTestService = connectorTestService;
	}

}
