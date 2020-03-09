package eu.domibus.connector.controller.service.impl;


import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DomibusConnectorTransportStatusServiceTest {

    DomibusConnectorTransportStatusService transportStatusService;

    @Mock
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Mock
    DomibusConnectorMessageErrorPersistenceService errorPersistenceService;

    @Mock
    DomibusConnectorPersistAllBigDataOfMessageService contentStorageService;

    DomibusConnectorMessage testMessage;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);

        testMessage = DomainEntityCreator.createMessage();
        testMessage.setConnectorMessageId("MSG1");
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(Mockito.eq("MSG1")))
                .thenReturn(testMessage);

        transportStatusService = new DomibusConnectorTransportStatusService();
        transportStatusService.setMessagePersistenceService(messagePersistenceService);
        transportStatusService.setErrorPersistenceService(errorPersistenceService);
        transportStatusService.setContentStorageService(contentStorageService);

    }

    @Test
    public void testSetTransportToGwStatus() {
        TransportStatusService.DomibusConnectorTransportState transportState = new TransportStatusService.DomibusConnectorTransportState();
        transportState.setRemoteTransportId("REMOTE1");
        transportState.setStatus(TransportState.ACCEPTED);
        transportState.setConnectorTransportId(new TransportStatusService.TransportId("MSG1"));

        transportStatusService.updateTransportToGatewayStatus(transportState);

        Mockito.verify(messagePersistenceService, Mockito.times(1)).mergeMessageWithDatabase(Mockito.eq(testMessage)); //, Mockito.times(1));
        Mockito.verify(messagePersistenceService, Mockito.times(1)).setDeliveredToGateway(Mockito.eq(testMessage)); //, Mockito.times(1));


    }


}