package eu.ecodex.connector.gwc.helper;

import java.util.List;

import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.springframework.util.StringUtils;

import backend.ecodex.org._1_1.DownloadMessageResponse;
import backend.ecodex.org._1_1.PayloadType;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageAttachment;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageContent;
import eu.domibus.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.gwc.util.CommonMessageHelper;

public class DownloadMessageHelper {

    private CommonMessageHelper commonMessageHelper;

    public void setCommonMessageHelper(CommonMessageHelper commonMessageHelper) {
        this.commonMessageHelper = commonMessageHelper;
    }

    public Message convertDownloadIntoMessage(Holder<DownloadMessageResponse> response, Holder<Messaging> ebMSHeader)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);
        MessageDetails details = commonMessageHelper.convertUserMessageToMessageDetails(userMessage);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        PayloadType bodyload = response.value.getBodyload();
        if (bodyload != null) {
            // bodyload dereferencing: The possible constellations are:
            // -- The header contains an href like "#id" and the bodyload a
            // PayloadId like "id"
            // -- The header contains no href and the bodyload the id "id"
            // -- The header contains an href like "#id" and the bodyload no
            // PayloadId
            String elementDescription = findBodyloadDescription(userMessage);

            // is it an Evidence or an eCodex content XML?

            if (elementDescription.equals(CommonMessageHelper.CONTENT_XML_NAME)) {
                message.getMessageContent().setECodexContent(bodyload.getValue());
            } else {
                MessageConfirmation confirmation = new MessageConfirmation();
                EvidenceType evidenceType = EvidenceType.valueOf(elementDescription);
                confirmation.setEvidenceType(evidenceType);
                confirmation.setEvidence(bodyload.getValue());
                message.addConfirmation(confirmation);
            }
            // }
        }

        List<PayloadType> payloads = response.value.getPayload();
        if (payloads != null && !payloads.isEmpty()) {
            if (userMessage.getPayloadInfo() != null && userMessage.getPayloadInfo().getPartInfo() != null
                    && (userMessage.getPayloadInfo().getPartInfo().size() - 1) == payloads.size()) {

                for (PayloadType payload : payloads) {
                    String elementDescription = findElementDesription(userMessage, payload.getPayloadId());

                    if (elementDescription == null) {
                        throw new ECodexConnectorGatewayWebserviceClientException(
                                "No PartInfo of PayloadInfo in ebms header found for actual payloads href "
                                        + payload.getPayloadId());
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
                        "Payload size does not match PayloadInfo size!");
            }
        }

        return message;
    }

    private String findElementDesription(UserMessage userMessage, String href) {
        for (PartInfo info : userMessage.getPayloadInfo().getPartInfo()) {
            if (info.getHref() != null && info.getHref().equals(href)) {
                return findPartPropertyDescription(info);
            }
        }

        return null;
    }

    private String findBodyloadDescription(UserMessage userMessage) {
        for (PartInfo info : userMessage.getPayloadInfo().getPartInfo()) {
            // if the PartInfo Href begins with "#" or is empty(null)
            if ((StringUtils.hasText(info.getHref()) && info.getHref().startsWith(
                    CommonMessageHelper.BODYLOAD_HREF_PREFIX))
                    || !StringUtils.hasText(info.getHref())) {
                return findPartPropertyDescription(info);
            }
        }

        return null;
    }

    private String findPartPropertyDescription(PartInfo info) {
        if (info.getPartProperties() != null && info.getPartProperties().getProperty() != null
                && !info.getPartProperties().getProperty().isEmpty()) {
            for (Property property : info.getPartProperties().getProperty()) {
                if (property.getName().equals(CommonMessageHelper.PARTPROPERTY_NAME)) {
                    return property.getValue();
                }
            }
        }
        return null;
    }

    private MessageConfirmation extractMessageConfirmation(PayloadType payload, String evidenceTypeString) {
        MessageConfirmation confirmation = new MessageConfirmation();
        EvidenceType evidenceType = EvidenceType.valueOf(evidenceTypeString);
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

}
