package eu.domibus.connector.common.db.model;

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

import eu.domibus.connector.common.enums.EvidenceType;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_EVIDENCES")
public class DomibusConnectorEvidence {

    @Id
    @TableGenerator(name = "seqStore", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_EVIDENCES.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStore")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private DomibusConnectorMessage message;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private EvidenceType type;

    @Column(name = "EVIDENCE")
    private String evidence;

    @Column(name = "DELIVERED_NAT")
    private Date deliveredToNationalSystem;

    @Column(name = "DELIVERED_GW")
    private Date deliveredToGateway;

    @Column(name = "UPDATED", nullable = false)
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomibusConnectorMessage getMessage() {
        return message;
    }

    public void setMessage(DomibusConnectorMessage message) {
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
}
