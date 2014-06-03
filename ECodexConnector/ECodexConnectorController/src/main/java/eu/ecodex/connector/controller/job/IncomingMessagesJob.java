package eu.ecodex.connector.controller.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.controller.ECodexConnectorController;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;

public class IncomingMessagesJob {

    static Logger LOGGER = LoggerFactory.getLogger(IncomingMessagesJob.class);

    private ECodexConnectorController incomingController;
    private ECodexConnectorMonitor connectorMonitor;

    public void setConnectorMonitor(ECodexConnectorMonitor connectorMonitor) {
        this.connectorMonitor = connectorMonitor;
    }

    public void setIncomingController(ECodexConnectorController incomingController) {
        this.incomingController = incomingController;
    }

    public void handleIncomingMessages() {
        LOGGER.debug("Job for handling incoming messages triggered.");
        Date start = new Date();
        connectorMonitor.setLastCalledIncomingMessagesPending(start);
        try {
            incomingController.handleMessages();
        } catch (ECodexConnectorControllerException e) {
            LOGGER.error("Exception while proceeding job handleIncomingMessages: ", e);
        }
        LOGGER.debug("Job for handling incoming messages finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));
    }
}
