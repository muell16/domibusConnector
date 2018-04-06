
package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public enum StoreType {
    MESSAGE_CONTENT("domibus_message_content", DomibusConnectorMessageContent.class), 
    MESSAGE_ATTACHMENT("domibus_message_attachment", DomibusConnectorMessageAttachment.class), 
    MESSAGE_CONFIRMATION("domibus_message_confirmation", DomibusConnectorMessageConfirmation.class);
    
    private final String dbString;
    private final Class domainClazz;

    private StoreType(String dbString, Class domainClazz) {
        this.dbString = dbString;
        this.domainClazz = domainClazz;
    }
    
    public Class getDomainClazz() {
        return domainClazz;
    }

    public String getDbString() {
        return dbString;
    }

}
