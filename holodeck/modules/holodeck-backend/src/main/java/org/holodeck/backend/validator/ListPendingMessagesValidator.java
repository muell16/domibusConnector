/*
 * 
 */
package org.holodeck.backend.validator;

import org.apache.log4j.Logger;
import org.holodeck.backend.service.exception.DownloadMessageServiceException;
import org.springframework.stereotype.Service;

/**
 * The Class ListPendingMessagesValidator.
 */
@Service
public class ListPendingMessagesValidator {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(ListPendingMessagesValidator.class);

	/**
	 * Validate.
	 *
	 * @param listPendingMessagesRequest the list pending messages request
	 * @throws DownloadMessageServiceException the download message service exception
	 */
	public void validate(backend.ecodex.org.ListPendingMessagesRequest listPendingMessagesRequest) throws DownloadMessageServiceException{
		log.debug("Validating ListPendingMessages");
	}
}
