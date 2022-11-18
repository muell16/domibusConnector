package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.ConfirmationCreatorService;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.ecodex.dc5.message.model.DC5Action;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Confirmation;
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
public class ValidateMessageConfirmationStep {

    private static final Logger LOGGER = LogManager.getLogger(ValidateMessageConfirmationStep.class);

    private final ConfirmationCreatorService confirmationCreatorService;
    private final ConfigurationPropertyManagerService configurationPropertyLoaderService;

    public ValidateMessageConfirmationStep(ConfirmationCreatorService confirmationCreatorService,
                                           ConfigurationPropertyManagerService configurationPropertyLoaderService) {
        this.confirmationCreatorService = confirmationCreatorService;
        this.configurationPropertyLoaderService = configurationPropertyLoaderService;
    }

    @Step(name = "ValidateMessageConfirmationStep")
    public DC5Message executeStep(final DC5Message msg) {
        msg
                .getTransportedMessageConfirmations()
                .forEach(c -> this.validateConfirmation(msg, c));

        if (msg.getTransportedMessageConfirmations().size() == 1) {
            validateActionService(msg);
        }
        return msg;
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
