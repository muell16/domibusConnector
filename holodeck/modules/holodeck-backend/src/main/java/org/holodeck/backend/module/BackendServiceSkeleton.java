/*
 * 
 */
package org.holodeck.backend.module;

import org.apache.log4j.Logger;
import org.holodeck.backend.module.exception.DownloadMessageFault;
import org.holodeck.backend.module.exception.ListPendingMessagesFault;
import org.holodeck.backend.module.exception.SendMessageFault;
import org.holodeck.backend.module.exception.SendMessageWithReferenceFault;
import org.holodeck.backend.service.DownloadMessageService;
import org.holodeck.backend.service.SendMessageService;
import org.holodeck.backend.service.exception.DownloadMessageServiceException;
import org.holodeck.backend.service.exception.SendMessageServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import backend.ecodex.org.Code;
import backend.ecodex.org.FaultDetail;

/**
 * The Class BackendServiceSkeleton.
 */
public class BackendServiceSkeleton extends org.holodeck.backend.spring.BackendSpringBeanAutowiringSupport {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(BackendServiceSkeleton.class);

	/** The send message service. */
	@Autowired
	private SendMessageService sendMessageService;

	/** The download message service. */
	@Autowired
	private DownloadMessageService downloadMessageService;

	/**
	 * Send message with reference.
	 *
	 * @param messagingRequest the messaging request
	 * @param sendRequestURL the send request url
	 * @throws SendMessageWithReferenceFault the send message with reference fault
	 */
	public backend.ecodex.org.SendResponse sendMessageWithReference(
			org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messagingRequest,
			backend.ecodex.org.SendRequestURL sendRequestURL) throws SendMessageWithReferenceFault
	{
		try {
			log.debug("Executed BackendService.sendMessageWithReference");

			init();

			return sendMessageService.sendMessageWithReference(messagingRequest, sendRequestURL);
		} catch (SendMessageServiceException serviceException) {
			SendMessageWithReferenceFault fault = new SendMessageWithReferenceFault(serviceException);
			fault.setFaultMessage(serviceException.getFault());
			throw fault;
		} catch (Exception exception) {
			log.error("Unknown error in BackendService.sendMessageWithReference", exception);
			FaultDetail faultDetail = new FaultDetail();
			faultDetail.setCode(Code.ERROR_GENERAL_001);
			SendMessageWithReferenceFault fault = new SendMessageWithReferenceFault(exception);
			fault.setFaultMessage(faultDetail);
			throw fault;
		}
	}

	/**
	 * Send message.
	 *
	 * @param messagingRequest the messaging request
	 * @param sendRequest the send request
	 * @throws SendMessageFault the send message fault
	 */
	public backend.ecodex.org.SendResponse sendMessage(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messagingRequest,
			backend.ecodex.org.SendRequest sendRequest) throws SendMessageFault
	{
		try {
			log.debug("Executed BackendService.sendMessage");

			init();

			return sendMessageService.sendMessage(messagingRequest, sendRequest);
		} catch (SendMessageServiceException serviceException) {
			SendMessageFault fault = new SendMessageFault(serviceException);
			fault.setFaultMessage(serviceException.getFault());
			throw fault;
		} catch (Exception exception) {
			log.error("Unknown error in BackendService.sendMessage", exception);
			FaultDetail faultDetail = new FaultDetail();
			faultDetail.setCode(Code.ERROR_GENERAL_001);
			SendMessageFault fault = new SendMessageFault(exception);
			fault.setFaultMessage(faultDetail);
			throw fault;
		}
	}

	/**
	 * List pending messages.
	 *
	 * @param listPendingMessagesRequest the list pending messages request
	 * @return the backend.ecodex.org. list pending messages response
	 * @throws ListPendingMessagesFault the list pending messages fault
	 */
	public backend.ecodex.org.ListPendingMessagesResponse listPendingMessages(backend.ecodex.org.ListPendingMessagesRequest listPendingMessagesRequest) throws ListPendingMessagesFault {
		try {
			log.debug("Executed BackendService.listPendingMessages");

			init();

			backend.ecodex.org.ListPendingMessagesResponse listPendingMessagesResponse = downloadMessageService
					.listPendingMessages(listPendingMessagesRequest);
			return listPendingMessagesResponse;
		} catch (DownloadMessageServiceException serviceException) {
			ListPendingMessagesFault fault = new ListPendingMessagesFault(serviceException);
			fault.setFaultMessage(serviceException.getFault());
			throw fault;
		} catch (Exception exception) {
			log.error("Unknown error in BackendService.listPendingMessages", exception);
			FaultDetail faultDetail = new FaultDetail();
			faultDetail.setCode(Code.ERROR_GENERAL_001);
			ListPendingMessagesFault fault = new ListPendingMessagesFault(exception);
			fault.setFaultMessage(faultDetail);
			throw fault;
		}
	}

	/**
	 * Download message.
	 *
	 * @param downloadMessageResponse the download message response
	 * @param downloadMessageRequest the download message request
	 * @return the org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704. messaging e
	 * @throws DownloadMessageFault the download message fault
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE downloadMessage(
			backend.ecodex.org.DownloadMessageResponse downloadMessageResponse,
			backend.ecodex.org.DownloadMessageRequest downloadMessageRequest) throws DownloadMessageFault
	{
		try {
			log.debug("Executed BackendService.downloadMessage");

			init();

			org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messagingE = downloadMessageService
					.downloadMessage(downloadMessageResponse, downloadMessageRequest);
			return messagingE;
		} catch (DownloadMessageServiceException serviceException) {
			if(serviceException.getCode()==Code.ERROR_DOWNLOAD_003){
				downloadMessageService.deleteMessage(downloadMessageRequest);
			}			
			
			DownloadMessageFault fault = new DownloadMessageFault(serviceException);
			fault.setFaultMessage(serviceException.getFault());
			throw fault;
		} catch (Exception exception) {
			log.error("Unknown error in BackendService.downloadMessage", exception);
			FaultDetail faultDetail = new FaultDetail();
			faultDetail.setCode(Code.ERROR_GENERAL_001);
			DownloadMessageFault fault = new DownloadMessageFault(exception);
			fault.setFaultMessage(faultDetail);
			throw fault;
		}
	}
}
