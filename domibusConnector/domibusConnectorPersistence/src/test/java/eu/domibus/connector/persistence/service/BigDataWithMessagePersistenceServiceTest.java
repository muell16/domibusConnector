/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.test.util.DomainEntityCreatorForPersistenceTests;
import eu.domibus.connector.domain.transformer.util.DomibusConnectorBigDataReferenceMemoryBacked;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class BigDataWithMessagePersistenceServiceTest {
    
    @Mock
    private DomibusConnectorBigDataPersistenceService bigDataPersistenceServiceImpl;
    
    
    BigDataWithMessagePersistenceService persistenceService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        persistenceService = new BigDataWithMessagePersistenceService();
        persistenceService.setBigDataPersistenceServiceImpl(bigDataPersistenceServiceImpl);
    }
    
    private static DomibusConnectorBigDataReferenceMemoryBacked generateNewDomibusConnectorBigDataReferenceMemoryBacked() {
        DomibusConnectorBigDataReferenceMemoryBacked ref = new DomibusConnectorBigDataReferenceMemoryBacked();
        ref.setStorageIdReference(UUID.randomUUID().toString());
        return ref;
    }
    
    @Test
    public void testPersistAllBigFilesFromMessage() {
        DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
        
        Mockito.when(bigDataPersistenceServiceImpl.createDomibusConnectorBigDataReference(any(DomibusConnectorMessage.class)))
                .thenReturn(generateNewDomibusConnectorBigDataReferenceMemoryBacked());
                
        persistenceService.persistAllBigFilesFromMessage(msg);
        
        // 1 attachment and one document should saved to db makes 2 calls
        Mockito.verify(bigDataPersistenceServiceImpl, Mockito.times(2)).createDomibusConnectorBigDataReference(any(DomibusConnectorMessage.class));
    }

    @Test
    public void testLoadAllBigFilesFromMessage() {
        DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
        persistenceService.loadAllBigFilesFromMessage(msg);
        
        // 2 attachments should be load from db: one document, one attachment
        Mockito.verify(bigDataPersistenceServiceImpl, Mockito.times(2)).getReadableDataSource(any(DomibusConnectorBigDataReference.class));
    }
    
}
