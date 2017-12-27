package eu.domibus.connector.persistence.model.test.util;

import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class PersistenceMessageCreator {

    public final static  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Creates a default DomibusConnectorMessage, for testing purposes
     * @return - the message
     */
    public static DomibusConnectorMessage createSimpleDomibusConnectorMessage() {
        try {
            DomibusConnectorMessage msg = new DomibusConnectorMessage();

            msg.setNationalMessageId("national1");
            msg.setConfirmed(dateFormat.parse("2017-12-23 23:45:23"));
            msg.setConversationId("conversation1");
            msg.setHashValue("hashvalue");
            msg.setId(78L);            
            msg.setEvidences(new HashSet<>());
                        
            
            return msg;
            
        } catch (ParseException ex) {
            throw new RuntimeException("should not happen!");
        }
    }
    
}
