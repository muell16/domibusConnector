package eu.domibus.connector.domain.model;

public class DomibusConnectorMessageId {
    String connectorMessageId;

    public DomibusConnectorMessageId() {
    }

    public DomibusConnectorMessageId(String connectorMessageId) {
        this.connectorMessageId = connectorMessageId;
    }

    public String getConnectorMessageId() {
        return connectorMessageId;
    }

    public void setConnectorMessageId(String connectorMessageId) {
        this.connectorMessageId = connectorMessageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomibusConnectorMessageId)) return false;

        DomibusConnectorMessageId that = (DomibusConnectorMessageId) o;

        return connectorMessageId != null ? connectorMessageId.equals(that.connectorMessageId) : that.connectorMessageId == null;
    }

    @Override
    public int hashCode() {
        return connectorMessageId != null ? connectorMessageId.hashCode() : 0;
    }

    public String toString() {
        return this.connectorMessageId;
    }
}
