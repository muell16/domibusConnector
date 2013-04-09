package eu.ecodex.connector.gwc.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import backend.ecodex.org._1_0.DownloadMessageResponse;
import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.enums.ServiceEnum;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class DownloadMessageHelper {

    public Message convertDownloadIntoMessage(Holder<DownloadMessageResponse> response, Holder<Messaging> ebMSHeader)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);
        MessageDetails details = convertUserMessageToMessageDetails(userMessage);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        List<DataHandler> payload = response.value.getPayload();
        if (payload != null && !payload.isEmpty() && userMessage.getPayloadInfo() != null
                && userMessage.getPayloadInfo().getPartInfo() != null
                && userMessage.getPayloadInfo().getPartInfo().size() == payload.size()) {

            int index = 0;
            for (DataHandler dh : payload) {
                PartInfo partInfo = userMessage.getPayloadInfo().getPartInfo().get(index);
                if (partInfo.getDescription().getValue().equals("ECodexContentXML")) {
                    extractMessageContent(dh, message.getMessageContent());
                } else if (matchesEvidenceType(partInfo.getDescription().getValue())) {
                    message.addConfirmation(extractMessageConfirmation(dh, partInfo.getDescription().getValue()));
                } else {
                    message.addAttachment(extractAttachments(dh, partInfo.getDescription().getValue()));
                }
                index++;
            }
        } else {
            throw new ECodexConnectorGatewayWebserviceClientException(
                    "Payload is empty or payload size does not match PayloadInfo size!");
        }

        return message;
    }

    private boolean matchesEvidenceType(String type) {
        return type.equals(ECodexEvidenceType.SUBMISSION_ACCEPTANCE.toString())
                || type.equals(ECodexEvidenceType.RELAY_REMMD_ACCEPTANCE.toString())
                || type.equals(ECodexEvidenceType.RELAY_REMMD_REJECTION.toString())
                || type.equals(ECodexEvidenceType.DELIVERY.toString())
                || type.equals(ECodexEvidenceType.NON_DELIVERY.toString())
                || type.equals(ECodexEvidenceType.RETRIEVAL.toString())
                || type.equals(ECodexEvidenceType.NON_RETRIEVAL.toString());
    }

    public Element createEmptyListPendingMessagesRequest() {
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

    private void extractMessageContent(DataHandler dh, MessageContent content) {
        try {
            content.setECodexContent(convertDataHandlerContentToByteArray(dh));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MessageConfirmation extractMessageConfirmation(DataHandler dh, String type) {
        MessageConfirmation confirmation = new MessageConfirmation();
        ECodexEvidenceType evidenceType = ECodexEvidenceType.valueOf(type);
        confirmation.setEvidenceType(evidenceType);
        try {
            confirmation.setEvidence(convertDataHandlerContentToByteArray(dh));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return confirmation;
    }

    private MessageAttachment extractAttachments(DataHandler dh, String description) {
        try {
            ByteArrayInputStream is = (ByteArrayInputStream) dh.getContent();
            byte[] b = new byte[is.available()];
            is.read(b);

            MessageAttachment attachment = new MessageAttachment();
            attachment.setAttachment(b);
            attachment.setMimeType(dh.getContentType());
            attachment.setName(description);
            return attachment;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            // LOGGER.error("No action {} found!", actionString);
        }

        String serviceString = userMessage.getCollaborationInfo().getService().getValue();
        try {
            ServiceEnum service = ServiceEnum.valueOf(serviceString);
            details.setService(service);
        } catch (IllegalArgumentException e) {
            // LOGGER.error("No service {} found!", actionString);
        }

        From from = userMessage.getPartyInfo().getFrom();
        PartnerEnum fromPartner = PartnerEnum.findValue(from.getPartyId().get(0).getValue(), from.getRole());
        details.setFromPartner(fromPartner);

        To to = userMessage.getPartyInfo().getTo();
        PartnerEnum toPartner = PartnerEnum.findValue(to.getPartyId().get(0).getValue(), to.getRole());
        details.setToPartner(toPartner);

        MessageProperties mp = userMessage.getMessageProperties();
        if (mp != null) {
            List<Property> properties = mp.getProperty();
            if (properties != null && !properties.isEmpty()) {
                for (Property property : properties) {
                    if (property.getName().equals("finalRecipient")) {
                        details.setFinalRecipient(property.getValue());
                    }
                    if (property.getName().equals("originalSender")) {
                        details.setOriginalSender(property.getValue());
                    }
                }
            }
        }

        return details;
    }
}
