package eu.domibus.connector.link.impl.testbackend;

import org.springframework.stereotype.Service;

import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.config.c2ctests.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties.AS4Action;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties.AS4Service;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain.BusinessDomainId;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.test.service.DCConnector2ConnectorTestService;

@Service
public class DCConnector2ConnectorTestServiceImpl implements DCConnector2ConnectorTestService {
	
	private final ConfigurationPropertyManagerService configurationPropertyLoaderService;

	public DCConnector2ConnectorTestServiceImpl(ConfigurationPropertyManagerService configurationPropertyLoaderService) {
		this.configurationPropertyLoaderService = configurationPropertyLoaderService;
	}

	@Override
	public void submitTestMessage(DomibusConnectorMessageType testMessage) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public AS4Service getTestService(BusinessDomainId laneId) {
		ConnectorTestConfigurationProperties c2cTestProperties = configurationPropertyLoaderService.loadConfiguration(laneId, ConnectorTestConfigurationProperties.class);
		return c2cTestProperties.getService();
	}
	
	@Override
	public AS4Action getTestAction(BusinessDomainId laneId) {
		ConnectorTestConfigurationProperties c2cTestProperties = configurationPropertyLoaderService.loadConfiguration(laneId, ConnectorTestConfigurationProperties.class);
		return c2cTestProperties.getAction();
	}
	
	
	
	

}
