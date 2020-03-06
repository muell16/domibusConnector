
package eu.domibus.connector.persistence.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.exceptions.LargeFileDeletionException;
import eu.domibus.connector.persistence.service.exceptions.LargeFileException;

/**
 * This interface describes a service for storing large amount of data
 * The by this service returned DomibusConnectorBigDataReference contains 
 * a OutputStream and a InputStream if this streams are available depends on 
 * the state of the DomibusConnectorBigDataReference also be aware that some
 * implementations can only read and write to this streams during an active
 * transaction (as an example the default database based implementation).
 * 
 * 
 * the implementation should at least manage 4GB
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//TODO: rename BigData to LargeFile
public interface DomibusConnectorBigDataPersistenceService {

    /**
     * returns a instance of DomibusConnectorBigDataReference,
     * where the getInputStream method will return a valid inputStream
     * @param bigDataReference the DomibusConnectorBigDataReference
     * @return the DomibusConnectorBigDataReference with initialized inputStream
     */
    public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference);
    
        
    /**
     * will create a new instance of DomibusConnectorBigDataReference     
     * @param input the inputstream holding the content
     * @param connectorMessageId the unique id of this message
     * @param documentName the name of this document
     * @param documentContentType the type of this document
     * @return the created DomibusConnectorBigDataReference with CLOSED OutputStream
     */
    @Deprecated
    DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(InputStream input, String connectorMessageId, String documentName,
    		String documentContentType);

    /**
     * will create a new instance of DomibusConnectorBigDataReference
     * @param connectorMessageId the unique id of this message
     * @param documentName the name of this document
     * @param documentContentType the type of this document
     * @return the created DomibusConnectorBigDataReference with OPEN OutputStream ready for WRITE,
     *      but closed INPUT stream
     *
     */
    DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(String connectorMessageId, String documentName, String documentContentType);

    
    /**
     * will delete the provided bigDataReference
     * @param bigDataReference the reference
     * @throws LargeFileDeletionException - in case of anything fails to delete the file
     */
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorBigDataReference bigDataReference) throws LargeFileDeletionException;

    /**
     * Returns a map of ALL currently not deleted bigDataReferences
     */
    public Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<DomibusConnectorBigDataReference>> getAllAvailableReferences();
}