package eu.ecodex.connector.controller.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.controller.ECodexConnectorController;

public class IncomingMessagesJob {

    static Logger LOGGER = LoggerFactory.getLogger(IncomingMessagesJob.class);

    private ECodexConnectorController incomingController;

    public void setIncomingController(ECodexConnectorController incomingController) {
        this.incomingController = incomingController;
    }

    public void handleIncomingMessages() {
        LOGGER.info("Job for handling incoming messages triggered.");
        Date start = new Date();
        // try {
        // incomingController.handleMessages();
        // } catch (ECodexConnectorControllerException e) {
        // LOGGER.error("Exception while proceeding job handleIncomingMessages: ",
        // e);
        // }
        LOGGER.info("Job for handling incoming messages finished in {} ms.",
                (start.getTime() - System.currentTimeMillis()));
    }
}
