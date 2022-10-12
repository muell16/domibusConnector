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
}
