package eu.domibus.connectorplugins.link.testbackend;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.c2ctests.config.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties.AS4Action;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties.AS4Service;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain.BusinessDomainId;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.test.service.DCConnector2ConnectorTestService;

@Service
public class DCConnector2ConnectorTestServiceImpl implements DCConnector2ConnectorTestService {

	private static final Logger LOGGER = LogManager.getLogger(DCConnector2ConnectorTestServiceImpl.class);
	
	private final ConfigurationPropertyManagerService configurationPropertyLoaderService;
	private final DomibusConnectorDomainMessageTransformerService transformerService;
	private final DomibusConnectorMessageIdGenerator messageIdGenerator;
	private final SubmitToConnector submitToConnector;

	public DCConnector2ConnectorTestServiceImpl(ConfigurationPropertyManagerService configurationPropertyLoaderService,
												DomibusConnectorDomainMessageTransformerService transformerService,
												DomibusConnectorMessageIdGenerator messageIdGenerator,
												SubmitToConnector submitToConnector) {
		this.configurationPropertyLoaderService = configurationPropertyLoaderService;
		this.transformerService = transformerService;
		this.messageIdGenerator = messageIdGenerator;
		this.submitToConnector = submitToConnector;
	}

	@Override
	public void submitTestMessage(DomibusConnectorMessageType testMessage) {
		DomibusConnectorLinkPartner.LinkPartnerName lpName = DomibusConnectorLinkPartner.LinkPartnerName.of(getTestBackendName());

		SubmitToConnector.ReceiveMessageFlowResult receiveMessageFlowResult = submitToConnector.receiveMessage(testMessage, (m, p) -> {
			return transformerService.transformTransitionToDomain(MessageTargetSource.GATEWAY, lpName, m, messageIdGenerator.generateDomibusConnectorMessageId());
		});
		//TODO: process result: receiveMessageFlowResult
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
