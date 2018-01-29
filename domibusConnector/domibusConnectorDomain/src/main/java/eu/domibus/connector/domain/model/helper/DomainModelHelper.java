
package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * This class contains static helper methods
 * for the domain model
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomainModelHelper {

    public static boolean isEvidenceMessage(DomibusConnectorMessage message) {
    	if(message.getMessageContent()!=null) {
    		return false;
    	}
    	return true;
//        throw new RuntimeException("not implemented yet!");
    }
    
}
