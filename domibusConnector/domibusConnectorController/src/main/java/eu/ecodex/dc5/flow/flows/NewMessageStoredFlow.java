package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.ecodex.dc5.events.DC5EventListener;
import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.flow.steps.DC5LookupDomainStep;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.MessageModelHelper;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewMessageStoredFlow {

    private final DC5MessageRepo messageRepo;
    private final DC5LookupDomainStep lookupDomainStep;
    private final MessageProcessManager processManager;
    private final ConfirmationMessageFlow confirmationMessageFlow;
    private final ProcessIncomingBusinessMessageFlow processIncomingBusinessMessageFlow;

    @DC5EventListener //implicit transactional and resumes implicit current message process
    public void handleNewMessageStoredEvent(NewMessageStoredEvent newMessageStoredEvent) {
        processManager.startProcess();
        Optional<DC5Message> byId = messageRepo.findById(newMessageStoredEvent.getMessageId()); //load message
        if (!byId.isPresent()) {
            String error = String.format("Unable to find message with id [%s] in MessageRepository", newMessageStoredEvent.getMessageId());
            throw new IllegalStateException(error);
        }
        DC5Message msg = byId.get();

        msg = lookupDomainStep.lookupDomain(msg);

        if (MessageModelHelper.isEvidenceTriggerMessage(msg) || MessageModelHelper.isEvidenceMessage(msg)) {
            confirmationMessageFlow.processMessage(msg);
        }

        if (DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND.equals(msg.getDirection() )) {
            processIncomingBusinessMessageFlow.processMessage(msg);
        }
        //if message is a business message to backend
        //run gateway2backend flow

        //if message is a confirmation message
        //run confirmation message flow

    }


}
