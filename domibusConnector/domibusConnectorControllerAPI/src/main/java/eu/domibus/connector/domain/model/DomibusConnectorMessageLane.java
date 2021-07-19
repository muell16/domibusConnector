package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;

import javax.validation.constraints.NotBlank;
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

    public Properties getMessageLaneProperties() {
        return messageLaneProperties;
    }

    public void setMessageLaneProperties(Properties messageLaneProperties) {
        this.messageLaneProperties = messageLaneProperties;
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

        @NotBlank
        private String messageLaneId;

        public String getMessageLaneId() {
            return messageLaneId;
        }

        public void setMessageLaneId(String messageLaneId) {
            this.messageLaneId = messageLaneId;
        }

        @Override
        public String toString() {
            return String.format("MessageLaneId: [%s]", this.messageLaneId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MessageLaneId)) return false;

            MessageLaneId that = (MessageLaneId) o;

            return messageLaneId != null ? messageLaneId.equals(that.messageLaneId) : that.messageLaneId == null;
        }

        @Override
        public int hashCode() {
            return messageLaneId != null ? messageLaneId.hashCode() : 0;
        }
    }

}
