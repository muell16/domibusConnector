package eu.ecodex.connector.common.db.model;

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
@Table(name = "ECODEX_MESSAGE_INFO")
public class ECodexMessageInfo {

    @Id
    @TableGenerator(name = "ecodexSeqStore", table = "ECODEX_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "ECODEX_MESSAGE_INFO.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ecodexSeqStore")
    private Long id;

    @OneToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private ECodexMessage message;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "FROM_PARTY_ID", nullable = true),
            @JoinColumn(name = "FROM_PARTY_ROLE", nullable = true) })
    private ECodexParty from;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "TO_PARTY_ID", nullable = true),
            @JoinColumn(name = "TO_PARTY_ROLE", nullable = true) })
    private ECodexParty to;

    @Column(name = "ORIGINAL_SENDER")
    private String originalSender;

    @Column(name = "FINAL_RECIPIENT")
    private String finalRecipient;

    @ManyToOne
    @JoinColumn(name = "ECDX_SERVICE")
    private ECodexService service;

    @ManyToOne
    @JoinColumn(name = "ECDX_ACTION")
    private ECodexAction action;

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

    public ECodexMessage getMessage() {
        return message;
    }

    public void setMessage(ECodexMessage message) {
        this.message = message;
    }

    public ECodexParty getFrom() {
        return from;
    }

    public void setFrom(ECodexParty from) {
        this.from = from;
    }

    public ECodexParty getTo() {
        return to;
    }

    public void setTo(ECodexParty to) {
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

    public ECodexService getService() {
        return service;
    }

    public void setService(ECodexService service) {
        this.service = service;
    }

    public ECodexAction getAction() {
        return action;
    }

    public void setAction(ECodexAction action) {
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
