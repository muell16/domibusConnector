package eu.ecodex.connector.gwc.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import backend.ecodex.org.DownloadMessageResponse;
import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.enums.ServiceEnum;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;

public class DownloadMessageHelper {

    public Message convertDownloadIntoMessage(Holder<DownloadMessageResponse> response, Holder<Messaging> ebMSHeader) {
        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);
        MessageDetails details = convertUserMessageToMessageDetails(userMessage);

        // TODO: Woher weiss ich, welcher Payload der content ist? Gibt es
        // referenzen aus der payloadInfo in der UserMessage? Derzeitige
        // Annahme: Der erste Payload ist der XML Content! Außerdem: Wie
        // identifiziere ich einen Payload als Evidence?

        List<DataHandler> payload = response.value.getPayload();
        MessageContent content = extractMessageContent(payload);

        Message message = new Message(details, content);

        if (payload.size() > 1) {
            List<MessageAttachment> attachments = extractAttachments(payload);
            message.setAttachments(attachments);
        }

        return message;
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

    private MessageContent extractMessageContent(List<DataHandler> payload) {
        DataHandler dh = payload.get(0);
        MessageContent content = new MessageContent();
        try {
            content.setECodexContent(convertDataHandlerContentToByteArray(dh));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private List<MessageAttachment> extractAttachments(List<DataHandler> payload) {
        List<MessageAttachment> attachments = new ArrayList<MessageAttachment>();
        for (int counter = 1; counter < payload.size(); counter++) {
            DataHandler dh = payload.get(counter);
            try {
                ByteArrayInputStream is = (ByteArrayInputStream) dh.getContent();
                byte[] b = new byte[is.available()];
                is.read(b);

                MessageAttachment attachment = new MessageAttachment();
                attachment.setAttachment(b);
                attachment.setMimeType(dh.getContentType());
                attachments.add(attachment);
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

        return details;
    }
}
