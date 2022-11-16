package eu.domibus.connector.controller.processor;

import eu.ecodex.dc5.message.model.DC5Message;
import org.springframework.stereotype.Service;

/**
 * This processor is called after a business message
 * has been rejected or confirmed
 *
 * delegates deletion of message content
 *
 */
@Service
public class CleanupMessageProcessor implements DomibusConnectorMessageProcessor {

//    private final DCMessageContentManager dcMessageContentManager;

    public CleanupMessageProcessor() {
//        this.dcMessageContentManager = dcMessageContentManager;
    }

    @Override
    public void processMessage(DC5Message message) {

//        dcMessageContentManager.cleanForMessage(message);
    }

}
