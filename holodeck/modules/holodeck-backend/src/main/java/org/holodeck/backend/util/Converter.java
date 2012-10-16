/*
 * 
 */
package org.holodeck.backend.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.util.StAXUtils;
import org.apache.log4j.Logger;
import org.holodeck.backend.db.model.Message;
import org.holodeck.backend.service.exception.DownloadMessageServiceException;
import org.holodeck.ebms3.config.Producer;
import org.holodeck.ebms3.submit.MsgInfoSet;

import backend.ecodex.org.Code;
import backend.ecodex.org.ListPendingMessagesResponse;

/**
 * The Class Converter.
 */
public class Converter {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(Converter.class);

	/**
	 * Convert user message to msg info set.
	 *
	 * @param userMessage the user message
	 * @return the org.holodeck.ebms3.submit. msg info set
	 */
	public static org.holodeck.ebms3.submit.MsgInfoSet convertUserMessageToMsgInfoSet(
			org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage userMessage)
	{
		org.holodeck.ebms3.submit.MsgInfoSet msgInfoSet = new MsgInfoSet();
		if (userMessage.getCollaborationInfo() != null) {
			if (userMessage.getCollaborationInfo().getAgreementRef() != null) {
				msgInfoSet.setAgreementRef(userMessage.getCollaborationInfo().getAgreementRef().getNonEmptyString());

				msgInfoSet.setPmode(userMessage.getCollaborationInfo().getAgreementRef().getPmode().getNonEmptyString());
			}
			if (userMessage.getCollaborationInfo().getConversationId() != null) {
				msgInfoSet.setConversationId(userMessage.getCollaborationInfo().getConversationId().toString());
			}
		}
		if (userMessage.getPartyInfo() != null) {
			if (userMessage.getPartyInfo().getFrom() != null) {
				org.holodeck.ebms3.config.Producer producer = new Producer();
				if (userMessage.getPartyInfo().getFrom().getRole() != null) {
					producer.setRole(userMessage.getPartyInfo().getFrom().getRole().getNonEmptyString());
				}
				List<org.holodeck.ebms3.config.Party> parties = new ArrayList<org.holodeck.ebms3.config.Party>();
				if (userMessage.getPartyInfo().getFrom().getPartyId() != null) {
					for (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId partyId : userMessage
							.getPartyInfo().getFrom().getPartyId()) {
						org.holodeck.ebms3.config.Party party = new org.holodeck.ebms3.config.Party();
						party.setPartyId(partyId.getNonEmptyString());
						if (partyId.getType() != null) {
							party.setType(partyId.getType().getNonEmptyString());
						}

						parties.add(party);
					}
				}
				producer.setParties(parties);
				msgInfoSet.setProducer(producer);
			}
		}
		if (userMessage.getMessageProperties() != null && userMessage.getMessageProperties().getProperty() != null) {
			org.holodeck.ebms3.submit.Properties properties = new org.holodeck.ebms3.submit.Properties();
			for (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property property : userMessage
					.getMessageProperties().getProperty()) {
				properties.addProperty(property.getName().getNonEmptyString(), property.getNonEmptyString());
			}

			msgInfoSet.setProperties(properties);
		}
		if (userMessage.getPayloadInfo() != null && userMessage.getPayloadInfo().getPartInfo() != null) {
			org.holodeck.ebms3.submit.Payloads payloads = new org.holodeck.ebms3.submit.Payloads();

			boolean first = true;
			for (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo partInfo : userMessage.getPayloadInfo().getPartInfo()) {
				if(first){
					msgInfoSet.setBodyPayload((partInfo.getDescription()!=null)?partInfo.getDescription().getNonEmptyString():"");
					first = false;
				}
				else{
					payloads.addPayload((partInfo.getHref()!=null)?partInfo.getHref().toString():"", (partInfo.getDescription()!=null)?partInfo.getDescription().getNonEmptyString():"");
				}
			}

			msgInfoSet.setPayloads(payloads);
		}
		else{
			msgInfoSet.setPayloads(new org.holodeck.ebms3.submit.Payloads());
		}

		msgInfoSet.setLegNumber(1);

		return msgInfoSet;
	}

	/**
	 * Convert message list to list pending messages response.
	 *
	 * @param messages the messages
	 * @return the list pending messages response
	 */
	public static ListPendingMessagesResponse convertMessageListToListPendingMessagesResponse(List<Message> messages){
		ListPendingMessagesResponse listPendingMessagesResponse = new ListPendingMessagesResponse();

		if(messages!=null){
			String[] ids = new String[messages.size()];

			for(int i=0; i<messages.size(); i++){
				ids[i] = Integer.toString(messages.get(i).getIdMessage());
			}

			listPendingMessagesResponse.setMessageID(ids);
		}

		return listPendingMessagesResponse;
	}
	
