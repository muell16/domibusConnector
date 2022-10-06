package eu.dc5.domain.model;

import javax.persistence.*;
import java.time.ZonedDateTime;

//@Embeddable // ???
@Entity(name = DC5EbmsData.TABLE_NAME)
public class DC5EbmsData {
    public static final String TABLE_NAME = "DC5_EBMS_DATA";

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

    // TODO:
    private ZonedDateTime created;

    @Column(name = "EBMS_MESSAGE_ID", unique = true, length = 255)
    private String ebmsMessageId;

    @Column(name = "BACKEND_MESSAGE_ID", unique = true, length = 255)
    private String backendMessageId;

    @Column(name = "DC5_CONVERSATION_ID", length = 255)
    private String conversationId;

    @Column(name = "DC5_REF_TO_MESSAGE_ID", length = 255) // TODO ?
    private String refToMessageId;


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
