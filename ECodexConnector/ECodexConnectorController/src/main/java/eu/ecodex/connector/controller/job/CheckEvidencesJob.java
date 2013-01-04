package eu.ecodex.connector.controller.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;

public class CheckEvidencesJob {

    static Logger LOGGER = LoggerFactory.getLogger(CheckEvidencesJob.class);

    private ECodexConnectorPersistenceService persistenceService;

    public void checkEvidences() {
        LOGGER.info("Job for checking evidence timer triggered.");
        Date start = new Date();

        LOGGER.info("Job for checking evidence timer finished in {} ms.",
                (start.getTime() - System.currentTimeMillis()));
    }

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}
