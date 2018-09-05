
package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

import javax.annotation.Nullable;

/**
 * This class contains static helper methods
 * for the domain model
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomainModelHelper {

    public static boolean isEvidenceMessage(DomibusConnectorMessage message) {
    	return message.getMessageContent() == null;
    }

    public static @Nullable
    DomibusConnectorEvidenceType getEvidenceTypeOfEvidenceMessage(DomibusConnectorMessage message) {
        if (message == null || message.getMessageConfirmations() == null || message.getMessageConfirmations().size() == 0) {
            return null;
        }
        return message.getMessageConfirmations().get(0).getEvidenceType();
    }
}
