package eu.dc5.domain.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public abstract class DC5Message implements Serializable {

    private Long id;
    private String ebmsMessageId;
    private String backendMessageId;
    private String backendLink;
    private String gwLink;

    private String fromPartyId; // Refactor to EcxAddress?
    private String fromPartyType;
    private String fromPartyRole;
    private String toPartyId;
    private String toPartyIdType;
    private String toPartyRole;
    private String finalRecipient;
    private String originalSender;

//    private Service service; // TODO: make new ones or import?
//    private Action action;

    private String conversationId;
    private String source;
    private String target;
    private String refToMessageId;
    private List<DC5Payload> payload;
}
