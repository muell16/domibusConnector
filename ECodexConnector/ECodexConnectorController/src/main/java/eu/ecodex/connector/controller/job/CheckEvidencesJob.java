package eu.ecodex.connector.controller.job;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.message.Message;

public class CheckEvidencesJob {

    static Logger LOGGER = LoggerFactory.getLogger(CheckEvidencesJob.class);

    private ECodexConnectorPersistenceService persistenceService;
    private ECodexConnectorProperties connectorProperties;

    public void checkEvidences() {
        LOGGER.info("Job for checking evidence timer triggered.");
        Date start = new Date();

        List<Message> unconfirmedOutgoing = persistenceService.findOutgoingUnconfirmedMessages();
        if (unconfirmedOutgoing != null && !unconfirmedOutgoing.isEmpty()) {
            for (Message unconfirmed : unconfirmedOutgoing) {
                Date delivered = unconfirmed.getDbMessage().getDeliveredToGateway();
                Date now = new Date();
                long deliveredTimout = delivered.getTime() + connectorProperties.getTimeoutRelayREMMD();
                if (now.getTime() > deliveredTimout) {
                    LOGGER.info("The RelayREMMDAcceptance evidence for message {} timed out. Will send rejection!",
                            unconfirmed.getMessageDetails().getEbmsMessageId());

                }
            }
        }

        LOGGER.info("Job for checking evidence timer finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));
    }

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

}
