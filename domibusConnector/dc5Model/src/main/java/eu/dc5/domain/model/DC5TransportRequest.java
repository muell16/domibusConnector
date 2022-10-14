package eu.dc5.domain.model;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("2")
public class DC5TransportRequest extends DC5ProcStep {

    @Column(name = "CONNECTOR_TRANSPORT_ID", length = 255)
    private String connectorTransportId;

    @Column(name = "TRANSPORT_SYSTEM_ID", length = 255)
    private String transportSystemId;

    @Column(name = "REMOTE_MESSAGE_ID", length = 255)
    private String remoteMessageId;

    @Column(name = "ERROR", length = 255)
    private String error;

    @Column(name = "STATE", length = 255)
    private String state;

    @Column(name = "UPDATED")
    private ZonedDateTime updated;


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DC5TransportRequest)) return false;
//        return id != null && id.equals(((DC5TransportRequest) o).getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }

    public String getConnectorTransportId() {
        return connectorTransportId;
    }

    public void setConnectorTransportId(String connectorTransportId) {
        this.connectorTransportId = connectorTransportId;
    }

    public String getTransportSystemId() {
        return transportSystemId;
    }

    public void setTransportSystemId(String transportSystemId) {
        this.transportSystemId = transportSystemId;
    }

    public String getRemoteMessageId() {
        return remoteMessageId;
    }

    public void setRemoteMessageId(String remoteMessageId) {
        this.remoteMessageId = remoteMessageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }
}
