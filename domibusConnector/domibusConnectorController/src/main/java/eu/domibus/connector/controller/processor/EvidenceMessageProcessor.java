package eu.domibus.connector.controller.processor;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.processor.steps.EvidenceTriggerStep;
import eu.domibus.connector.controller.processor.steps.MessageConfirmationStep;
import eu.domibus.connector.controller.processor.steps.SubmitMessageToLinkModuleQueueStep;
import eu.domibus.connector.controller.processor.steps.ValidateMessageConfirmationStep;
import eu.domibus.connector.controller.processor.util.FindBusinessMessageByMsgId;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvidenceMessageProcessor implements DomibusConnectorMessageProcessor {

    private final EvidenceTriggerStep evidenceTriggerStep;
    private final FindBusinessMessageByMsgId findBusinessMessageByMsgId;
    private final MessageConfirmationStep messageConfirmationStep;
    private final ValidateMessageConfirmationStep validateMessageConfirmationStep;
    private final SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep;

    @Override
//    @Transactional(Transactional.TxType.REQUIRES_NEW) //start new nested transaction...
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = "EvidenceMessageProcessor")
    public void processMessage(DomibusConnectorMessage message) {

        if (DomainModelHelper.isEvidenceTriggerMessage(message)) {
            evidenceTriggerStep.executeStep(message);
        }

        DomibusConnectorMessageDirection revertedDirection = DomibusConnectorMessageDirection.revert(message.getMessageDetails().getDirection());
        DomibusConnectorMessage businessMsg = findBusinessMessageByMsgId.findBusinessMessageByIdAndDirection(message, revertedDirection);

        validateMessageConfirmationStep.executeStep(message);

        messageConfirmationStep.processConfirmationForMessage(businessMsg, message.getTransportedMessageConfirmations().get(0));

        submitMessageToLinkModuleQueueStep.submitMessage(message);
    }

}
