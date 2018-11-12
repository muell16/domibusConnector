package eu.domibus.connector.controller.processor.content;

import eu.domibus.connector.controller.spring.ContentDeletionTimeoutConfigurationProperties;
import eu.domibus.connector.controller.test.util.DomibusConnectorBigDataReferenceInMemory;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


class CheckContentDeletedProcessorImplTest {

    CheckContentDeletedProcessorImpl checkContentDeletedProcessor;

    @Mock
    DomibusConnectorBigDataPersistenceService bigDataPersistenceService;
    @Mock
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    //ContentDeletionTimeoutConfigurationProperties contentDeletionTimeoutConfigurationProperties;

    private DomibusConnectorMessage message;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        checkContentDeletedProcessor = new CheckContentDeletedProcessorImpl();
        checkContentDeletedProcessor.setBigDataPersistenceService(bigDataPersistenceService);
        checkContentDeletedProcessor.setMessagePersistenceService(messagePersistenceService);



        message = DomainEntityCreator.createMessage();
        message.getMessageDetails().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
        message.getMessageDetails().setDeliveredToGateway(new Date()); //already delivered
        message.setConnectorMessageId("msg1");
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(any())).thenReturn(message);

        List<DomibusConnectorBigDataReference> bigDataReferenceList = new ArrayList<>();

        DomibusConnectorBigDataReferenceInMemory ref1 = new DomibusConnectorBigDataReferenceInMemory();
        ref1.setStorageIdReference("storageid1");
        bigDataReferenceList.add(ref1);
        DomibusConnectorBigDataReferenceInMemory ref2 = new DomibusConnectorBigDataReferenceInMemory();
        ref2.setStorageIdReference("storageid2");
        bigDataReferenceList.add(ref2);

        DomibusConnectorMessage.DomibusConnectorMessageId msgId = new DomibusConnectorMessage.DomibusConnectorMessageId("msg1");

        Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<DomibusConnectorBigDataReference>> map = new HashMap<>();
        map.put(msgId, bigDataReferenceList);

        Mockito.when(bigDataPersistenceService.getAllAvailableReferences()).thenReturn(map);

    }

    @Test
    public void testCheckContentDeletedProcessor_alreadyDelivered_shouldDelete() {

        checkContentDeletedProcessor.checkContentDeletedProcessor();

        //2 storage references - should be invoked twice
        Mockito.verify(bigDataPersistenceService, Mockito.times(2)).deleteDomibusConnectorBigDataReference(any());
    }

    @Test
    public void testCheckContentDeletedProcessor_notDeliveredYetButRejected_shouldDelete() {
        message.getMessageDetails().setFailed(new Date());
        message.getMessageDetails().setDeliveredToGateway(null);
        message.getMessageDetails().setDeliveredToBackend(null);

        checkContentDeletedProcessor.checkContentDeletedProcessor();

        //2 storage references - should be invoked twice
        Mockito.verify(bigDataPersistenceService, Mockito.times(2)).deleteDomibusConnectorBigDataReference(any());
    }

    @Test
    public void testCheckContentDeletedProcessor_notDeliveredYet_shouldNotDelete() {
        message.getMessageDetails().setDeliveredToGateway(null);
        message.getMessageDetails().setDeliveredToBackend(null);

        checkContentDeletedProcessor.checkContentDeletedProcessor();

        //2 storage references - but should not invoked because not delivered yet!
        Mockito.verify(bigDataPersistenceService, Mockito.times(0)).deleteDomibusConnectorBigDataReference(any());
    }

}