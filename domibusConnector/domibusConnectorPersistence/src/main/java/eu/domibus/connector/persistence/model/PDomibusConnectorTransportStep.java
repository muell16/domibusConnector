package eu.domibus.connector.persistence.model;


import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DC_TRANSPORT_STEP")
public class PDomibusConnectorTransportStep {

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqTransportStep", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DC_TRANSPORT_STEP.ID", valueColumnName = "SEQ_VALUE", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqTransportStep")
    private Long id;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ID")
    private PDomibusConnectorMessage message;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "LINK_PARTNER_ID")
    private PDomibusConnectorLinkPartner linkPartner;

    @Column(name = "ATTEMPT")
    private int attempt = 1;

    @Column(name = "TRANSPORT_SYSTEM_MESSAGE_ID")
    private String transportSystemMessageId;

    @Column(name = "REMOTE_MESSAGE_ID")
    private String remoteMessageId;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @OneToMany
    private List<PDomibusConnectorTransportStepStatusUpdate> statusUpdates;

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PDomibusConnectorMessage getMessage() {
        return message;
    }

    public void setMessage(PDomibusConnectorMessage message) {
        this.message = message;
    }

    public PDomibusConnectorLinkPartner getLinkPartner() {
        return linkPartner;
    }

    public void setLinkPartner(PDomibusConnectorLinkPartner linkPartner) {
        this.linkPartner = linkPartner;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public String getTransportSystemMessageId() {
        return transportSystemMessageId;
    }

    public void setTransportSystemMessageId(String transportSystemMessageId) {
        this.transportSystemMessageId = transportSystemMessageId;
    }

    public String getRemoteMessageId() {
        return remoteMessageId;
    }

    public void setRemoteMessageId(String remoteMessageId) {
        this.remoteMessageId = remoteMessageId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<PDomibusConnectorTransportStepStatusUpdate> getStatusUpdates() {
        return statusUpdates;
    }

    public void setStatusUpdates(List<PDomibusConnectorTransportStepStatusUpdate> statusUpdates) {
        this.statusUpdates = statusUpdates;
    }
}
