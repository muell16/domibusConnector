package eu.domibus.connector.domain.transformer;

import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 11:59:43
 */
public class DomibusConnectorDomainMessageTransformer {

	/**
	 * 
	 * @param domainMessage
	 */
	public static DomibusConnectorMessageType transformDomainToTransition(final DomibusConnectorMessage domainMessage){
		return null;
	}

	/**
	 * 
	 * @param transitionMessage
	 */
	public static DomibusConnectorMessage transformTransitionToDomain(final DomibusConnectorMessageType transitionMessage){
		return null;
	}

}