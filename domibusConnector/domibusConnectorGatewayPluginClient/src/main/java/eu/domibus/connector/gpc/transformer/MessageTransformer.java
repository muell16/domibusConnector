package eu.domibus.connector.gpc.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.cxf.attachment.ByteDataSource;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageAttachmentType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageCollaborationInfoType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageContentType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageDetailsType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessagePartyInfoType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessagePropertiesType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessagePropertyType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.PartyType;
import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageAttachment;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageContent;
import eu.domibus.connector.common.message.MessageDetails;
import eu.domibus.connector.gpc.validator.DomibusConnectorMessageValidator;

@Component
public class MessageTransformer {

	org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MessageTransformer.class);

	private static final String ORIGINAL_SENDER_PROPERTY_NAME = "originalSender";
	private static final String FINAL_RECIPIENT_PROPERTY_NAME = "finalRecipient";
	private static final String ORIGINAL_MESSAGE_ID = "originalMessageId";
	private static final String XML_MIME_TYPE = "text/xml";
	private static final String APPLICATION_MIME_TYPE = "application/octet-stream";
	private static final String CONTENT_PDF_NAME = "ContentPDF";
	private static final String CONTENT_XML_NAME = "ContentXML";

	@Autowired
	private CommonConnectorProperties connectorProperties;

	@Autowired
	private DomibusConnectorPersistenceService persistenceService;

	public Message transformToDTO(MessageType msgType) throws Exception{
		DomibusConnectorMessageValidator.validateMessage(msgType);

		MessageDetails messageDetails = transformMessageDetailsToDTO(msgType);

		MessageContent messageContent = new MessageContent();

		Message message = new Message(messageDetails, messageContent);

		MessageContentType msgContent = msgType.getMessageContent();

		String contentName = msgContent.getContentName();
		String contentMimeType = msgContent.getContentMimeType();
		if(contentName!=null && contentMimeType!=null){
			EvidenceType evidenceType = isEvidence(contentName);
			if(evidenceType!=null){
				MessageConfirmation messageConfirmation = new MessageConfirmation(evidenceType);
				messageConfirmation.setEvidence(extractBytesFromDataHandler(msgContent.getContentData()));
				message.addConfirmation(messageConfirmation);
			}else if (contentMimeType.equals(XML_MIME_TYPE)){
				messageContent.setInternationalContent(extractBytesFromDataHandler(msgContent.getContentData()));
			}else if (contentMimeType.equals(APPLICATION_MIME_TYPE)){
				messageContent.setPdfDocument(extractBytesFromDataHandler(msgContent.getContentData()));
				messageContent.setPdfDocumentName(contentName);
			}
		}else
			throw new Exception("Message contains no valid content!");

		List<MessageAttachmentType> msgAttachments = msgType.getMessageAttachments();
		if(!CollectionUtils.isEmpty(msgAttachments)){
			for (MessageAttachmentType attachment:msgAttachments){
				String attachmentIdentifier = attachment.getAttachmentIdentifier();
				byte[] attachmentData = extractBytesFromDataHandler(attachment.getAttachmentData());
				if(!StringUtils.isEmpty(attachmentIdentifier) && !ArrayUtils.isEmpty(attachmentData)){
					if(attachmentIdentifier.equals(CONTENT_PDF_NAME)){
						messageContent.setPdfDocument(attachmentData);
						messageContent.setPdfDocumentName(attachmentIdentifier);
					}else{
						EvidenceType evidenceType = isEvidence(attachmentIdentifier);
						if(evidenceType!=null){
							MessageConfirmation messageConfirmation = new MessageConfirmation(evidenceType);
							messageConfirmation.setEvidence(attachmentData);
							message.addConfirmation(messageConfirmation);
						}else{
							MessageAttachment att = new MessageAttachment(attachmentData, attachmentIdentifier);
							att.setName(attachment.getAttachmentName());
							att.setMimeType(attachment.getAttachmentMimeType());
							att.setDescription(attachment.getAttachmentDescription());
							message.addAttachment(att);
						}
					}
				}
			}
		}


		return message;
	}

	private EvidenceType isEvidence(String value){
		try{
			EvidenceType ev = EvidenceType.valueOf(value);
			return ev;
		}catch(IllegalArgumentException e){
		}
		return null;

	}

	private MessageDetails transformMessageDetailsToDTO(MessageType msgType) {
		MessageDetails messageDetails = new MessageDetails();

		MessageDetailsType msgDetails = msgType.getMessageDetails();

		messageDetails.setEbmsMessageId(msgDetails.getMessageId());
		messageDetails.setRefToMessageId(msgDetails.getRefToMessageId());

		MessageCollaborationInfoType collaborationInfo = msgDetails.getCollaborationInfo();

		DomibusConnectorAction action = persistenceService.getAction(collaborationInfo.getAction());
		messageDetails.setAction(action);

		DomibusConnectorService service = persistenceService.getService(collaborationInfo.getServiceId());
		messageDetails.setService(service);

		messageDetails.setConversationId(collaborationInfo.getConversationId());


		MessagePartyInfoType partyInfo = msgDetails.getPartyInfo();

		DomibusConnectorParty fromParty = persistenceService.getPartyByPartyId(partyInfo.getFrom().getPartyId());
		messageDetails.setFromParty(fromParty);

		DomibusConnectorParty toParty = persistenceService.getPartyByPartyId(partyInfo.getTo().getPartyId());
		messageDetails.setToParty(toParty);


		MessagePropertiesType msgProperties = msgDetails.getMessageProperties();
		if(!CollectionUtils.isEmpty(msgProperties.getMessageProperties())){
			for(MessagePropertyType prop:msgProperties.getMessageProperties()){
				switch(prop.getName()){
				case ORIGINAL_SENDER_PROPERTY_NAME: messageDetails.setOriginalSender(prop.getValue());break;
				case FINAL_RECIPIENT_PROPERTY_NAME: messageDetails.setFinalRecipient(prop.getValue());break;
				}
			}
		}
		return messageDetails;
	}

	public MessageType transformFromDTO(Message msg){
		MessageType message = new MessageType();

		transformMessageDetailsFromDTO(msg, message);

		MessageContentType messageContent = new MessageContentType();

		MessageContent msgContent = msg.getMessageContent();

		if (msgContent != null) {
			byte[] content = msgContent.getInternationalContent() != null ? msgContent.getInternationalContent() : msgContent
					.getNationalXmlContent();

			messageContent.setContentData(buildByteArrayDataHandler(content, XML_MIME_TYPE));
			messageContent.setContentMimeType(XML_MIME_TYPE);
			messageContent.setContentName(CONTENT_XML_NAME);
		}

		boolean asicsFound = false;
		if (msg.getAttachments() != null) {
			for (MessageAttachment attachment : msg.getAttachments()) {
				if (attachment.getAttachment() != null && attachment.getAttachment().length > 0) {
					String mimeType = StringUtils.isEmpty(attachment.getMimeType()) ? APPLICATION_MIME_TYPE
							: attachment.getMimeType();
					MessageAttachmentType attType = new MessageAttachmentType();
					attType.setAttachmentData(buildByteArrayDataHandler(attachment.getAttachment(), mimeType));
					attType.setAttachmentMimeType(mimeType);
					attType.setAttachmentName(attachment.getName());
					attType.setAttachmentIdentifier(attachment.getIdentifier());
					attType.setAttachmentDescription(attachment.getDescription());

					message.getMessageAttachments().add(attType );

					if (attachment.getName().endsWith(".asics")) {
						asicsFound = true;
					}
				}
			}
		}

		if (!asicsFound && msgContent != null && msgContent.getPdfDocument() != null
				&& msgContent.getPdfDocument().length > 0) {
			MessageAttachmentType attType = new MessageAttachmentType();
			attType.setAttachmentData(buildByteArrayDataHandler(msgContent.getPdfDocument(), APPLICATION_MIME_TYPE));
			attType.setAttachmentMimeType(APPLICATION_MIME_TYPE);
			attType.setAttachmentName(CONTENT_PDF_NAME);
			attType.setAttachmentIdentifier(CONTENT_PDF_NAME);
			attType.setAttachmentDescription(CONTENT_PDF_NAME);

			message.getMessageAttachments().add(attType );
		}

		if (msg.getConfirmations() != null) {
			// Pick only the last produced evidence
			MessageConfirmation messageConfirmation = msg.getConfirmations().get(
					msg.getConfirmations().size() - 1);

			if (checkMessageConfirmationValid(messageConfirmation)) {
				if (messageContent.getContentData() == null) {
					// must be an evidence message
					messageContent.setContentData(buildByteArrayDataHandler(messageConfirmation.getEvidence(), XML_MIME_TYPE));
					messageContent.setContentMimeType(XML_MIME_TYPE);
					messageContent.setContentName(messageConfirmation.getEvidenceType().toString());
				} else {
					MessageAttachmentType attType = new MessageAttachmentType();
					attType.setAttachmentData(buildByteArrayDataHandler(messageConfirmation.getEvidence(), XML_MIME_TYPE));
					attType.setAttachmentMimeType(XML_MIME_TYPE);
					attType.setAttachmentName(messageConfirmation.getEvidenceType().toString());
					attType.setAttachmentIdentifier(messageConfirmation.getEvidenceType().toString());
					attType.setAttachmentDescription(messageConfirmation.getEvidenceType().toString());

					message.getMessageAttachments().add(attType );
				}
			}
		}

		message.setMessageContent(messageContent);

		return message;
	}

	private void transformMessageDetailsFromDTO(Message msg, MessageType message) {
		MessageDetailsType messageDetails = new MessageDetailsType();

		messageDetails.setMessageId(msg.getMessageDetails().getEbmsMessageId());
		messageDetails.setRefToMessageId(msg.getMessageDetails().getRefToMessageId());

		MessagePartyInfoType partyInfo = new MessagePartyInfoType();
		PartyType from = new PartyType();
		from.setPartyId(msg.getMessageDetails().getFromParty().getPartyId());
		from.setPartyIdType(msg.getMessageDetails().getFromParty().getPartyIdType());
		from.setRole(msg.getMessageDetails().getFromParty().getRole());
		partyInfo.setFrom(from );

		PartyType to = new PartyType();
		to.setPartyId(msg.getMessageDetails().getToParty().getPartyId());
		to.setPartyIdType(msg.getMessageDetails().getToParty().getPartyIdType());
		to.setRole(msg.getMessageDetails().getToParty().getRole());
		partyInfo.setTo(to );

		messageDetails.setPartyInfo(partyInfo );


		MessageCollaborationInfoType colInfo = new MessageCollaborationInfoType();
		colInfo.setAction(msg.getMessageDetails().getAction().getAction());
		colInfo.setServiceId(msg.getMessageDetails().getService().getService());
		colInfo.setServiceType(msg.getMessageDetails().getService().getServiceType());
		colInfo.setConversationId(msg.getMessageDetails().getConversationId());
		messageDetails.setCollaborationInfo(colInfo );

		MessagePropertiesType messageProps = new MessagePropertiesType();

		if (msg.getMessageDetails().getFinalRecipient() != null) {

			MessagePropertyType finalRecipient = new MessagePropertyType();
			finalRecipient.setName(FINAL_RECIPIENT_PROPERTY_NAME);
			finalRecipient.setValue(msg.getMessageDetails().getFinalRecipient());
			messageProps.getMessageProperties().add(finalRecipient);
		}

		if (msg.getMessageDetails().getOriginalSender() != null) {
			MessagePropertyType originalSender = new MessagePropertyType();
			originalSender.setName(ORIGINAL_SENDER_PROPERTY_NAME);
			originalSender.setValue(msg.getMessageDetails().getOriginalSender());
			messageProps.getMessageProperties().add(originalSender);
		}

		if (connectorProperties.isUseDynamicDiscovery()) {
			LOGGER.error("Dynamic Discovery not yet supported!");
		}

		if (msg.getMessageDetails().getRefToMessageId() != null) {
			MessagePropertyType originalMessageId = new MessagePropertyType();
			originalMessageId.setName(ORIGINAL_MESSAGE_ID);
			originalMessageId.setValue(msg.getMessageDetails().getRefToMessageId());
			messageProps.getMessageProperties().add(originalMessageId);
		}

		messageDetails.setMessageProperties(messageProps );

		message.setMessageDetails(messageDetails );
	}

	private DataHandler buildByteArrayDataHandler(byte[] data, String mimeType) {
		DataSource ds = new ByteDataSource(data, mimeType);
		DataHandler dh = new DataHandler(ds);

		return dh;
	}

	private byte[] extractBytesFromDataHandler(DataHandler dh){
		try {
			InputStream is = dh.getInputStream();
			byte[] b = new byte[is.available()];
			is.read(b);
			return b;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private boolean checkMessageConfirmationValid(MessageConfirmation messageConfirmation) {
		return messageConfirmation != null && messageConfirmation.getEvidence() != null
				&& messageConfirmation.getEvidence().length > 0 && messageConfirmation.getEvidenceType() != null;
	}


}
