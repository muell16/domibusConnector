/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import java.io.InputStream;
import java.io.OutputStream;

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
     * returns a instance of DomibusConnectorBigDataReference,
     * where the getOutpuStream method will return a valid outputStream
     * 
     * the implementation will overwrite the data referenced by the provided id
     * 
     * @param bigDataReference - the big data reference
     * @return the BigDataReference with writeable OutputStream
     */
    public DomibusConnectorBigDataReference getWriteableDataSource(DomibusConnectorBigDataReference bigDataReference);
    
    
    /**
     * will create a new instance of DomibusConnectorBigDataReference     
     * @return the created DomibusConnectorBigDataReference with writeable OutputStream
     */
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference();
    
}
