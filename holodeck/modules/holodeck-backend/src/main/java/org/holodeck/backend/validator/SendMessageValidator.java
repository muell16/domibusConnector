/*
 * 
 */
package org.holodeck.backend.validator;

import org.holodeck.backend.util.StringUtils;
import org.apache.log4j.Logger;
import org.holodeck.backend.service.exception.SendMessageServiceException;
import org.holodeck.ebms3.submit.MsgInfoSet;
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

		if(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAgreementRef()==null ||
				messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAgreementRef().getPmode()==null){
			log.error("AgreementRef is empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"AgreementRef is empty", Code.ERROR_GENERAL_002);
			throw sendMessageServiceException;
		}

		if(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAgreementRef().getPmode()==null
				|| StringUtils.isEmpty(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAgreementRef().getPmode().getNonEmptyString())){
			log.error("Pmode is empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Pmode is empty", Code.ERROR_GENERAL_002);
			throw sendMessageServiceException;
		}

		MsgInfoSet msgInfoSet = new MsgInfoSet();
		msgInfoSet.setPmode(messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAgreementRef().getPmode().getNonEmptyString());

		String mep = org.holodeck.ebms3.module.Configuration.getMep(msgInfoSet);

		if(mep==null){
			log.error("Invalid pmode");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Invalid pmode", Code.ERROR_SEND_001);
			throw sendMessageServiceException;
		}
	}
}
