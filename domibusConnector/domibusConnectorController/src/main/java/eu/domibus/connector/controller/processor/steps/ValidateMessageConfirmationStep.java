package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.processor.util.ConfirmationCreatorService;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.ecodex.dc5.message.model.DC5Action;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Does some validation
 * if the Action of this evidence message
 * is correct in respect of the transported
 * evidence type
 */
@Component
public class ValidateMessageConfirmationStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(ValidateMessageConfirmationStep.class);

    private final ConfirmationCreatorService confirmationCreatorService;
    private final ConfigurationPropertyManagerService configurationPropertyLoaderService;

    public ValidateMessageConfirmationStep(ConfirmationCreatorService confirmationCreatorService,
                                           ConfigurationPropertyManagerService configurationPropertyLoaderService) {
        this.confirmationCreatorService = confirmationCreatorService;
        this.configurationPropertyLoaderService = configurationPropertyLoaderService;
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "ValidateMessageConfirmationStep")
    public boolean executeStep(final DC5Message DC5Message) {
        DC5Message
                .getTransportedMessageConfirmations()
                .forEach(c -> this.validateConfirmation(DC5Message, c));

        if (DC5Message.getTransportedMessageConfirmations().size() == 1) {
            validateActionService(DC5Message);
        }
        return true;
    }

    private void validateActionService(DC5Message DC5Message) {
        EvidenceActionServiceConfigurationProperties evidenceActionServiceConfigurationProperties =
                configurationPropertyLoaderService.loadConfiguration(DC5Message.getMessageLaneId(), EvidenceActionServiceConfigurationProperties.class);
        boolean enforcing = evidenceActionServiceConfigurationProperties.isEnforceServiceActionNames();

        DC5Confirmation confirmation = DC5Message.getTransportedMessageConfirmations().get(0);
        DomibusConnectorEvidenceType evidenceType = confirmation.getEvidenceType();
        DC5Action requiredEvidenceAction = confirmationCreatorService.createEvidenceAction(evidenceType);

        DC5Action action = DC5Message.getEbmsData().getAction();
        if (!requiredEvidenceAction.equals(action)) {
            String error = String.format("Enforcing the AS4 action is [%s] and the action [%s] is illegal for this type [%s] of evidence", enforcing, action, evidenceType);
            if (enforcing) {
                throwError(DC5Message, error);
            } else {
                LOGGER.warn(error);
            }
        }
    }

    private void validateConfirmation(DC5Message msg, DC5Confirmation confirmation) {
        if (confirmation.getEvidenceType() == null) {
            throwError(msg, "The evidence type is null!");
        }
        if (confirmation.getEvidence() == null) {
            throwError(msg, "The evidence itself is null!");
        }
    }

    private void throwError(DC5Message msg, String text) {
        throw new DomibusConnectorMessageException(msg, ValidateMessageConfirmationStep.class, text);
    }

}
