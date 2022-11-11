package eu.ecodex.dc5.flow.flows;

import eu.ecodex.dc5.core.model.DC5Msg;
import eu.ecodex.dc5.core.repository.DC5MessageRepo;
import eu.ecodex.dc5.events.DC5EventListener;
import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.flow.steps.DC5LookupDomainStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewMessageStoredFlow {

    private final DC5MessageRepo messageRepo;
    private final DC5LookupDomainStep lookupDomainStep;

    @DC5EventListener //implicit transactional and resumes implicit current message process
    public void handleNewMessageStoredEvent(NewMessageStoredEvent newMessageStoredEvent) {

//        Optional<DC5Msg> byId = messageRepo.findById(newMessageStoredEvent.getMessageId()); //load message
//        if (!byId.isPresent()) {
//            String error = String.format("Unable to find message with id [%s] in MessageRepository", newMessageStoredEvent.getMessageId());
//            throw new IllegalStateException(error);
//        }
//        DC5Msg msg = byId.get();
//
//        msg = lookupDomainStep.lookupDomain(msg);

//        if (isConfirmationMessage(msg)) {
//            confirmationMessageProcessingFlow.processMessage(msg);
//        }

        //if message is a business message to backend
        //run gateway2backend flow

        //if message is a confirmation message
        //run confirmation message flow

    }


    private boolean isConfirmationMessage(DC5Msg msg) {
        return (msg.getConfirmations().size() == 1 && !msg.getContent().isPresent());
    }

}
