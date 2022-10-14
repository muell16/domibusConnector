package eu.dc5.domain.model;

import javax.persistence.*;
import java.time.ZonedDateTime;

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

    // TODO:
//    private List<DC5Msg> msgHistory;

    // TODO:
    private ZonedDateTime created;

    @Column(name = "DC5_CONVERSATION_ID", length = 255)
    private String conversationId;

    @Column(name = "EBMS_MESSAGE_ID", unique = true, length = 255)
    private String ebmsMessageId;

    @Column(name = "BACKEND_MESSAGE_ID", unique = true, length = 255)
    private String backendMessageId;

    @Column(name = "DC5_REF_TO_MESSAGE_ID", length = 255)
    private String refToMessageId;

    // TODO: action, service, getter, setter

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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
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
}
