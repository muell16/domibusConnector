package eu.ecodex.dc5.message.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = DC5Ebms.TABLE_NAME)
@Data
public class DC5Ebms {

    public static final String TABLE_NAME = "DC5_EBMS";

    private Long id;

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


}
