package eu.domibus.connector.controller.queues;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = QueuesConfigurationProperties.PREFIX)
public class QueuesConfigurationProperties {

    public static final String PREFIX = "connector.queues";
    // must match up with queuePrefix attribute of <individualDeadLetterStrategy> configured in activemq.xml
    public static final String DLQ_PREFIX = "DLQ.";

    private String toConnectorControllerQueue = "toConnectorControllerQueue";
    private String toConnectorControllerDeadLetterQueue = DLQ_PREFIX + toConnectorControllerQueue;

    private String toLinkQueue = "submitToLinkQueue";
    private String toLinkDeadLetterQueue = DLQ_PREFIX + toLinkQueue;

    private String cleanupQueue = "cleanUpQueue";
    private String cleanupDeadLetterQueue = DLQ_PREFIX + cleanupQueue;

    public String getToConnectorControllerQueue() {
        return toConnectorControllerQueue;
    }

    public void setToConnectorControllerQueue(String toConnectorControllerQueue) {
        this.toConnectorControllerQueue = toConnectorControllerQueue;
    }

    public String getToConnectorControllerDeadLetterQueue() {
        return toConnectorControllerDeadLetterQueue;
    }

    public void setToConnectorControllerDeadLetterQueue(String toConnectorControllerDeadLetterQueue) {
        this.toConnectorControllerDeadLetterQueue = toConnectorControllerDeadLetterQueue;
    }

    public String getToLinkQueue() {
        return toLinkQueue;
    }

    public void setToLinkQueue(String toLinkQueue) {
        this.toLinkQueue = toLinkQueue;
    }

    public String getToLinkDeadLetterQueue() {
        return toLinkDeadLetterQueue;
    }

    public void setToLinkDeadLetterQueue(String toLinkDeadLetterQueue) {
        this.toLinkDeadLetterQueue = toLinkDeadLetterQueue;
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

    public void setCleanupDeadLetterQueue(String cleanupDeadLetterQueue) {
        this.cleanupDeadLetterQueue = cleanupDeadLetterQueue;
    }
}
