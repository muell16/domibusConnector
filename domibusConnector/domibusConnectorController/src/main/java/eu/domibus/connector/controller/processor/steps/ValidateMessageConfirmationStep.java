package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.common.service.ConfigurationPropertyLoaderService;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.processor.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateMessageConfirmationStep implements MessageProcessStep {

    private final CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactory;
    private final ConfigurationPropertyLoaderService configurationPropertyLoaderService;

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "ValidateMessageConfirmationStep")
    public boolean executeStep(final DomibusConnectorMessage domibusConnectorMessage) {
        domibusConnectorMessage
                .getTransportedMessageConfirmations()
                .forEach(c -> this.validateConfirmation(domibusConnectorMessage, c));

        if (domibusConnectorMessage.getTransportedMessageConfirmations().size() == 1) {
            validateActionService(domibusConnectorMessage);
        }
        return true;
    }

    private void validateActionService(DomibusConnectorMessage domibusConnectorMessage) {
        EvidenceActionServiceConfigurationProperties evidenceActionServiceConfigurationProperties =
                configurationPropertyLoaderService.loadConfiguration(DomibusConnectorMessageLane.getDefaultMessageLaneId(), EvidenceActionServiceConfigurationProperties.class);
        boolean enforcing = evidenceActionServiceConfigurationProperties.isEnforceServiceActionNames();

        DomibusConnectorMessageConfirmation confirmation = domibusConnectorMessage.getTransportedMessageConfirmations().get(0);
        DomibusConnectorEvidenceType evidenceType = confirmation.getEvidenceType();
        DomibusConnectorAction requiredEvidenceAction = createConfirmationMessageBuilderFactory.createEvidenceAction(evidenceType);

        DomibusConnectorAction action = domibusConnectorMessage.getMessageDetails().getAction();
        if (!requiredEvidenceAction.equals(action)) {
            String error = String.format("Enforcing the AS4 action is [%s] and the action [%s] is illegal for this type [%s] of evidence", enforcing, action, evidenceType);
            if (enforcing) {
                throwError(domibusConnectorMessage, error);
            } else {
                log.warn(error);
            }
        }



    }

    private void validateConfirmation(DomibusConnectorMessage msg, DomibusConnectorMessageConfirmation confirmation) {
        if (confirmation.getEvidenceType() == null) {
            throwError(msg, "The evidence type is null!");
        }
        if (confirmation.getEvidence() == null) {
            throwError(msg, "The evidence itself is null!");
        }
    }

    private void throwError(DomibusConnectorMessage msg, String text) {
        throw new DomibusConnectorMessageException(msg, ValidateMessageConfirmationStep.class, text);
    }

}
