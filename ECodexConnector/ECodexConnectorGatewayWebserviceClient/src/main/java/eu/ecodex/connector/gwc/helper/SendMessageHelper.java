package eu.ecodex.connector.gwc.helper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

import backend.ecodex.org._1_0.SendRequest;
import backend.ecodex.org._1_0.SendResponse;
import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class SendMessageHelper {

    private static final String XML_MIME_TYPE = "text/xml";
    private ECodexConnectorProperties connectorProperties;
    private ECodexConnectorPersistenceService persistenceService;

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void buildMessage(SendRequest request, Messaging ebMSHeaderInfo, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = new UserMessage();

        buildSendRequestAndPayloadInfo(userMessage, request, message);

        userMessage.setMessageProperties(buildMessageProperties(message));

        userMessage.setPartyInfo(buildPartyInfo(message.getMessageDetails()));

        userMessage.setCollaborationInfo(buildCollaborationInfo(message.getMessageDetails()));

        MessageInfo info = new MessageInfo();

        info.setRefToMessageId(message.getMessageDetails().getRefToMessageId());

        userMessage.setMessageInfo(info);

        ebMSHeaderInfo.getUserMessage().add(userMessage);
    }

    private MessageProperties buildMessageProperties(Message message) {
        if (message.getMessageDetails().getFinalRecipient() == null
                && message.getMessageDetails().getOriginalSender() == null) {
            return null;
        }
        MessageProperties mp = new MessageProperties();

        if (message.getMessageDetails().getFinalRecipient() != null) {

            Property finalRecipient = new Property();
            finalRecipient.setName("finalRecipient");
            finalRecipient.setValue(message.getMessageDetails().getFinalRecipient());
            mp.getProperty().add(finalRecipient);
        }

        if (message.getMessageDetails().getOriginalSender() != null) {
            Property originalSender = new Property();
            originalSender.setName("originalSender");
            originalSender.setValue(message.getMessageDetails().getOriginalSender());
            mp.getProperty().add(originalSender);
        }

        return mp;
    }

    private void buildSendRequestAndPayloadInfo(UserMessage userMessage, SendRequest request, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {
        PayloadInfo pli = new PayloadInfo();

        MessageContent messageContent = message.getMessageContent();
        if (messageContent != null) {
            request.getPayload().add(buildByteArrayDataHandler(messageContent.getECodexContent(), XML_MIME_TYPE));
            pli.getPartInfo().add(buildPartInfo("ECodexContentXML", messageContent.getECodexContent().length));
        }

        if (message.getAttachments() != null) {
            for (MessageAttachment attachment : message.getAttachments()) {
                request.getPayload().add(
                        buildByteArrayDataHandler(attachment.getAttachment(), attachment.getMimeType()));
                pli.getPartInfo().add(buildPartInfo(attachment.getName(), attachment.getAttachment().length));
            }
        }

        if (message.getConfirmations() != null) {
            // Pick only the last produced evidence
            MessageConfirmation messageConfirmation = message.getConfirmations().get(
                    message.getConfirmations().size() - 1);
            request.getPayload().add(buildByteArrayDataHandler(messageConfirmation.getEvidence(), XML_MIME_TYPE));
            pli.getPartInfo().add(
                    buildPartInfo(messageConfirmation.getEvidenceType().toString(),
                            messageConfirmation.getEvidence().length));
        }

        userMessage.setPayloadInfo(pli);

        if (request.getPayload().isEmpty()) {
            throw new ECodexConnectorGatewayWebserviceClientException("No payload to send. Message without content?");
        }
    }

    private DataHandler buildByteArrayDataHandler(byte[] data, String mimeType) {
        DataSource ds = new ByteArrayDataSource(data, mimeType);
        DataHandler dh = new DataHandler(ds);

        return dh;
    }

    private PartInfo buildPartInfo(String name, int length) {
        PartInfo pi = new PartInfo();

        String description = name + "$" + Integer.toString(length);

        Description desc = new Description();

        desc.setValue(description);

        pi.setDescription(desc);
        //
        // PartProperties properties = new PartProperties();
        //
        // Property nameProperty = new Property();
        // nameProperty.setName("name");
        // nameProperty.setValue(description);
        // properties.getProperty().add(nameProperty);
        //
        // Property lengthProperty = new Property();
        // lengthProperty.setName("length");
        // lengthProperty.setValue(Integer.toString(length));
        // properties.getProperty().add(lengthProperty);
        //
        // pi.setPartProperties(properties);

        return pi;
    }

    public void extractEbmsMessageIdAndPersistIntoDB(SendResponse response, Message message) {
        if (response.getMessageID() != null && !response.getMessageID().isEmpty()) {
            String ebmsMessageId = response.getMessageID().get(0);
            if (!ebmsMessageId.isEmpty()) {
                message.getDbMessage().setEbmsMessageId(ebmsMessageId);
                persistenceService.mergeMessageWithDatabase(message);
            }
        }
    }

    private PartyInfo buildPartyInfo(MessageDetails messageDetails) {
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        if (messageDetails.getFromPartner() != null) {
            partyId.setValue(messageDetails.getFromPartner().getName());
            from.setRole(messageDetails.getFromPartner().getRole());
        } else {
            partyId.setValue(connectorProperties.getGatewayName());
            from.setRole(connectorProperties.getGatewayRole());
        }
        from.getPartyId().add(partyId);
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

        info.setConversationId(messageDetails.getConversationId());

        return info;
    }

}
