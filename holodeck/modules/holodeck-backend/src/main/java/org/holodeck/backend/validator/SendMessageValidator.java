/*
 * 
 */
package org.holodeck.backend.validator;

import org.apache.log4j.Logger;
import org.holodeck.backend.service.exception.SendMessageServiceException;
import org.holodeck.backend.util.StringUtils;
import org.springframework.stereotype.Service;

import backend.ecodex.org.Code;

/**
 * The Class SendMessageValidator.
 */
@Service
public class SendMessageValidator {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(SendMessageValidator.class);

	/**
	 * Validate.
	 *
	 * @param messaging the messaging
	 * @param sendRequest the send request
	 * @throws SendMessageServiceException the send message service exception
	 */
	public void validate(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging,
			backend.ecodex.org.SendRequest sendRequest) throws SendMessageServiceException{
		log.debug("Validating SendMessage");

		validateMessaging(messaging);
		validateSendRequest(sendRequest);
	}

	/**
	 * Validate send request.
	 *
	 * @param sendRequest the send request
	 * @throws SendMessageServiceException the send message service exception
	 */
	private void validateSendRequest(backend.ecodex.org.SendRequest sendRequest) throws SendMessageServiceException{
		if(sendRequest==null){
			log.error("SendRequest is empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"SendRequest is empty", Code.ERROR_GENERAL_003);
			throw sendMessageServiceException;
		}

		if(sendRequest.getPayload()==null || sendRequest.getPayload().length==0){
			log.error("Payloads are empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Payloads are empty", Code.ERROR_GENERAL_003);
			throw sendMessageServiceException;
		}
	}

	/**
	 * Validate messaging.
	 *
	 * @param messaging the messaging
	 * @throws SendMessageServiceException the send message service exception
	 */
	private void validateMessaging(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging) throws SendMessageServiceException{
		if(messaging==null){
			log.error("Messaging is empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Messaging is empty", Code.ERROR_GENERAL_002);
			throw sendMessageServiceException;
		}

		if(messaging.getMessaging().getUserMessage()==null || messaging.getMessaging().getUserMessage().length==0){
			log.error("UserMessage is empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"UserMessage is empty", Code.ERROR_GENERAL_002);
			throw sendMessageServiceException;
		}

		if(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo()==null){
			log.error("CollaborationInfo is empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"CollaborationInfo is empty", Code.ERROR_GENERAL_002);
			throw sendMessageServiceException;
		}

//		if(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAgreementRef()==null){
//			log.error("AgreementRef is empty");
//
//			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
//					"AgreementRef is empty", Code.ERROR_GENERAL_002);
//			throw sendMessageServiceException;
//		}		
		
		if(messaging.getMessaging().getUserMessage()[0].getPartyInfo()==null
				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo() == null
				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId() == null
				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId().length == 0
				|| StringUtils.isEmpty(messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getNonEmptyString())
//				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getType()==null
//				|| StringUtils.isEmpty(messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getType().getNonEmptyString())
				
				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom() == null
				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId() == null
				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId().length == 0
				|| StringUtils.isEmpty(messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getNonEmptyString())
//				|| messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getType()==null
//				|| StringUtils.isEmpty(messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getType().getNonEmptyString())
				
				|| messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getService()==null
				|| StringUtils.isEmpty(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getService().getNonEmptyString())
				|| messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAction()==null
				|| StringUtils.isEmpty(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAction().toString())
				){
			log.error("The parameterers needed to find an appropiate pmode are missing or invalid");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"The parameterers needed to find an appropiate pmode are missing or invalid", Code.ERROR_GENERAL_002);
			throw sendMessageServiceException;
		}
		
		String action = messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAction().toString();
		String fromPartyid = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getNonEmptyString();
		String fromPartyidType = null;
//		String fromPartyidType = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getType().getNonEmptyString();
		String toPartyid = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getNonEmptyString();
		String toPartyidType = null;
//		String toPartyidType = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getType().getNonEmptyString();
		String service = messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getService().getNonEmptyString();
		
		String pmode = org.holodeck.ebms3.module.Configuration.getPMode(action, service, fromPartyid, fromPartyidType, toPartyid, toPartyidType);

		if(pmode==null){
			log.error("Cannot find the appropiate pmode");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Cannot find the appropiate pmode", Code.ERROR_GENERAL_002);
			throw sendMessageServiceException;
		}
	}
}
