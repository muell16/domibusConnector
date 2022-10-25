package eu.ecodex.dc.core.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = DC5Ebms.TABLE_NAME)
public class DC5Ebms {
    public static final String TABLE_NAME = "DC5_EBMS";

    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name = "seq" + TABLE_NAME,
            table = DC5PersistenceSettings.SEQ_STORE_TABLE_NAME,
            pkColumnName = DC5PersistenceSettings.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = DC5PersistenceSettings.SEQ_VALUE_COLUMN_NAME,
            initialValue = DC5PersistenceSettings.INITIAL_VALUE,
            allocationSize = DC5PersistenceSettings.ALLOCATION_SIZE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
    private Long id;

    @OneToOne(targetEntity = DC5TransportRequest.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5TransportRequest transportRequest;

    @OneToMany(mappedBy = "refEbms", cascade = CascadeType.ALL)
    private List<DC5Msg> msgHistory = new ArrayList<>();

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "DC5_CONVERSATION_ID", length = 255)
    private String conversationId;

    @Column(name = "EBMS_MESSAGE_ID", unique = true, length = 255)
    private String ebmsMessageId;

    @Column(name = "BACKEND_MESSAGE_ID", unique = true, length = 255)
    private String backendMessageId;

    @Column(name = "DC5_REF_TO_MESSAGE_ID", length = 255)
    private String refToMessageId;

    @Embedded
    private DC5Action action;

    @Embedded
    private DC5Service service;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "ecxAddress", column = @Column(name = "S_ECX_ADDRESS", length = 255)),
            @AttributeOverride( name = "party.partyId", column = @Column(name = "S_PARTY_Id", length = 255)),
            @AttributeOverride( name = "party.partyIdType", column = @Column(name = "S_PARTY_TYPE", length = 255)),
            @AttributeOverride( name = "role.role", column = @Column(name = "S_ROLE", length = 255)),
            @AttributeOverride( name = "role.roleType", column = @Column(name = "S_ROLE_TYPE", length = 255)),
    })
    private DC5EcxAddress sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "ecxAddress", column = @Column(name = "R_ECX_ADDRESS", length = 255)),
            @AttributeOverride( name = "party.partyId", column = @Column(name = "R_PARTY_Id", length = 255)),
            @AttributeOverride( name = "party.partyIdType", column = @Column(name = "R_PARTY_TYPE", length = 255)),
            @AttributeOverride( name = "role.role", column = @Column(name = "R_ROLE", length = 255)),
            @AttributeOverride( name = "role.roleType", column = @Column(name = "R_ROLE_TYPE", length = 255)),
    })
    private DC5EcxAddress receiver;

    public void addState(DC5Msg historicMsg) {
        this.msgHistory.add(historicMsg);
        historicMsg.setRefEbms(this);
    }

    public void removeHistoricMsg(DC5Msg historicMsg) {
        this.msgHistory.remove(historicMsg);
        historicMsg.setRefEbms(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5Ebms )) return false;
        return id != null && id.equals(((DC5Ebms) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // just getter & setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DC5TransportRequest getTransportRequest() {
        return transportRequest;
    }

    public void setTransportRequest(DC5TransportRequest transportRequest) {
        this.transportRequest = transportRequest;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getEbmsMessageId() {
        return ebmsMessageId;
    }

    public void setEbmsMessageId(String ebmsMessageId) {
        this.ebmsMessageId = ebmsMessageId;
    }

    public String getBackendMessageId() {
        return backendMessageId;
    }

    public void setBackendMessageId(String backendMessageId) {
        this.backendMessageId = backendMessageId;
    }

    public String getRefToMessageId() {
        return refToMessageId;
    }

    public void setRefToMessageId(String refToMessageId) {
        this.refToMessageId = refToMessageId;
    }

    public DC5EcxAddress getSender() {
        return sender;
    }

    public void setSender(DC5EcxAddress sender) {
        this.sender = sender;
    }

    public DC5EcxAddress getReceiver() {
        return receiver;
    }

    public void setReceiver(DC5EcxAddress receiver) {
        this.receiver = receiver;
    }

    public DC5Action getAction() {
        return action;
    }

    public void setAction(DC5Action action) {
        this.action = action;
    }

    public DC5Service getService() {
        return service;
    }

    public void setService(DC5Service service) {
        this.service = service;
    }
}
