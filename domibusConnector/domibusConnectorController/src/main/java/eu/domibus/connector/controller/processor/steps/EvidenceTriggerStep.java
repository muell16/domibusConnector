package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.processor.DomibusConnectorMessageProcessor;
import eu.domibus.connector.controller.processor.EvidenceMessageProcessor;
import eu.domibus.connector.controller.processor.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.processor.util.FindBusinessMessageByMsgId;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvidenceTriggerStep implements MessageProcessStep {

    private final FindBusinessMessageByMsgId findBusinessMessageByMsgId;
    private final CreateConfirmationMessageBuilderFactoryImpl confirmationMessageService;

    @Override
    @StoreMessageExceptionIntoDatabase
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "VerifyPModes")
    public boolean executeStep(DomibusConnectorMessage evidenceTriggerMsg) {

        //is evidence triggering allowed
        isEvidenceTriggeringAllowed(evidenceTriggerMsg);

        //only incoming evidence messages are looked up
        DomibusConnectorMessage businessMsg = findBusinessMessageByMsgId.findBusinessMessageByIdAndDirection(evidenceTriggerMsg, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);

        DomibusConnectorEvidenceType evidenceType = getEvidenceType(evidenceTriggerMsg);

        //create evidence
        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder
                = confirmationMessageService.createConfirmationMessageBuilderFromBusinessMessage(businessMsg, evidenceType);
        log.info(LoggingMarker.BUSINESS_LOG, "Successfully created evidence [{}] for evidence trigger", evidenceType);

        return true;
    }

    private void isEvidenceTriggeringAllowed(DomibusConnectorMessage evidenceTriggerMsg) {
        if (!DomainModelHelper.isEvidenceTriggerMessage(evidenceTriggerMsg)) {
            throwException(evidenceTriggerMsg, "The message is not an evidence trigger message!");
        }
        MessageTargetSource source = evidenceTriggerMsg.getMessageDetails().getDirection().getSource();
        if (source != MessageTargetSource.BACKEND && source != MessageTargetSource.CONNECTOR) {
            throwException(evidenceTriggerMsg, "Only connector OR backend can generate trigger messages");
        }
    }

    private void throwException(DomibusConnectorMessage evidenceTriggerMsg, String s) {
        throw new DomibusConnectorMessageException(evidenceTriggerMsg, EvidenceTriggerStep.class, s);
    }

    private DomibusConnectorEvidenceType getEvidenceType(DomibusConnectorMessage evidenceTriggerMsg) {
        if (!DomainModelHelper.isEvidenceTriggerMessage(evidenceTriggerMsg)) {
            throw new DomibusConnectorMessageException(evidenceTriggerMsg, EvidenceTriggerStep.class, "The message is not an evidence trigger msg!");
        }
        DomibusConnectorMessageConfirmation msgConfirmation = evidenceTriggerMsg.getTransportedMessageConfirmations().get(0);
        return msgConfirmation.getEvidenceType();
    }


}
