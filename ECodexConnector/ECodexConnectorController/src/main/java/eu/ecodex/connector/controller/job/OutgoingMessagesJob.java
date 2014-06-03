package eu.ecodex.connector.controller.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.controller.ECodexConnectorController;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;

public class OutgoingMessagesJob {

    static Logger LOGGER = LoggerFactory.getLogger(OutgoingMessagesJob.class);

    private ECodexConnectorController outgoingController;
    private ECodexConnectorMonitor connectorMonitor;

    public void setConnectorMonitor(ECodexConnectorMonitor connectorMonitor) {
        this.connectorMonitor = connectorMonitor;
    }

    public void setOutgoingController(ECodexConnectorController outgoingController) {
        this.outgoingController = outgoingController;
    }

    public void handleOutgoingMessages() {
        LOGGER.debug("Job for handling outgoing messages triggered.");
        Date start = new Date();
        connectorMonitor.setLastCalledOutgoingMessagesPending(start);

        LOGGER.debug("Handling messages....");
        try {
            outgoingController.handleMessages();
        } catch (ECodexConnectorControllerException e) {
            LOGGER.error("Exception while proceeding job handleOutgoingMessages: ", e);
        }
        LOGGER.debug("Handling confirmations....");
        try {
            outgoingController.handleEvidences();
        } catch (ECodexConnectorControllerException e) {
            LOGGER.error("Exception while proceeding job handleOutgoingMessages: ", e);
        }
        LOGGER.debug("Job for handling outgoing messages finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));
    }
}
