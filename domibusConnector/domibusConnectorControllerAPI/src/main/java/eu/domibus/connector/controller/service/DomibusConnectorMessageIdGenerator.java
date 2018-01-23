
package eu.domibus.connector.controller.service;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorMessageIdGenerator {
    
    /**
     * generates a uniquie Message Id for a received message
     * the maximum string length for this id is 255
     * @return the message id 
     */
    public String generateDomibusConnectorMessageId();

}
