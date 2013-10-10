package eu.ecodex.connector.gwc._1_1;

import java.util.List;

import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3._2005._05.xmlmime.Base64Binary;

import backend.ecodex.org._1_1.DownloadMessageResponse;
import backend.ecodex.org._1_1.PayloadType;
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
import eu.ecodex.connector.gwc.util.CommonMessageHelper;

public class DownloadMessageHelper {

    public Message convertDownloadIntoMessage(Holder<DownloadMessageResponse> response, Holder<Messaging> ebMSHeader)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);
        MessageDetails details = convertUserMessageToMessageDetails(userMessage);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        Base64Binary bodyload = response.value.getBodyload();
        if (bodyload != null) {
            if (bodyload.getContentType().equals(CommonMessageHelper.XML_MIME_TYPE)) {
                String elementDescription = findElementDesription(userMessage, "bodyPayload");

                // is it an Evidence or an eCodex content XML?

                if (elementDescription.equals(CommonMessageHelper.CONTENT_XML_NAME)) {
                    message.getMessageContent().setECodexContent(bodyload.getValue());
                } else {
                    MessageConfirmation confirmation = new MessageConfirmation();
                    ECodexEvidenceType evidenceType = ECodexEvidenceType.valueOf(elementDescription);
                    confirmation.setEvidenceType(evidenceType);
                    confirmation.setEvidence(bodyload.getValue());
                    message.addConfirmation(confirmation);
                }
            }
        }

        List<PayloadType> payloads = response.value.getPayload();
        if (payloads != null && !payloads.isEmpty() && userMessage.getPayloadInfo() != null
                && userMessage.getPayloadInfo().getPartInfo() != null
                && userMessage.getPayloadInfo().getPartInfo().size() == payloads.size()) {

            for (PayloadType payload : payloads) {
                String elementDescription = findElementDesription(userMessage, payload.getHref());

                if (elementDescription == null) {
                    throw new ECodexConnectorGatewayWebserviceClientException(
                            "No PartInfo of PayloadInfo in ebms header found for actual payloads href "
                                    + payload.getHref());
                }

                if (elementDescription.equals(CommonMessageHelper.CONTENT_PDF_NAME)) {
                    message.getMessageContent().setPdfDocument(payload.getValue());
                } else if (elementDescription.equals("SUBMISSION_ACCEPTANCE")) {
                    // only SUBMISSION_ACCEPTANCE allowed with EPO message!
                    message.addConfirmation(extractMessageConfirmation(payload, elementDescription));
                } else if (elementDescription.equals("tokenXML") || elementDescription.endsWith(".asics")) {
                    message.addAttachment(extractAttachment(payload, elementDescription));
                } else {
                    throw new ECodexConnectorGatewayWebserviceClientException("Unknown description for a payload: "
                            + elementDescription);
                }

            }
        } else {
            throw new ECodexConnectorGatewayWebserviceClientException(
                    "Payload is empty or payload size does not match PayloadInfo size!");
        }

        return message;
    }

    private String findElementDesription(UserMessage userMessage, String href) {
        String elementDescription = null;
        for (PartInfo info : userMessage.getPayloadInfo().getPartInfo()) {
            if (info.getHref().equals(href)) {
                elementDescription = info.getDescription().getValue();
            }
        }

        return elementDescription;
    }

    private MessageConfirmation extractMessageConfirmation(PayloadType payload, String evidenceTypeString) {
        MessageConfirmation confirmation = new MessageConfirmation();
        ECodexEvidenceType evidenceType = ECodexEvidenceType.valueOf(evidenceTypeString);
        confirmation.setEvidenceType(evidenceType);

        confirmation.setEvidence(payload.getValue());

        return confirmation;
    }

    private MessageAttachment extractAttachment(PayloadType payload, String attachmentName) {

        MessageAttachment attachment = new MessageAttachment();
        attachment.setAttachment(payload.getValue());
        attachment.setMimeType(payload.getContentType());
        attachment.setName(attachmentName);
        return attachment;

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
