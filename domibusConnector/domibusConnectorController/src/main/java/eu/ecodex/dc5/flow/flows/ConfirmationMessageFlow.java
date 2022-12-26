package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.flow.events.MessageReadyForTransportEvent;
import eu.ecodex.dc5.flow.steps.MessageConfirmationStep;
import eu.ecodex.dc5.flow.steps.ValidateMessageConfirmationStep;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.ecodex.dc5.flow.steps.EvidenceTriggerStep;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.validation.ConfirmationMessageRules;
import eu.ecodex.dc5.message.validation.EvidenceTriggerMessageRules;
import eu.ecodex.dc5.message.validation.IncomingBusinessMesssageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConfirmationMessageFlow {


    private final EvidenceTriggerStep evidenceTriggerStep;
    private final FindBusinessMessageByMsgId findBusinessMessageByMsgId;
    private final ValidateMessageConfirmationStep validateMessageConfirmationStep;
    private final MessageConfirmationStep messageConfirmationStep;
    private final Validator validator;
    private final ConnectorMessageProcessingProperties messageProcessingProperties;


    private final ApplicationEventPublisher eventPublisher;

    public void processMessage(DC5Message message) {
        // validate message

        boolean isEvidenceTriggerMessage = MessageModelHelper.isEvidenceTriggerMessage(message);
        if (isEvidenceTriggerMessage || message.getEbmsData() == null) {
//            confirmationCreatorService.createConfirmation()
            validateEvidenceTriggerMessage(message);
            message = evidenceTriggerStep.executeStep(message);
        }
        validateConfirmationMessage(message);




        DomibusConnectorMessageDirection revertedDirection = DomibusConnectorMessageDirection.revert(message.getDirection());
        DC5Message businessMsg = findBusinessMessageByMsgId.findBusinessMessageByIdAndDirection(message, revertedDirection);

        message.setRefToConnectorMessageId(businessMsg.getRefToConnectorMessageId());

        //set ref to backend message id (backendId of business message)
        BackendMessageId businessMessageBackendId = businessMsg.getBackendData().getBackendMessageId();
        log.debug("Setting refToBackendMessageId to [{}]", businessMessageBackendId);
        if (message.getBackendData() == null) {
            message.setBackendData(DC5BackendData.builder()
                    .build());
        }
        message.getBackendData().setRefToBackendMessageId(businessMessageBackendId);

        //set ref to message id (EBMSID of business message)
        EbmsMessageId businessMessageEbmsId = businessMsg.getEbmsData().getEbmsMessageId();
        log.debug("Setting refToMessageId to [{}]", businessMessageEbmsId);
        message.getEbmsData().setRefToEbmsMessageId(businessMessageEbmsId);



        validateMessageConfirmationStep.executeStep(message);

        //process confirmation...
        messageConfirmationStep.processConfirmationForMessage(businessMsg, message.getTransportedMessageConfirmation());

        if (message.getBackendLinkName() == null) {
            message.setBackendLinkName(businessMsg.getBackendLinkName());
        }
        if (message.getGatewayLinkName() == null) {
            message.setGatewayLinkName(businessMsg.getGatewayLinkName());
        }

        MessageReadyForTransportEvent messageReadyForTransportEvent =
                MessageReadyForTransportEvent.of(message.getId(), message.getTargetLinkName(), message.getTarget());
        eventPublisher.publishEvent(messageReadyForTransportEvent);

        //if activated send triggered evidence back
        if (isEvidenceTriggerMessage
                && messageProcessingProperties.isSendGeneratedEvidencesToBackend()
                && message.getSource() == MessageTargetSource.BACKEND) {
            MessageReadyForTransportEvent messageReadyForTransportEvent2 =
                    MessageReadyForTransportEvent.of(message.getId(), message.getBackendLinkName(), message.getSource());
            eventPublisher.publishEvent(messageReadyForTransportEvent2);
        }

    }

    private void validateEvidenceTriggerMessage(DC5Message message) {
        Set<ConstraintViolation<DC5Message>> validate = validator.validate(message, EvidenceTriggerMessageRules.class);

        if (!validate.isEmpty()) {
            throw new IllegalArgumentException("Message is not a valid trigger message due: " + validate
                    .stream()
                    .map(v -> v.getPropertyPath() + " : " + v.getMessage())
                    .collect(Collectors.joining("\n")));
        }
    }

    private void validateConfirmationMessage(DC5Message message) {
        Set<ConstraintViolation<DC5Message>> validate = validator.validate(message, ConfirmationMessageRules.class);

        if (!validate.isEmpty()) {
            throw new IllegalArgumentException("Message is not a valid confirmation message due: " + validate
                    .stream()
                    .map(v -> v.getPropertyPath() + " : " + v.getMessage())
                    .collect(Collectors.joining("\n")));
        }
    }


}
