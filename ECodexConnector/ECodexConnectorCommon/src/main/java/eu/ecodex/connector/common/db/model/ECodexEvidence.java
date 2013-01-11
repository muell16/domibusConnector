package eu.ecodex.connector.common.db.model;

import java.sql.Clob;
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

import eu.ecodex.connector.common.enums.ECodexEvidenceType;

@Entity
@Table(name = "ECODEX_EVIDENCES")
public class ECodexEvidence {

    @Id
    @TableGenerator(name = "ecodexSeqStore", table = "ECODEX_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "ECODEX_EVIDENCES.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ecodexSeqStore")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private ECodexMessage message;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ECodexEvidenceType type;

    @Column(name = "EVIDENCE")
    private Clob evidence;

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

    public ECodexMessage getMessage() {
        return message;
    }

    public void setMessage(ECodexMessage message) {
        this.message = message;

        if (null != this.message.getEvidences() && !this.message.getEvidences().contains(this)) {
            this.message.getEvidences().add(this);
        }
    }

    public ECodexEvidenceType getType() {
        return type;
    }

    public void setType(ECodexEvidenceType type) {
        this.type = type;
    }

    public Clob getEvidence() {
        return evidence;
    }

    public void setEvidence(Clob evidence) {
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
