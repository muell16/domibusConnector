package eu.domibus.connector.gateway.link;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * @author riederb
 * @version 1.0
 * @created 02-J�n-2018 09:05:50
 */
public interface DomibusConnectorSubmissionService {

	/**
	 * 
	 * @param message
	 */
	public boolean submitMessage(DomibusConnectorMessage message);

}