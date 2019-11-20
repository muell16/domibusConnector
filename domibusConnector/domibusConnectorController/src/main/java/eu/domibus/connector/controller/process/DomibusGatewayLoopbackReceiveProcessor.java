package eu.domibus.connector.controller.process;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class DomibusGatewayLoopbackReceiveProcessor implements DomibusConnectorMessageProcessor {

    private static final Logger LOGGER = LogManager.getLogger(DomibusGatewayLoopbackReceiveProcessor.class);

    String removedSuffix = "_1";

    @Override
    public void processMessage(DomibusConnectorMessage message) {
        DomibusConnectorMessageDetails messageDetails = message.getMessageDetails();
        String ebmsMessageId = messageDetails.getEbmsMessageId();
        String ebmsMessageIdRemovedSuffix = ebmsMessageId.endsWith(removedSuffix) ? ebmsMessageId.substring(0, ebmsMessageId.length() - removedSuffix.length()) : ebmsMessageId;
        LOGGER.info("Removing suffix [{}] from incoming message ebmsId, for details see domibus gateway admin guide 9.3 Message ID convention");
        messageDetails.setEbmsMessageId(ebmsMessageIdRemovedSuffix);
    }
}
