/*
 * 
 */
package org.holodeck.backend.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.holodeck.backend.util.StringUtils;
import org.apache.log4j.Logger;
import org.holodeck.backend.service.exception.SendMessageServiceException;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.springframework.stereotype.Service;

import backend.ecodex.org.Code;

/**
 * The Class SendMessageWithReferenceValidator.
 */
@Service
public class SendMessageWithReferenceValidator {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(SendMessageWithReferenceValidator.class);

	/**
	 * Validate.
	 *
	 * @param messaging the messaging
	 * @param sendRequestURL the send request url
	 * @throws SendMessageServiceException the send message service exception
	 */
	public void validate(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging,
			backend.ecodex.org.SendRequestURL sendRequestURL) throws SendMessageServiceException{
		log.debug("Validating SendMessageWithReference");

		validateMessaging(messaging);
		validateSendRequestURL(sendRequestURL);
	}

	/**
	 * Validate send request url.
	 *
	 * @param sendRequestURL the send request url
	 * @throws SendMessageServiceException the send message service exception
	 */
	private void validateSendRequestURL(backend.ecodex.org.SendRequestURL sendRequestURL) throws SendMessageServiceException{
		if(sendRequestURL==null){
			log.error("SendRequest is empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"SendRequest is empty", Code.ERROR_GENERAL_003);
			throw sendMessageServiceException;
		}

		if(sendRequestURL.getPayload()==null || sendRequestURL.getPayload().length==0 ){
			log.error("Payloads are empty");

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Payloads are empty", Code.ERROR_GENERAL_003);
			throw sendMessageServiceException;
		}

		int counter = 1;
		for(String payload: sendRequestURL.getPayload()){
			if(StringUtils.isEmpty(payload)){
				log.error("Payload " + counter +  "  is empty");

				SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
						"Payload " + counter +  "  is empty", Code.ERROR_GENERAL_003);
				throw sendMessageServiceException;
			}

			InputStream is = null;
			try {
				URL url = new URL(payload);
				is = url.openStream();
			} catch (MalformedURLException e) {
				log.error("Payload url " + counter +  " is invalid");

				SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
						"Payload url " + counter +  " is invalid", Code.ERROR_SEND_004);
				throw sendMessageServiceException;
			} catch (IOException e) {
				log.error("Payload url " + counter +  " is invalid");

				SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
						"Payload url " + counter +  " is invalid", Code.ERROR_SEND_004);
				throw sendMessageServiceException;
			}
			finally{
				if(is!=null){
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}


			counter++;
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
