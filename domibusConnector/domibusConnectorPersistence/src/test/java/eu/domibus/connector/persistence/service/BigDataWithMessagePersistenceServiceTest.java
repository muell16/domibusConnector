package eu.domibus.connector.persistence.service;

import eu.domibus.connector.persistence.service.impl.BigDataWithMessagePersistenceServiceImpl;
import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.test.util.DomainEntityCreatorForPersistenceTests;
import eu.domibus.connector.domain.transformer.util.DomibusConnectorBigDataReferenceMemoryBacked;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 *
 */
public class BigDataWithMessagePersistenceServiceTest {
    
    @Mock
    private DomibusConnectorBigDataPersistenceService bigDataPersistenceServiceImpl;

    DomibusConnectorPersistAllBigDataOfMessageService persistenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        BigDataWithMessagePersistenceServiceImpl impl = new BigDataWithMessagePersistenceServiceImpl();
        impl.setBigDataPersistenceServiceImpl(bigDataPersistenceServiceImpl);
        persistenceService = impl;
    }
    
    private static DomibusConnectorBigDataReferenceMemoryBacked generateNewDomibusConnectorBigDataReferenceMemoryBacked() {
        DomibusConnectorBigDataReferenceMemoryBacked ref = new DomibusConnectorBigDataReferenceMemoryBacked();
        ref.setStorageIdReference(UUID.randomUUID().toString());
        return ref;
    }

//    @Test(timeout=20000)
//    public void testPersistAllBigFilesFromMessage() {
//        DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
//        
//        Mockito.when(bigDataPersistenceServiceImpl.createDomibusConnectorBigDataReference(any(DomibusConnectorMessage.class)))
//                .thenReturn(generateNewDomibusConnectorBigDataReferenceMemoryBacked());
//                
//        persistenceService.persistAllBigFilesFromMessage(msg);
//        
//        // 1 attachment and one document should saved to db makes 2 calls
//        Mockito.verify(bigDataPersistenceServiceImpl, Mockito.times(2)).createDomibusConnectorBigDataReference(any(DomibusConnectorMessage.class));
//    }

    @Test
    public void testLoadAllBigFilesFromMessage() {
        Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
            DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
            persistenceService.loadAllBigFilesFromMessage(msg);

            // 2 attachments should be load from db: one document, one attachment
            Mockito.verify(bigDataPersistenceServiceImpl, Mockito.times(2)).getReadableDataSource(any(DomibusConnectorBigDataReference.class));
        });
    }
    
}
