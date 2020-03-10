package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ConfigurationProperties(prefix = "connector")
public class ConnectorControllerProperties {

    public static final String DEFAULT_MESSAGE_LANE_NAME = "default";

    public ConnectorControllerProperties() {
        this.messageLane.put(DEFAULT_MESSAGE_LANE_NAME, new ConnectorMessageLaneProperties());
    }

    /**
     * a random instance name is the default
     */
    private String instanceName = UUID.randomUUID().toString().substring(0,6);

    private Map<String, ConnectorMessageLaneProperties> messageLane = new HashMap<>();

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Map<String, ConnectorMessageLaneProperties> getMessageLane() {
        return messageLane;
    }

    public void setMessageLane(Map<String, ConnectorMessageLaneProperties> messageLane) {
        this.messageLane = messageLane;
    }

    public ConnectorMessageLaneProperties getMessageLane(String key) {
        return messageLane.get(DEFAULT_MESSAGE_LANE_NAME);
    }
}
