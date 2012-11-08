package eu.ecodex.connector.security;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.security.exception.ECodexConnectorSecurityException;

/**
 * Interface with methods to invoke WP4 functionality.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorSecurityToolkit {

    /**
     * Method to encrypt document from a national backend system to be sent over
     * gateway.
     * 
     * @param mainDocument
     *            - the main document. Pre-Validation not necessary.
     * @param attachments
     *            - If there are some.
     * @return A Package Object which contains encrypted parts for payload of
     *         the message.
     * @throws ECodexConnectorSecurityException
     */
    public Message encryptDocument(Object mainDocument, Object[] attachments) throws ECodexConnectorSecurityException;

    /**
     * Method to validate a document's signature.
     * 
     * @param mainDocument
     *            - the document to validate
     * @return a verification document -> Structure to be defined
     * @throws ECodexConnectorSecurityException
     */
    public Object validateDocument(Object mainDocument) throws ECodexConnectorSecurityException;

}
