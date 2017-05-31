package eu.domibus.connector.gpc.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageCollaborationInfoType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageContentType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageDetailsType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessagePartyInfoType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessagePropertiesType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessagePropertyType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.PartyType;

public class DomibusConnectorMessageValidator {

	public static void validateMessage(MessageType message) throws DomibusConnectorMessageValidationException{
		
		org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DomibusConnectorMessageValidator.class);

		Collection<String> validationResults = new ArrayList<String>();
		MessageDetailsType messageDetails = message.getMessageDetails();
		if(messageDetails!=null){
			validateMessageDetails(messageDetails, validationResults);
		}else{
			validationResults.add("Message contains no MessageDetails.");
		}

		MessageContentType messageContent = message.getMessageContent();
		if(messageContent!=null){
			validateMessageContent(messageContent, validationResults);
		}else{
			validationResults.add("Message contains no MessageContent.");
		}

		
		if(!CollectionUtils.isEmpty(validationResults)){
			Iterator<String> iterator = validationResults.iterator();
			while(iterator.hasNext())
				LOGGER.error(iterator.next());
			throw new DomibusConnectorMessageValidationException("There have been error results validating the Message! Please see logs for further details.");
		}

	}

	private static void validateMessageDetails(MessageDetailsType messageDetails, Collection<String> validationResults) {
		if(StringUtils.isEmpty(messageDetails.getMessageId()))
			validationResults.add("Message contains no Message-ID.");

		validatePartyInfo(messageDetails, validationResults);

		validateMessageProperties(messageDetails, validationResults);
		
		validateCollaborationInfo(messageDetails, validationResults);

	}

	private static void validateCollaborationInfo(MessageDetailsType messageDetails,
			Collection<String> validationResults) {
		MessageCollaborationInfoType collaborationInfo = messageDetails.getCollaborationInfo();
		if(collaborationInfo!=null){
			if(StringUtils.isEmpty(collaborationInfo.getAction()))
				validationResults.add("Message Action must not be null or empty!");
			if(StringUtils.isEmpty(collaborationInfo.getServiceId()))
				validationResults.add("Message ServiceId must not be null or empty!");
			if(StringUtils.isEmpty(collaborationInfo.getServiceType()))
				validationResults.add("Message ServiceType must not be null or empty!");
		}else{
			validationResults.add("The message contains no CollaborationInfo!");
		}
	}

	private static void validateMessageProperties(MessageDetailsType messageDetails,
			Collection<String> validationResults) {
		MessagePropertiesType messageProperties = messageDetails.getMessageProperties();
		if(messageProperties==null)
			validationResults.add("The message has no messageProperties!");
		else{
			List<MessagePropertyType> props = messageProperties.getMessageProperties();
			if(!CollectionUtils.isEmpty(props)){
				Iterator<MessagePropertyType> iterator = props.iterator();
				boolean finalRecipientFound = false;
				boolean originalSenderFound = false;
				while(iterator.hasNext()){
					MessagePropertyType next = iterator.next();
					if(next.getName().equals("finalRecipient"))
						finalRecipientFound = true;
					if(next.getName().equals("originalSender"))
						originalSenderFound = true;
				}
				if(!finalRecipientFound && StringUtils.isEmpty(messageDetails.getRefToMessageId()))
					validationResults.add("The message has no finalRecipient!");
				if(!originalSenderFound && StringUtils.isEmpty(messageDetails.getRefToMessageId()))
					validationResults.add("The message has no originalSender!");
			}else if(StringUtils.isEmpty(messageDetails.getRefToMessageId()))
				validationResults.add("The message has no messageProperties!");
		}
	}

	private static void validatePartyInfo(MessageDetailsType messageDetails, Collection<String> validationResults) {
		MessagePartyInfoType partyInfo = messageDetails.getPartyInfo();
		if(partyInfo!=null){

			PartyType from = partyInfo.getFrom();
			if(from!=null){
				if(StringUtils.isEmpty(from.getPartyId()))
					validationResults.add("The from party ID is null or empty.");
				if(StringUtils.isEmpty(from.getPartyIdType()))
					validationResults.add("The from party ID-type is null or empty.");
			}else
				validationResults.add("The From Party must not be null!");
			PartyType to = partyInfo.getTo();
			if(to!=null){
				if(StringUtils.isEmpty(to.getPartyId()))
					validationResults.add("The to party ID is null or empty.");
				if(StringUtils.isEmpty(to.getPartyIdType()))
					validationResults.add("The to party ID-type is null or empty.");
			}else
				validationResults.add("The To Party must not be null!");
		}else
			validationResults.add("The PartyInfo must not be null!");
	}

	public static void validateMessageContent(MessageContentType messageContent, Collection<String> validationResults){
		DataHandler contentData = messageContent.getContentData();
		if(contentData==null){
			validationResults.add("The message contains no MessageContent data!");
		}
		
		if(StringUtils.isEmpty(messageContent.getContentMimeType()))
			validationResults.add("The messageContent MimeType must not be null or empty!");
		
		if(StringUtils.isEmpty(messageContent.getContentName()))
			validationResults.add("The messageContent Name must not be null or empty!");
	}
}
