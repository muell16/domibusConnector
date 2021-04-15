package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.spring.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class Connector2ConnectorTestStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(Connector2ConnectorTestStep.class);

    private final ConnectorTestConfigurationProperties connectorTestConfigurationProperties;
    private final SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep;
    private final SubmitToConnector submitToConnector;

    public Connector2ConnectorTestStep(ConnectorTestConfigurationProperties connectorTestConfigurationProperties,
                                       SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep,
                                       SubmitToConnector submitToConnector) {
        this.connectorTestConfigurationProperties = connectorTestConfigurationProperties;
        this.submitMessageToLinkModuleQueueStep = submitMessageToLinkModuleQueueStep;
        this.submitToConnector = submitToConnector;
    }

    private boolean isConnector2ConnectorTest(DomibusConnectorMessage message) {

        EvidenceActionServiceConfigurationProperties.AS4Service connectorTestService = connectorTestConfigurationProperties.getService();
        EvidenceActionServiceConfigurationProperties.AS4Action connectorTestAction = connectorTestConfigurationProperties.getAction();

        return connectorTestConfigurationProperties.isEnabled() &&
                connectorTestService.getConnectorService().equals(message.getMessageDetails().getService()) &&
                connectorTestAction.getConnectorAction().equals(message.getMessageDetails().getAction());
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "Connector2ConnectorTestStep")
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        boolean isConnectorTConnectorTest = isConnector2ConnectorTest(domibusConnectorMessage);
        //TODO: do work for con2con test here....
        if (isConnectorTConnectorTest) {
            createDeliveryEvidenceTrigger(domibusConnectorMessage);
        }
        
//			if(isConnector2ConnectorTest(domibusConnectorMessage)){
				// if it is a connector to connector test originalMessage defined by service and action, do NOT deliver originalMessage to the backend, but
				// only send a DELIVERY evidence back.
//				log.info("#processMessage: Message [{}] is a connector to connector test originalMessage. \nIt will NOT be delivered to the backend!", domibusConnectorMessage);
//				createDeliveryEvidenceAndSendIt(validatedMessage);
//                createDeliveryEvidenceTrigger();
//
//				log.info("#processMessage: Connector to Connector Test originalMessage [{}] is confirmed!", domibusConnectorMessage);
//			} else {
//
//			}

        //stop processing if con2con test
        return !isConnectorTConnectorTest;

    }

    private void createDeliveryEvidenceTrigger(DomibusConnectorMessage businessMessage) {
        //TODO: create trigger and put it on queue...
    }

}
