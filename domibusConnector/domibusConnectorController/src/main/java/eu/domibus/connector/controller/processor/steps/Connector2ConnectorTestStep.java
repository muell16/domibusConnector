package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.processor.util.ConfirmationCreatorService;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.spring.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Handles connector2connector tests
 *  checks if the message service and action
 *  attributes are matching the configured
 *  service and action names for connector2connector
 *  tests
 *  If this is the case this step sends back a
 *  deliveryConfirmation and returns true,
 *  so the calling process should stop message
 *  processing
 *
 */
@Component
public class Connector2ConnectorTestStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(Connector2ConnectorTestStep.class);

    private final ConnectorTestConfigurationProperties connectorTestConfigurationProperties;
    private final SubmitConfirmationAsEvidenceMessageStep submitConfirmationAsEvidenceMessageStep;
    private final ConfirmationCreatorService confirmationCreatorService;
    private final MessageConfirmationStep messageConfirmationStep;

    public Connector2ConnectorTestStep(ConnectorTestConfigurationProperties connectorTestConfigurationProperties,
                                       SubmitConfirmationAsEvidenceMessageStep submitConfirmationAsEvidenceMessageStep,
                                       ConfirmationCreatorService confirmationCreatorService,
                                       MessageConfirmationStep messageConfirmationStep) {
        this.connectorTestConfigurationProperties = connectorTestConfigurationProperties;
        this.submitConfirmationAsEvidenceMessageStep = submitConfirmationAsEvidenceMessageStep;
        this.confirmationCreatorService = confirmationCreatorService;
        this.messageConfirmationStep = messageConfirmationStep;
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
    public boolean executeStep(DomibusConnectorMessage message) {
        boolean isConnectorTConnectorTest = isConnector2ConnectorTest(message);
        if (isConnectorTConnectorTest) {
            LOGGER.info(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] is a connector to connector test originalMessage.\nIt will NOT be delivered to the backend!", message);

            //create DeliveryConfirmation
            DomibusConnectorMessageConfirmation deliveryConfirmation = confirmationCreatorService.createDelivery(message);

            //process created confirmation for message
            messageConfirmationStep.processConfirmationForMessage(message, deliveryConfirmation);

            //return evidence as message
            submitConfirmationAsEvidenceMessageStep.submitOppositeDirection(null, message, deliveryConfirmation);
        }

        //stop processing if con2con test
        return !isConnectorTConnectorTest;
    }


}
