package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.spring.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * TODO: con2con tests should work as specific backend
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Connector2ConnectorTestStep implements MessageProcessStep {

    private final ConnectorTestConfigurationProperties connectorTestConfigurationProperties;
    private final SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep;
    private final SubmitToConnector submitToConnector;

    private boolean isConnector2ConnectorTest(DomibusConnectorMessage message) {
        String connectorTestService = connectorTestConfigurationProperties.getService();
        String connectorTestAction = connectorTestConfigurationProperties.getAction();

        return (!StringUtils.isEmpty(connectorTestService) && message.getMessageDetails().getService().getService().equals(connectorTestService))
                && (!StringUtils.isEmpty(connectorTestAction) && message.getMessageDetails().getAction().getAction().equals(connectorTestAction));
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "Connector2ConnectorTestStep")
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        //TODO: do work for con2con test here..
			if(isConnector2ConnectorTest(domibusConnectorMessage)){
				// if it is a connector to connector test originalMessage defined by service and action, do NOT deliver originalMessage to the backend, but
				// only send a DELIVERY evidence back.
				log.info("#processMessage: Message [{}] is a connector to connector test originalMessage. \nIt will NOT be delivered to the backend!", domibusConnectorMessage);
//				createDeliveryEvidenceAndSendIt(validatedMessage);
//                createDeliveryEvidenceTrigger();

				log.info("#processMessage: Connector to Connector Test originalMessage [{}] is confirmed!", domibusConnectorMessage);
			} else {
				try {
//					submitMessageToLinkModuleQueueStep.submitMessage(validatedMessage);
				} catch (Exception e) {
//					createNonDeliveryEvidenceAndSendIt(validatedMessage);
				}
			}

        //stop processing if con2con test
        return !isConnector2ConnectorTest(domibusConnectorMessage);

    }

    private void createDeliveryEvidenceTrigger(DomibusConnectorMessage domibusConnectorMessage) {

    }

}
