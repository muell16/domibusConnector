package eu.ecodex.dc5.flow.common;

import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5MessageId;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SubmitConfirmationMsg {

    private final MessageProcessManager messageProcessManager;
    private final DC5MessageRepo messageRepo;

    private final ApplicationEventPublisher eventPublisher;

    public SubmitConfirmationMsg(MessageProcessManager messageProcessManager, DC5MessageRepo messageRepo, ApplicationEventPublisher eventPublisher) {
        this.messageProcessManager = messageProcessManager;
        this.messageRepo = messageRepo;
        this.eventPublisher = eventPublisher;
    }

    public void submitConfirmationMsg(DC5Message businessMessage, DC5Confirmation confirmation) {
        DC5Message.DC5MessageBuilder builder = DC5Message.builder();
        DC5Message evidenceMessage = builder
                .process(messageProcessManager.getCurrentProcess())
                .source(businessMessage.getTarget())
                .target(businessMessage.getSource())
                .transportedMessageConfirmation(confirmation)
                .businessDomainId(businessMessage.getBusinessDomainId())
                .refToConnectorMessageId(businessMessage.getConnectorMessageId())
                .connectorMessageId(DC5MessageId.ofRandom())
                .build();
//        dc5MessageRepo.save(evidenceMessage);
//        NewMessageStoredEvent newMessageStoredEvent = NewMessageStoredEvent.of(evidenceMessage.getId());
//        eventPublisher.publishEvent(newMessageStoredEvent);
        evidenceMessage.setProcess(messageProcessManager.createProcess());
        messageRepo.save(evidenceMessage);
        NewMessageStoredEvent newMessageStoredEvent = NewMessageStoredEvent.of(evidenceMessage.getId());
        eventPublisher.publishEvent(newMessageStoredEvent);
    }
}
