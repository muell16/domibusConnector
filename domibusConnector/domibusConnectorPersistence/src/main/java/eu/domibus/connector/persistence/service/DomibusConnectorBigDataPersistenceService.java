/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

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
     * @param message - the message the data is related to
     * @return the created DomibusConnectorBigDataReference with writeable OutputStream
     */
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(DomibusConnectorMessage message);
    
    /**
     * will delete all messages related to the provides message
     * @param message the message
     */
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorMessage message);
    
}
