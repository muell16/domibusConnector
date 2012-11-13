package eu.ecodex.connector.gwc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.AgreementRef;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import backend.ecodex.org.BackendInterface;
import backend.ecodex.org.DownloadMessageFault;
import backend.ecodex.org.DownloadMessageRequest;
import backend.ecodex.org.DownloadMessageResponse;
import backend.ecodex.org.ListPendingMessagesFault;
import backend.ecodex.org.ListPendingMessagesResponse;
import backend.ecodex.org.SendMessageFault;
import backend.ecodex.org.SendRequest;
import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.enums.ServiceEnum;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class ECodexConnectorGatewayWebserviceClientImpl implements ECodexConnectorGatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorGatewayWebserviceClientImpl.class);

    private static final String AGREEMENT_REF = "Agr123";
    private static final String PMODE = "EE_Form_A";

    private BackendInterface gatewayBackendWebservice;
    private ECodexConnectorProperties connectorProperties;

    public void setGatewayBackendWebservice(BackendInterface gatewayBackendWebservice) {
        this.gatewayBackendWebservice = gatewayBackendWebservice;
    }

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    @Override
    public void sendMessageWithReference(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(Message message) throws ECodexConnectorGatewayWebserviceClientException {
        SendRequest request = null;
        try {
            request = buildSendRequest(message);
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw e;
        } catch (IOException e1) {
            LOGGER.error("Could not build sendRequest! ", e1);
            throw new ECodexConnectorGatewayWebserviceClientException(e1);
        }

        Messaging ebMSHeaderInfo = new Messaging();

        UserMessage userMessage = new UserMessage();

        userMessage.setPartyInfo(buildPartyInfo(message.getMessageDetails()));

        userMessage.setCollaborationInfo(buildCollaborationInfo(message.getMessageDetails()));

        MessageInfo info = new MessageInfo();

        info.setRefToMessageId(message.getMessageDetails().getRefToMessageId());

        userMessage.setMessageInfo(info);

        ebMSHeaderInfo.getUserMessage().add(userMessage);

        try {
            gatewayBackendWebservice.sendMessage(request, ebMSHeaderInfo);
        } catch (SendMessageFault e) {
            LOGGER.error("sendMessage failed: ", e);
            throw new ECodexConnectorGatewayWebserviceClientException(e);
        }
    }

    @Override
    public String[] listPendingMessages() {
        LOGGER.debug("started... ");
        try {
            ListPendingMessagesResponse response = gatewayBackendWebservice
                    .listPendingMessages(createEmptyListPendingMessagesRequest());

            LOGGER.debug(response.getMessageID().toString());
            return response.getMessageID().toArray(new String[response.getMessageID().size()]);
        } catch (ListPendingMessagesFault e) {
            LOGGER.error("Could not execute! ", e);
        }
        return null;

    }

    @Override
    public Message downloadMessage(String messageId) throws ECodexConnectorGatewayWebserviceClientException {

        Holder<DownloadMessageResponse> response = new Holder<DownloadMessageResponse>();
        Holder<Messaging> ebMSHeader = new Holder<Messaging>();

        DownloadMessageRequest request = new DownloadMessageRequest();
        request.setMessageID(messageId);

        try {
            gatewayBackendWebservice.downloadMessage(request, response, ebMSHeader);
            LOGGER.debug("Successfully downloaded message with id [{}]", request.getMessageID());
        } catch (DownloadMessageFault e) {
            LOGGER.error("Could not execute! ", e);
        }

        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);
        MessageDetails details = convertUserMessageToMessageDetails(userMessage);

        // TODO: Woher weiss ich, welcher Payload der content ist? Gibt es
        // referenzen aus der payloadInfo in der UserMessage? Derzeitige
        // Annahme: Der erste Payload ist der XML Content! Außerdem: Wie
        // identifiziere ich einen Payload als Evidence?

        if (response.value == null || response.value.getPayload() == null || response.value.getPayload().isEmpty()) {
            LOGGER.info("Message {} contains no payload!", request.getMessageID());
            throw new ECodexConnectorGatewayWebserviceClientException("Message " + request.getMessageID()
                    + " contains no payload!");
        }

        List<DataHandler> payload = response.value.getPayload();
        MessageContent content = extractMessageContent(payload);

        Message message = new Message(details, content);

        if (payload.size() > 1) {
            MessageAttachment[] attachments = extractAttachments(payload);
            message.setAttachments(attachments);
        }

        return message;
    }

    private MessageContent extractMessageContent(List<DataHandler> payload) {
        DataHandler dh = payload.get(0);
        MessageContent content = new MessageContent();
        try {
            content.setXmlContent(convertDataHandlerContentToByteArray(dh));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private MessageAttachment[] extractAttachments(List<DataHandler> payload) {
        MessageAttachment[] attachments = new MessageAttachment[payload.size() - 1];
        for (int counter = 1; counter < payload.size(); counter++) {
            DataHandler dh = payload.get(counter);
            try {
                ByteArrayInputStream is = (ByteArrayInputStream) dh.getContent();
                byte[] b = new byte[is.available()];
                is.read(b);

                MessageAttachment attachment = new MessageAttachment();
                attachment.setAttachment(b);
                attachment.setMimeType(dh.getContentType());
                attachments[counter] = attachment;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return attachments;
    }

    private byte[] convertDataHandlerContentToByteArray(DataHandler dh) throws IOException {
        ByteArrayInputStream is = (ByteArrayInputStream) dh.getContent();
        byte[] b = new byte[is.available()];
        is.read(b);
        return b;
    }

    private MessageDetails convertUserMessageToMessageDetails(UserMessage userMessage) {
        MessageDetails details = new MessageDetails();

        details.setEbmsMessageId(userMessage.getMessageInfo().getMessageId());
        details.setRefToMessageId(userMessage.getMessageInfo().getRefToMessageId());
        details.setConversationId(userMessage.getCollaborationInfo().getConversationId());

        String actionString = userMessage.getCollaborationInfo().getAction();
        try {
            ActionEnum action = ActionEnum.valueOf(actionString);
            details.setAction(action);
        } catch (IllegalArgumentException e) {
            LOGGER.error("No action {} found!", actionString);
        }

        String serviceString = userMessage.getCollaborationInfo().getService().getValue();
        try {
            ServiceEnum service = ServiceEnum.valueOf(serviceString);
            details.setService(service);
        } catch (IllegalArgumentException e) {
            LOGGER.error("No service {} found!", actionString);
        }

        From from = userMessage.getPartyInfo().getFrom();
        PartnerEnum fromPartner = PartnerEnum.findValue(from.getPartyId().get(0).getValue(), from.getRole());
        details.setFromPartner(fromPartner);

        To to = userMessage.getPartyInfo().getTo();
        PartnerEnum toPartner = PartnerEnum.findValue(to.getPartyId().get(0).getValue(), to.getRole());
        details.setToPartner(toPartner);

        return details;
    }

    private Element createEmptyListPendingMessagesRequest() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        Document document = db.newDocument();
        Element request = document.createElement("listPendingMessagesRequest");

        return request;
    }

    private SendRequest buildSendRequest(Message message) throws IOException,
            ECodexConnectorGatewayWebserviceClientException {
        SendRequest request = new SendRequest();

        if (message.getMessageContent() != null) {
            DataHandler contentHandler = new DataHandler(message.getMessageContent().getXmlContent(), "text/xml");
            request.getPayload().add(contentHandler);
        }

        if (message.getAttachments() != null) {
            for (MessageAttachment attachment : message.getAttachments()) {
                DataHandler dh = new DataHandler(attachment.getAttachment(), attachment.getMimeType());
                request.getPayload().add(dh);
            }
        }

        if (message.getConfirmations() != null) {
            // Pick only the last produced evidence
            DataHandler evidenceHandler = new DataHandler(
                    message.getConfirmations()[message.getConfirmations().length].getEvidence(), "text/xml");
            request.getPayload().add(evidenceHandler);
        }

        if (request.getPayload().isEmpty()) {
            throw new ECodexConnectorGatewayWebserviceClientException("No payload to send. Message without content?");
        }

        return request;
    }

    private PartyInfo buildPartyInfo(MessageDetails messageDetails) {
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        partyId.setValue(connectorProperties.getGatewayName());
        from.getPartyId().add(partyId);
        from.setRole(connectorProperties.getGatewayRole());
        partyInfo.setFrom(from);

        To to = new To();
        PartyId partyId2 = new PartyId();
        partyId2.setValue(messageDetails.getToPartner().getName());
        to.getPartyId().add(partyId2);
        to.setRole(messageDetails.getToPartner().getRole());
        partyInfo.setTo(to);

        return partyInfo;
    }

    private CollaborationInfo buildCollaborationInfo(MessageDetails messageDetails) {
        CollaborationInfo info = new CollaborationInfo();

        info.setAction(messageDetails.getAction().toString());
        Service service = new Service();
        service.setValue(messageDetails.getService().toString());
        info.setService(service);

        AgreementRef ref = new AgreementRef();
        ref.setPmode(PMODE);
        ref.setValue(AGREEMENT_REF);
        info.setAgreementRef(ref);

        info.setConversationId(messageDetails.getConversationId());

        return info;
    }

}
