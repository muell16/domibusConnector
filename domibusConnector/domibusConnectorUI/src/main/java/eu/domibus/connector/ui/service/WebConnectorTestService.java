package eu.domibus.connector.ui.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties.AS4Action;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties.AS4Service;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.test.service.DCConnector2ConnectorTestService;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.dto.WebMessageDetail;


@ConditionalOnBean(DCConnector2ConnectorTestService.class) 	//it might be possible, if the testbackend plugin is not enabled, that the service DCConnector2ConnectorTestService is not available!, in this case do not create WebConnectorTestService bean.
@Service("webConnectorTestService")
public class WebConnectorTestService {

	private final DCConnector2ConnectorTestService connectorTestService;

	public WebConnectorTestService(DCConnector2ConnectorTestService connectorTestService) {
		this.connectorTestService = connectorTestService;
	}
	
	public void submitTestMessage(WebMessage testMsg) {
		
	}
	
	public WebMessageDetail.Service getTestService() {
		AS4Service testService = connectorTestService.getTestService(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
		WebMessageDetail.Service service = new WebMessageDetail.Service(testService.getName(), testService.getServiceType());
		return service;
	}
	
	public WebMessageDetail.Action getTestAction() {
		AS4Action testAction = connectorTestService.getTestAction(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
		WebMessageDetail.Action action = new WebMessageDetail.Action(testAction.getAction());
		return action;
	}
	
	public String getConnectorTestBackendName() {
		return connectorTestService.getTestBackendName();
	}

}
