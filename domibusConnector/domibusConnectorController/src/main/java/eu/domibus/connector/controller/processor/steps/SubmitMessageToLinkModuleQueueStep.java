package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.queues.producer.ToLinkQueue;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Ebms;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SubmitMessageToLinkModuleQueueStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(SubmitMessageToLinkModuleQueueStep.class);

    private final ToLinkQueue toLinkQueue;

    public SubmitMessageToLinkModuleQueueStep(ToLinkQueue toLinkQueue) {
        this.toLinkQueue = toLinkQueue;
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "SubmitMessageToLinkModuleQueueStep")
    public boolean executeStep(DC5Message DC5Message) {
        submitMessage(DC5Message);
        return true;
    }

    public void submitMessage(DC5Message message) {
        toLinkQueue.putOnQueue(message);
    }

    /**
     * Submits message to ConnectorToLinkQueue,
     * but before this is done, the following message details are overwritten with
     * the switched originalMessageDetails see {@link DomainModelHelper#switchMessageDirection(DC5Ebms)}
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
    public void submitMessageOpposite(DC5Message originalMessage, DC5Message message) {
        DC5Ebms switchedDetails = DomainModelHelper.switchMessageDirection(originalMessage.getEbmsData());
        DC5Ebms msgDetails = message.getEbmsData();

//        msgDetails.setDirection(switchedDetails.getDirection());
//        msgDetails.setFromParty(switchedDetails.getFromParty());
//        msgDetails.setToParty(switchedDetails.getToParty());
//        msgDetails.setOriginalSender(switchedDetails.getOriginalSender());
//        msgDetails.setFinalRecipient(switchedDetails.getFinalRecipient());
        //TODO: repair!!!
        LOGGER.debug("Message Direction attributes are changed to [{}]", msgDetails);
        submitMessage(message);
    }


}
