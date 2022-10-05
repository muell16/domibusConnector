package eu.dc5.domain.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "DC5Message")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DC5_MESSAGE_TYPE_ID",
        discriminatorType = DiscriminatorType.INTEGER,
        length = 1
)
public abstract class DC5Message implements Serializable {

    private static final String TABLE_NAME = "DC5_Message";
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

    @Column(name = "EBMS_MESSAGE_ID", unique = true, length = 255)
    private String ebmsMessageId;

    @Column(name = "BACKEND_MESSAGE_ID", unique = true, length = 255)
    private String backendMessageId;

    @Column(name = "DC5_BACKEND_LINK", length = 255)
    private String backendLink;
    
    @Column(name = "DC5_GATEWAY_LINK", length = 255)
    private String gwLink;

    @Column(name = "DC5_FROM_PARTY_ID", length = 255)
    private String fromPartyId; // Refactor to EcxAddress?
    @Column(name = "DC5_FROM_PARTY_TYPE", length = 255)
    private String fromPartyType;
    @Column(name = "DC5_FROM_PARTY_ROLE", length = 255)
    private String fromPartyRole;

    @Column(name = "DC5_TO_PARTY_ID", length = 255)
    private String toPartyId;
    @Column(name = "DC5_TO_PARTY_TYPE", length = 255)
    private String toPartyIdType;
    @Column(name = "DC5_TO_PARTY_ROLE", length = 255)
    private String toPartyRole;

    @Column(name = "DC5_FINAL_RECIEPIENT", length = 2048)
    private String finalRecipient;
    @Column(name = "DC5_ORIGINAL_SENDER", length = 2048)
    private String originalSender;

//    private Service service; // TODO: make new ones or import?
//    private Action action;

    @Column(name = "DC5_CONVERSATION_ID", length = 255)
    private String conversationId;

    @Column(name = "DC5_MESSAGE_SOURCE", length = 255)
    private String source;
    @Column(name = "DC5_MESSAGE_SOURCE", length = 255)
    private String target;


    @Column(name = "DC5_REF_TO_MESSAGE_ID", length = 255) // TODO ?
    private String refToMessageId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = TABLE_NAME)
    private Set<DC5Payload> payload;

}
