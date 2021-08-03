package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Properties;

public class DomibusConnectorMessageLane {

    public static final String DEFAULT_LANE_NAME = "default_message_lane";

    private MessageLaneId id;

    private String description;

    private Properties messageLaneProperties;

    public static DomibusConnectorMessageLane getDefaultMessageLane() {
        DomibusConnectorMessageLane defaultMessageLane = new DomibusConnectorMessageLane();
        defaultMessageLane.setId(new MessageLaneId(DEFAULT_LANE_NAME));
        defaultMessageLane.setDescription("default message lane");
        return defaultMessageLane;
    }

    public static DomibusConnectorMessageLane.MessageLaneId getDefaultMessageLaneId() {
        return getDefaultMessageLane().getId();
    }

    public MessageLaneId getId() {
        return id;
    }

    public void setId(MessageLaneId id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EvidenceActionServiceConfigurationProperties getMessageLaneActionService() {
        return new EvidenceActionServiceConfigurationProperties();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomibusConnectorMessageLane)) return false;

        DomibusConnectorMessageLane that = (DomibusConnectorMessageLane) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public static class MessageLaneId {

        public MessageLaneId() {}

        public MessageLaneId(String id) {
            this.messageLaneId = id;
        }

        private String messageLaneId;

        public String getMessageLaneId() {
            return messageLaneId;
        }

        public void setMessageLaneId(String messageLaneId) {
            this.messageLaneId = messageLaneId;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("messageLaneId", messageLaneId)
                    .toString();
        }
    }

}