	/**
	 * Convert file to messaging e.
	 *
	 * @param messageFile the message file
	 * @return the org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704. messaging e
	 * @throws DownloadMessageServiceException the download message service exception
	 */
	public static org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE  convertFileToMessagingE(
			File messageFile)throws DownloadMessageServiceException
	{
		if(messageFile==null || !messageFile.exists()){
			log.error("Error loading message file");

			DownloadMessageServiceException downloadMessageServiceException = new DownloadMessageServiceException(
					"Error loading message file", Code.ERROR_DOWNLOAD_003);
			throw downloadMessageServiceException;
		}

		XMLStreamReader xmlReader;
		try {
			xmlReader = StAXUtils.createXMLStreamReader(new FileInputStream(messageFile));

			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.Factory.parse(xmlReader);
		} catch (Exception e) {
			log.error("Error loading message file", e);

			DownloadMessageServiceException downloadMessageServiceException = new DownloadMessageServiceException(
					"Error loading message file", Code.ERROR_DOWNLOAD_003);
			throw downloadMessageServiceException;
		}
	}

	/**
	 * Convert user message to msg info set.
	 *
	 * @param userMessage the user message
	 * @return the org.holodeck.ebms3.submit. msg info set
	 */
	public static org.holodeck.logging.persistent.MessageInfo convertUserMessageToMessageInfo(
			org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage userMessage, String messageId, String service, String action, String status)
	{
		org.holodeck.logging.persistent.MessageInfo messageInfo = new org.holodeck.logging.persistent.MessageInfo();
		if (userMessage.getCollaborationInfo() != null) {
			if (userMessage.getCollaborationInfo().getAgreementRef() != null) {
				messageInfo.setPmode(userMessage.getCollaborationInfo().getAgreementRef().getPmode().getNonEmptyString());
			}
			if (userMessage.getCollaborationInfo().getConversationId() != null) {
				messageInfo.setConversationId(userMessage.getCollaborationInfo().getConversationId().toString());
			}
		}
		if (userMessage.getPartyInfo() != null) {
			if (userMessage.getPartyInfo().getFrom() != null) {
				if (userMessage.getPartyInfo().getFrom().getRole() != null) {
					messageInfo.setFromRole(userMessage.getPartyInfo().getFrom().getRole().getNonEmptyString());
				}
				if (userMessage.getPartyInfo().getFrom().getPartyId() != null) {
					String sender = null; 
					
					for (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId partyId : userMessage
							.getPartyInfo().getFrom().getPartyId()) {
						org.holodeck.ebms3.config.Party party = new org.holodeck.ebms3.config.Party();
						party.setPartyId(partyId.getNonEmptyString());
						if (partyId.getType() != null) {
							party.setType(partyId.getType().getNonEmptyString());
						}

						if(sender == null){
							sender = "";
						}
						else{
							sender += "\n";
						}
						
						if(party.getPartyId()!=null){
							sender += party.getPartyId();
						}
					}
					
					messageInfo.setSender(sender);
				}
			}
			
			if (userMessage.getPartyInfo().getTo() != null) {
				if (userMessage.getPartyInfo().getTo().getRole() != null) {
					messageInfo.setToRole(userMessage.getPartyInfo().getTo().getRole().getNonEmptyString());
				}
				if (userMessage.getPartyInfo().getTo().getPartyId() != null) {
					String recipient = null; 
					
					for (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId partyId : userMessage
							.getPartyInfo().getTo().getPartyId()) {
						org.holodeck.ebms3.config.Party party = new org.holodeck.ebms3.config.Party();
						party.setPartyId(partyId.getNonEmptyString());
						if (partyId.getType() != null) {
							party.setType(partyId.getType().getNonEmptyString());
						}

						if(recipient == null){
							recipient = "";
						}
						else{
							recipient += "\n";
						}
						
						if(party.getPartyId()!=null){
							recipient += party.getPartyId();
						}
					}
					
					messageInfo.setRecipient(recipient);
				}
			}
		}

		messageInfo.setMessageId(messageId);
		messageInfo.setService(service);
		messageInfo.setAction(action);
		messageInfo.setStatus(status);		

		return messageInfo;
	}
}