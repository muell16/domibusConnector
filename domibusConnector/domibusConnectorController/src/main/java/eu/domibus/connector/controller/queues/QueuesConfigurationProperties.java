package eu.domibus.connector.controller.queues;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = QueuesConfigurationProperties.PREFIX)
@Data
public class QueuesConfigurationProperties {

    public static final String PREFIX = "connector.queues";

    private String toConnectorControllerErrorQueue = "toConnectorControllerErrorQueue";
    private String toConnectorControllerQueue = "toConnectorControllerQueue";
    private String toLinkQueue = "submitToLinkQueue";

    private String cleanupQueue = "cleanUpQueue";
    private String deadLetterQueue = "dlq";

}
