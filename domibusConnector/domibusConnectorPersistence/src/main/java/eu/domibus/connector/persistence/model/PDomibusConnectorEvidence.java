package eu.domibus.connector.persistence.model;

import eu.domibus.connector.persistence.model.enums.EvidenceType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;



@Entity
@Table(name = "DOMIBUS_CONNECTOR_EVIDENCE")
public class PDomibusConnectorEvidence {

    @Id
    @TableGenerator(name = "evidencesSeqStore", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_EVIDENCE.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "evidencesSeqStore")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private PDomibusConnectorMessage message;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private EvidenceType type;

    @Column(name = "EVIDENCE")
    private String evidence;

    @Column(name = "DELIVERED_NAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredToNationalSystem;

    @Column(name = "DELIVERED_GW")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredToGateway;

    @Column(name = "UPDATED", nullable = false)
    private Date updated;

    @PrePersist
    public void prePersist() {
        updated = new Date();
    }
    
    @PreUpdate
    public void preUpdate() {
        updated = new Date();
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

        if (null != this.message.getEvidences() && !this.message.getEvidences().contains(this)) {
            this.message.getEvidences().add(this);
        }
    }

    public EvidenceType getType() {
        return type;
    }

    public void setType(EvidenceType type) {
        this.type = type;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public Date getDeliveredToNationalSystem() {
        return deliveredToNationalSystem;
    }

    public void setDeliveredToNationalSystem(Date deliveredToNationalSystem) {
        this.deliveredToNationalSystem = deliveredToNationalSystem;
    }

    public Date getDeliveredToGateway() {
        return deliveredToGateway;
    }

    public void setDeliveredToGateway(Date deliveredToGateway) {
        this.deliveredToGateway = deliveredToGateway;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", this.id);
        toString.append("evidence", this.evidence);
        toString.append("deliveredToGw", this.deliveredToGateway);
        toString.append("deliveredToNationalSystem", this.deliveredToNationalSystem);
        return toString.build();
    }
}
