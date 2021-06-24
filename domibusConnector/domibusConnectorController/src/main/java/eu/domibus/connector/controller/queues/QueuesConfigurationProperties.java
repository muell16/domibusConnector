package eu.domibus.connector.controller.queues;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = QueuesConfigurationProperties.PREFIX)
public class QueuesConfigurationProperties {

    public static final String PREFIX = "connector.queues";

    private String toConnectorControllerErrorQueue = "toConnectorControllerErrorQueue";
    private String toConnectorControllerQueue = "toConnectorControllerQueue";
    private String toLinkQueue = "submitToLinkQueue";
    private String toLinkErrorQueue = "submitToLinkErrorQueue";

    private String cleanupQueue = "cleanUpQueue";
    private String cleanupDeadLetterQueue = "DLQ.cleanUpDeadLetterQueue";

    public String getToConnectorControllerErrorQueue() {
        return toConnectorControllerErrorQueue;
    }

    public void setToConnectorControllerErrorQueue(String toConnectorControllerErrorQueue) {
        this.toConnectorControllerErrorQueue = toConnectorControllerErrorQueue;
    }

    public String getToConnectorControllerQueue() {
        return toConnectorControllerQueue;
    }

    public void setToConnectorControllerQueue(String toConnectorControllerQueue) {
        this.toConnectorControllerQueue = toConnectorControllerQueue;
    }

    public String getToLinkQueue() {
        return toLinkQueue;
    }

    public void setToLinkQueue(String toLinkQueue) {
        this.toLinkQueue = toLinkQueue;
    }

    public String getToLinkErrorQueue() {
        return toLinkErrorQueue;
    }

    public void setToLinkErrorQueue(String toLinkErrorQueue) {
        this.toLinkErrorQueue = toLinkErrorQueue;
    }

    public String getCleanupQueue() {
        return cleanupQueue;
    }

    public void setCleanupQueue(String cleanupQueue) {
        this.cleanupQueue = cleanupQueue;
    }

    public String getCleanupDeadLetterQueue() {
        return cleanupDeadLetterQueue;
    }

    public void setCleanupDeadLetterQueue(String cleanupErrorQueue) {
        this.cleanupDeadLetterQueue = cleanupErrorQueue;
    }

}
