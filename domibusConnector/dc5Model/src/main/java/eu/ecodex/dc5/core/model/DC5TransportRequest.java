package eu.ecodex.dc5.core.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("TRANSPORT_REQUEST_STEP")
@Data
public class DC5TransportRequest {

    @Id
    @GeneratedValue
    public Long id;

    @Column(name = "CONNECTOR_TRANSPORT_ID", length = 255)
    private String connectorTransportId;

    @Column(name = "TRANSPORT_SYSTEM_ID", length = 255)
    private String transportSystemId;

    @Column(name = "REMOTE_MESSAGE_ID", length = 255)
    private String remoteMessageId;

    @Column(name = "UPDATED")
    private LocalDateTime updated;

}
