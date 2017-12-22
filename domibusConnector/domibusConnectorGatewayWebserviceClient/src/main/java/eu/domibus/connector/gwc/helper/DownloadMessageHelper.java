package eu.domibus.connector.gwc.helper;

import java.util.List;

import javax.xml.ws.Holder;

import org.apache.commons.lang.ArrayUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.springframework.util.StringUtils;

import backend.ecodex.org._1_1.DownloadMessageResponse;
import backend.ecodex.org._1_1.PayloadType;
import eu.domibus.connector.common.CommonConnectorGlobalConstants;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageAttachment;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageContent;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.gwc.util.CommonMessageHelper;

public class DownloadMessageHelper {

	private CommonMessageHelper commonMessageHelper;

	public void setCommonMessageHelper(CommonMessageHelper commonMessageHelper) {
		this.commonMessageHelper = commonMessageHelper;
	}

	public Message convertDownloadIntoMessage(Holder<DownloadMessageResponse> response, Holder<Messaging> ebMSHeader)
			throws DomibusConnectorGatewayWebserviceClientException {
		UserMessage userMessage = ebMSHeader.value.getUserMessage();
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

			if (elementDescription.equals(CommonConnectorGlobalConstants.OLD_CONTENT_XML_NAME) || elementDescription.equals(CommonConnectorGlobalConstants.CONTENT_XML_IDENTIFIER)) {
				message.getMessageContent().setInternationalContent(bodyload.getValue());
			} else if (isEvidence(elementDescription)) {
				MessageConfirmation confirmation = new MessageConfirmation();
				EvidenceType evidenceType = EvidenceType.valueOf(elementDescription);
				confirmation.setEvidenceType(evidenceType);
				confirmation.setEvidence(bodyload.getValue());
				message.addConfirmation(confirmation);
			}
		}

		List<PayloadType> payloads = response.value.getPayload();
		if (payloads != null && !payloads.isEmpty()) {
			if (userMessage.getPayloadInfo() != null && userMessage.getPayloadInfo().getPartInfo() != null
					//&& (userMessage.getPayloadInfo().getPartInfo().size() - 1) == payloads.size()
					) {

				for (PayloadType payload : payloads) {
					String elementDescription = findElementDesription(userMessage, payload.getPayloadId());

					if (elementDescription == null) {
						throw new DomibusConnectorGatewayWebserviceClientException(
								"No PartInfo of PayloadInfo in ebms header found for actual payloads href "
										+ payload.getPayloadId());
					}
					
					if(ArrayUtils.isEmpty(payload.getValue())){
						throw new DomibusConnectorGatewayWebserviceClientException(
								"No payload value found for actual payloads href "
										+ payload.getPayloadId());
					}

					if (elementDescription.equals(CommonConnectorGlobalConstants.OLD_CONTENT_XML_NAME) || elementDescription.equals(CommonConnectorGlobalConstants.CONTENT_XML_IDENTIFIER)) {
						message.getMessageContent().setInternationalContent(payload.getValue());
					} else if (elementDescription.equals(CommonConnectorGlobalConstants.CONTENT_PDF_IDENTIFIER)) {
						message.getMessageContent().setPdfDocument(payload.getValue());
					} else if (isEvidence(elementDescription)) {
						message.addConfirmation(extractMessageConfirmation(payload, elementDescription));
					} else 
						message.addAttachment(extractAttachment(payload, elementDescription));
//					} else if (elementDescription.equals("tokenXML") || elementDescription.endsWith(".asics")) {
//						message.addAttachment(extractAttachment(payload, elementDescription));
//					} else {
//						throw new DomibusConnectorGatewayWebserviceClientException(
//								"Unknown description for a payload: " + elementDescription);
//					}

				}
			} else {
				throw new DomibusConnectorGatewayWebserviceClientException(
						"Payload size does not match PayloadInfo size!");
			}
		}

		return message;
	}

	private boolean isEvidence(String value) {
		try {
			EvidenceType ev = EvidenceType.valueOf(value);
			if (ev != null)
				return true;
		} catch (IllegalArgumentException e) {
		}
		return false;

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
			if ((StringUtils.hasText(info.getHref())
					&& info.getHref().startsWith(CommonMessageHelper.BODYLOAD_HREF_PREFIX))
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

		MessageAttachment attachment = new MessageAttachment(payload.getValue(), attachmentName);
		attachment.setMimeType(payload.getContentType());
		attachment.setName(attachmentName);
		return attachment;

	}

}
