package eu.domibus.connector.common.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_MESSAGE_INFO")
public class DomibusConnectorMessageInfo {

    @Id
    @TableGenerator(name = "seqStore", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MESSAGE_INFO.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStore")
    private Long id;

    @OneToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private DomibusConnectorMessage message;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "FROM_PARTY_ID", nullable = true),
            @JoinColumn(name = "FROM_PARTY_ROLE", nullable = true) })
    private DomibusConnectorParty from;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "TO_PARTY_ID", nullable = true),
            @JoinColumn(name = "TO_PARTY_ROLE", nullable = true) })
    private DomibusConnectorParty to;

    @Column(name = "ORIGINAL_SENDER")
    private String originalSender;

    @Column(name = "FINAL_RECIPIENT")
    private String finalRecipient;

    @ManyToOne
    @JoinColumn(name = "SERVICE")
    private DomibusConnectorService service;

    @ManyToOne
    @JoinColumn(name = "ACTION")
    private DomibusConnectorAction action;

    @Column(name = "CREATED", nullable = false)
    private Date created;

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
    }

    public DomibusConnectorParty getFrom() {
        return from;
    }

    public void setFrom(DomibusConnectorParty from) {
        this.from = from;
    }

    public DomibusConnectorParty getTo() {
        return to;
    }

    public void setTo(DomibusConnectorParty to) {
        this.to = to;
    }

    public String getOriginalSender() {
        return originalSender;
    }

    public void setOriginalSender(String originalSender) {
        this.originalSender = originalSender;
    }

    public String getFinalRecipient() {
        return finalRecipient;
    }

    public void setFinalRecipient(String finalRecipient) {
        this.finalRecipient = finalRecipient;
    }

    public DomibusConnectorService getService() {
        return service;
    }

    public void setService(DomibusConnectorService service) {
        this.service = service;
    }

    public DomibusConnectorAction getAction() {
        return action;
    }

    public void setAction(DomibusConnectorAction action) {
        this.action = action;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
