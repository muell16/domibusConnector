package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;

import static eu.domibus.connector.controller.queues.QueuesConfiguration.TO_LINK_QUEUE_BEAN;

@Service
@RequiredArgsConstructor
public class SubmitMessageToLinkModuleQueueStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(SubmitMessageToLinkModuleQueueStep.class);


    @Qualifier(TO_LINK_QUEUE_BEAN)
    private final Queue queue;
    private final JmsTemplate jmsTemplate;

    @Override
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        submitMessage(domibusConnectorMessage);
        return true;
    }

    public void submitMessage(DomibusConnectorMessage message) {
        jmsTemplate.convertAndSend(queue, message);
    }

    /**
     * Submits message to ConnectorToLinkQueue,
     * but before this is done, the following message details are overwritten with
     * the switched originalMessageDetails see {@link DomainModelHelper#switchMessageDirection(DomibusConnectorMessageDetails)}
     *
     * <ul>
     *     <li>direction</li>
     *     <li>fromParty</li>
     *     <li>toParty</li>
     *     <li>originalSender</li>
     *     <li>finalRecipient</li>
     * </ul>
     * @param originalMessage
     * @param message
     */
    public void submitMessageOpposite(DomibusConnectorMessage originalMessage, DomibusConnectorMessage message) {
        DomibusConnectorMessageDetails switchedDetails = DomainModelHelper.switchMessageDirection(originalMessage.getMessageDetails());
        DomibusConnectorMessageDetails msgDetails = message.getMessageDetails();

        msgDetails.setDirection(switchedDetails.getDirection());
        msgDetails.setFromParty(switchedDetails.getFromParty());
        msgDetails.setToParty(switchedDetails.getToParty());
        msgDetails.setOriginalSender(switchedDetails.getOriginalSender());
        msgDetails.setFinalRecipient(switchedDetails.getFinalRecipient());
        LOGGER.debug("Message Direction attributes are changed to [{}]", msgDetails);
        submitMessage(message);
    }


}
