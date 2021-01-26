package eu.domibus.connector.controller.service.impl;


import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageContentManager;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DomibusConnectorStringServiceTest {

    DomibusConnectorTransportStateService transportStatusService;

    @Mock
    DCMessagePersistenceService messagePersistenceService;

    @Mock
    DomibusConnectorMessageErrorPersistenceService errorPersistenceService;

    @Mock
    TransportStepPersistenceService transportStepPersistenceService;

    @Mock
    DomibusConnectorMessageContentManager contentStorageService;

    DomibusConnectorMessage testMessage;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);

        DomibusConnectorTransportStep domibusConnectorTransportStep = new DomibusConnectorTransportStep();
        domibusConnectorTransportStep.setMessageId(new DomibusConnectorMessageId("MSG1"));

        Mockito.when(transportStepPersistenceService.getTransportStepByTransportId(Mockito.any()))
                .thenReturn(domibusConnectorTransportStep);

        testMessage = DomainEntityCreator.createMessage();
        testMessage.setConnectorMessageId("MSG1");
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(Mockito.eq("MSG1")))
                .thenReturn(testMessage);

        transportStatusService = new DomibusConnectorTransportStateService();
        transportStatusService.setMessagePersistenceService(messagePersistenceService);
        transportStatusService.setErrorPersistenceService(errorPersistenceService);
        transportStatusService.setContentStorageService(contentStorageService);
        transportStatusService.setTransportStepPersistenceService(transportStepPersistenceService);

    }

    @Test
    public void testSetTransportToGwStatus() {
        TransportStateService.DomibusConnectorTransportState transportState = new TransportStateService.DomibusConnectorTransportState();
        transportState.setRemoteMessageId("REMOTE1");
        transportState.setStatus(TransportState.ACCEPTED);
        transportState.setConnectorTransportId(new TransportStateService.TransportId("MSG1"));

        transportStatusService.updateTransportToGatewayStatus(transportState.getConnectorTransportId(), transportState);

        Mockito.verify(messagePersistenceService, Mockito.times(1)).mergeMessageWithDatabase(Mockito.eq(testMessage)); //, Mockito.times(1));
        Mockito.verify(messagePersistenceService, Mockito.times(1)).setDeliveredToGateway(Mockito.eq(testMessage)); //, Mockito.times(1));


    }


}