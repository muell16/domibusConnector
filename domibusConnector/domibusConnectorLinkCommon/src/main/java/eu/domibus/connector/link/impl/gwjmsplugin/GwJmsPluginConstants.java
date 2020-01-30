package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.domain.transition.DomibusConnectorConfirmationType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class GwJmsPluginConstants {

    public static final String MESSAGE_TYPE_PROPERTY_NAME = "messageType";

    public static final String MESSAGE_TYPE_VALUE_SUBMIT_MESSAGE = "submitMessage";
    public static final String MESSAGE_TYPE_VALUE_SUBMIT_RESPONSE = "submitResponse";
    public static final String MESSAGE_TYPE_VALUE_MESSAGE_SENT = "messageSent";
    public static final String MESSAGE_TYPE_VALUE_MESSAGE_SENT_FAILURE = "messageSendFailure";
    public static final String MESSAGE_TYPE_VALUE_INCOMING_MESSAGE = "incomingMessage";
    public static final String MESSAGE_TYPE_VLAUE_MESSAGE_RECEIVE_FAILURE = "messageReceiveFailure";

    public static final String PROTOCOL_PROPERTY_NAME = "protocol";
    public static final String PROTOCOL_VALUE_DEFAULT = "AS4";

    public static final String EBMS_MESSAGE_ID_PROPERTY_NAME = "messageId";
    public static final String EBMS_ACTION_PROPERTY_NAME = "action";
    public static final String EBMS_SERVICE_PROPERTY_NAME = "service";
    public static final String EBMS_SERVICE_TYPE_PROPERTY_NAME = "serviceType";
    public static final String EBMS_CONVERSATION_ID_PROPERTY_NAME = "conversationId";
    public static final String EBMS_FROM_PARTY_ID_PROPERTY_NAME = "fromPartyId";
    public static final String EBMS_FROM_PARTY_ID_TYPE_PROPERTY_NAME = "fromPartyIdType";
    public static final String EBMS_FROM_PARTY_ROLE_PROPERTY_NAME = "fromRole";
    public static final String EBMS_TO_PARTY_ID_PROPERTY_NAME = "toPartyId";
    public static final String EBMS_TO_PARTY_ID_TYPE_PROPERTY_NAME = "toPartyIdType";
    public static final String EBMS_TO_PARTY_ROLE_PROPERTY_NAME = "toRole";
    public static final String EBMS_ORIGINAL_SENDER_PROPERTY_NAME = "originalSender";
    public static final String EBMS_FINAL_RECIPIENT_PROPERTY_NAME = "finalRecipient";
    public static final String EBMS_REF_TO_MESSAGE_ID_PROPERTY_NAME = "refToMessageId";
    public static final String EBMS_AGREEMENT_REF_PROPERTY_NAME = "agreementRef";

    public static final String TOTAL_NUMBER_OF_PAYLOADS = "totalNumberOfPayloads";
    public static final String ERROR_DETAIL_PROPERTY_NAME = "errorDetail";

    public static final String SERVICE_TYPE_DEFAULT_VALUE = "";

    public static final String PROPERTY_PREFIX = "property_";
    public static final String PROPERTY_TYPE_PREFIX = "propertyType_";
    private static final String PAYLOAD_NAME_PREFIX = "payload_";
    public static final String PAYLOAD_NAME_FORMAT = PAYLOAD_NAME_PREFIX + "{0}";
    private static final String PAYLOAD_MIME_TYPE_SUFFIX = "_mimeType";
    public static final String PAYLOAD_MIME_TYPE_FORMAT = PAYLOAD_NAME_FORMAT + PAYLOAD_MIME_TYPE_SUFFIX;
    private static final String PAYLOAD_FILE_NAME_SUFFIX = "_fileName";
    public static final String PAYLOAD_FILE_NAME_FORMAT = PAYLOAD_NAME_FORMAT + PAYLOAD_FILE_NAME_SUFFIX;
    private static final String PAYLOAD_MIME_CONTENT_ID_SUFFIX = "_mimeContentId";
    public static final String PAYLOAD_MIME_CONTENT_ID_FORMAT = PAYLOAD_NAME_FORMAT + PAYLOAD_MIME_CONTENT_ID_SUFFIX;
    private static final String PAYLOAD_MIME_DESCRIPTION_SUFFIX = "_description";
    public static final String PAYLOAD_DESCRIPTION_ID_FORMAT = PAYLOAD_NAME_FORMAT + PAYLOAD_MIME_CONTENT_ID_SUFFIX;

    public static final String PAYLOAD_FILENAME = "FileName";

    public static final Set<String> EVIDENCE_TYPE_NAMES = (Arrays.stream(DomibusConnectorConfirmationType.values()).map(t -> t.value()).collect(Collectors.toSet()));

    public static final String ASIC_S_DESCRIPTION_NAME = "ASIC-S";
    public static final String TOKEN_XML_DESCRIPTION_NAME = "tokenXML";
    public static final String MESSAGE_CONTENT_DESCRIPTION_NAME = "messageContent";

    public static final String ASIC_S_MIMETYPE = "application/vnd.etsi.asic-s+zip";


}
