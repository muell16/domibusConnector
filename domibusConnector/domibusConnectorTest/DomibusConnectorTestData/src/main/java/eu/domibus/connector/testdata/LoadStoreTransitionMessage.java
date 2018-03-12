package eu.domibus.connector.testdata;


import eu.domibus.connector.domain.transition.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;

import java.util.Properties;

public class LoadStoreTransitionMessage {

    public static final String SERVICE_PROP_NAME = "service";
    public static final String NATIONAL_ID_PROP_NAME = "message.nation-id";
    public static final String EBMS_ID_PROP_NAME = "message.ebms-id";
    public static final String BACKEND_CLIENT_NAME_PROP_NAME = "message.backend-client-name";
    public static final String MESSAGE_CONTENT_XML_PROP_NAME = "message.content.xml";
    public static final String DEFAULT_CONTENT_XML_FILE_NAME = "content.xml";
    public static final String MESSAGE_DOCUMENT_FILE_PROP_NAME = "message.content.document.file";
    public static final String MESSAGE_DOCUMENT_NAME_PROP_NAME = "message.content.document.name";
    public static final String MESSAGE_DOCUMENT_HASH_PROP_NAME = "message.content.document.hash";
    public static final String MESSAGE_DOCUMENT_SIGNATURE_FILE_PROP_NAME = "message.content.document.signature.file";
    public static final String MESSAGE_DOCUMENT_SIGNATURE_TYPE_PROP_NAME = "message.content.document.signature.type";
    public static final String MESSAGE_DOCUMENT_SIGNATURE_NAME_PROP_NAME = "message.content.document.signature.name";
    public static final String FROM_PARTY_ID_PROP_NAME = "from.party.id";
    public static final String FROM_PARTY_ROLE_PROP_NAME = "from.party.role";
    public static final String TO_PARTY_ID_PROP_NAME = "to.party.id";
    public static final String TO_PARTY_ROLE_PROP_NAME = "to.party.role";
    public static final String ACTION_PROP_NAME = "action";
    public static final String CONVERSATION_ID_PROP_NAME = "message.conversation-id";


    private static final Logger LOGGER = LoggerFactory.getLogger(LoadStoreTransitionMessage.class);
    public static String MESSAGE_CONFIRMATIONS_PREFIX = "message.confirmation";
    public static String MESSAGE_ATTACHMENT_PREFIX = "message.attachment";

    private Resource basicFolder;

    private Properties messageProperties;

    public static DomibusConnectorMessageType loadMessageFrom(Resource path) {
        LoadStoreTransitionMessage load = new LoadStoreTransitionMessage(path);
        return load.loadMessage();
    }

    public void storeMessageTo(String path) {
        //TODO: implement!
    }

    private LoadStoreTransitionMessage() {
    }

    private LoadStoreTransitionMessage(Resource basicFolder) {
        this.basicFolder = basicFolder;
    }

    private DomibusConnectorMessageType loadMessage() {
        DomibusConnectorMessageType message = new DomibusConnectorMessageType();

        message.setMessageDetails(loadMessageDetails());

        //TODO: load content


        return message;
    }

    private DomibusConnectorMessageDetailsType loadMessageDetails() {
        DomibusConnectorMessageDetailsType messageDetails = new DomibusConnectorMessageDetailsType();

        DomibusConnectorActionType domibusConnectorActionType = new DomibusConnectorActionType();
        domibusConnectorActionType.setAction(messageProperties.getProperty("action"));
        messageDetails.setAction(domibusConnectorActionType);

        DomibusConnectorPartyType fromParty = new DomibusConnectorPartyType();
        fromParty.setPartyId(messageProperties.getProperty("from.party.id"));
        fromParty.setRole(messageProperties.getProperty("from.party.role"));
        messageDetails.setFromParty(fromParty);


        DomibusConnectorPartyType toParty = new DomibusConnectorPartyType();
        toParty.setPartyId(messageProperties.getProperty("to.party.id"));
        toParty.setRole(messageProperties.getProperty("to.party.role"));
        messageDetails.setToParty(toParty);


        DomibusConnectorServiceType service = new DomibusConnectorServiceType();
        service.setService(messageProperties.getProperty("service"));
        messageDetails.setService(service);

        messageDetails.setConversationId(messageProperties.getProperty("message.conversation-id"));

        messageDetails.setEbmsMessageId(messageProperties.getProperty("message.ebms-id"));

        messageDetails.setBackendMessageId(messageProperties.getProperty("message.national-id"));

        return messageDetails;
    }






}
