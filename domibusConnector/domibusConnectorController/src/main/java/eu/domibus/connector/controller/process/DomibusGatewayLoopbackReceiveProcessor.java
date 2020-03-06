package eu.domibus.connector.controller.process;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * This processor adds a quick fix to
 * set the EBMS id by an domibus gw received loopbacked message
 * to the same EBMSID as the sent message, because
 * domibus gw is adding a _1 suffix to a loopbacked message,
 * for the connector and all other applications this message would look like
 * a new message, refToMessageId is then broken so evidence can not be
 * assigned to the correct message, ...
 *
 * So we are just removing the _1 suffix if we get any here.
 * This code will be moved to the domibusConnectorPlugin
 *
 *
 */
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