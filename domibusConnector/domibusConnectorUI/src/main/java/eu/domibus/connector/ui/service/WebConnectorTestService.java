package eu.domibus.connector.ui.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import eu.domibus.connector.test.service.DCConnector2ConnectorTestService;


@ConditionalOnBean(DCConnector2ConnectorTestService.class) 	//it might be possible, if the testbackend plugin is not enabled, that the service DCConnector2ConnectorTestService is not available!, in this case do not create WebConnectorTestService bean.
//TODO: respect the fact that this service bean might not be available
@Service("webConnectorTestService")
public class WebConnectorTestService {

	private final DCConnector2ConnectorTestService connectorTestService;

	public WebConnectorTestService(DCConnector2ConnectorTestService connectorTestService) {
		this.connectorTestService = connectorTestService;
	}

}
