package eu.ecodex.connector.common.db;

import java.util.Date;

import eu.ecodex.connector.common.MessageState;

public class Message {

    // @Id
    // @GeneratedValue(generator = "ECMSL_SEQ_GEN", strategy =
    // GenerationType.SEQUENCE)
    private Long id;

    // @Column(name = "EBMS_MESSAGE_ID")
    private String ebmsMessageId;

    // @Column(name = "NAT_MESSAGE_ID")
    private String nationalMessageId;

    // @Column(name = "CONVERSATION_ID")
    private String conversationId;

    // @Column(name = "HASH_VALUE")
    private String hashValue;

    // @Column(name = "MESSAGE_STATE")
    // @Enumerated(EnumType.STRING)
    private MessageState messageState;

    // @Column(name = "UPDATED")
    private Date updated;
}
