package eu.ecodex.connector.controller.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.controller.check.CheckOutgoing;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.controller.jmx.ECodexConnectorJMXMonitor;

public class CheckEvidencesJob {

    static Logger LOGGER = LoggerFactory.getLogger(CheckEvidencesJob.class);

    private CheckOutgoing checkOutgoing;
    private ECodexConnectorJMXMonitor jmxMonitor;

    public void setJmxMonitor(ECodexConnectorJMXMonitor jmxMonitor) {
        this.jmxMonitor = jmxMonitor;
    }

    public void checkEvidences() throws ECodexConnectorControllerException {
        LOGGER.debug("Job for checking evidence timer triggered.");
        Date start = new Date();
        jmxMonitor.setLastCalledEvidenceTimeoutCheck(start);
        checkOutgoing.checkEvidences();

        LOGGER.debug("Job for checking evidence timer finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));
    }

    public void setCheckOutgoing(CheckOutgoing checkOutgoing) {
        this.checkOutgoing = checkOutgoing;
    }

}
