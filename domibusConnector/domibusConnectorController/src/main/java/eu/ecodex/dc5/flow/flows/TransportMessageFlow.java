package eu.ecodex.dc5.flow.flows;


import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.ecodex.dc5.events.DC5EventListener;
import eu.ecodex.dc5.flow.events.MessageReadyForTransportEvent;
import eu.ecodex.dc5.flow.events.MessageTransportEvent;
import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransportMessageFlow {

    private final SubmitToLinkService submitToLinkService;
    private final DC5MessageRepo dc5MessageRepo;

    @DC5EventListener //implicit transactional and resumes implicit current message process
    public void handleMessageReadyForTransport(MessageReadyForTransportEvent messageReadyForTransportEvent) {

        DC5Message msg = dc5MessageRepo.getById(messageReadyForTransportEvent.getId());

        submitToLinkService.submitToLink(msg);

    }

    @DC5EventListener
    public void handleMessageTransportEvent(MessageTransportEvent messageTransportEvent) {
        //TODO: handle transport events
        //submitted

        // update message attributes (backend messageId, ebmsId,
        // send already in message existing submission_acceptance (when submission success)
        // trigger submission_rejection (when submission to gw failed)
        // trigger non_delivery (when transport to backend failed)

    }

}
