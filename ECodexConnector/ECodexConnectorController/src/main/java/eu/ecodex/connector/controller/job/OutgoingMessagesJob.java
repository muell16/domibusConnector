package eu.ecodex.connector.controller.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.controller.ECodexConnectorController;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;

public class OutgoingMessagesJob {

    static Logger LOGGER = LoggerFactory.getLogger(OutgoingMessagesJob.class);

    private ECodexConnectorController outgoingController;

    public void setOutgoingController(ECodexConnectorController outgoingController) {
        this.outgoingController = outgoingController;
    }

    public void handleOutgoingMessages() {
        LOGGER.info("Job for handling outgoing messages triggered.");
        Date start = new Date();
        try {
            outgoingController.handleMessages();
        } catch (ECodexConnectorControllerException e) {
            LOGGER.error("Exception while proceeding job handleOutgoingMessages: ", e);
        }
        LOGGER.info("Job for handling outgoing messages finished in {} ms.",
                (start.getTime() - System.currentTimeMillis()));
    }
}
