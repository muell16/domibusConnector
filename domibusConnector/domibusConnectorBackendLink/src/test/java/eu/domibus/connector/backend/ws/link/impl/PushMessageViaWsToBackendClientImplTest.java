package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.service.DomibusConnectorBackendInternalDeliverToController;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.impl.BigDataWithMessagePersistenceServiceImpl;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

public class PushMessageViaWsToBackendClientImplTest {

    PushMessageToBackendClient pushMessageToBackendService;

    @Mock
    BackendClientInfoPersistenceService backendClientInfoPersistenceService;
    @Mock
    DomibusConnectorBackendInternalDeliverToController backendSubmissionService;
    @Mock
    BackendClientWebServiceClientFactory webServiceClientFactory;
    @Mock
    BigDataWithMessagePersistenceServiceImpl bigDataMessageService;
    @Mock
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(webServiceClientFactory.createWsClient(any(DomibusConnectorBackendClientInfo.class)))
                .then((InvocationOnMock invocation) -> {
                    DomibusConnectorBackendClientInfo backendInfo = invocation.getArgument(0);
                    return createWebService(backendInfo);
                });

        //just pass message through bigDataMessageService
        Mockito.when(bigDataMessageService.loadAllBigFilesFromMessage(any(DomibusConnectorMessage.class)))
                .then((Answer<DomibusConnectorMessage>) invocation -> invocation.getArgument(0));

        Mockito.when(backendSubmissionService.processMessageBeforeDeliverToBackend(any(DomibusConnectorMessage.class)))
                .then(invoc -> invoc.getArgument(0));


        PushMessageViaWsToBackendClientImpl pushMessageOverWs = new PushMessageViaWsToBackendClientImpl();

        pushMessageOverWs.setBackendClientPersistenceService(backendClientInfoPersistenceService);
        pushMessageOverWs.setBackendClientWebServiceClientFactory(this.webServiceClientFactory);
//        pushMessageOverWs.setBigDataMessageService(this.bigDataMessageService);
        pushMessageOverWs.setMessagePersistenceService(this.messagePersistenceService);
        pushMessageOverWs.setDomibusConnectorBackendInternalDeliverToController(this.backendSubmissionService);

        pushMessageToBackendService = pushMessageOverWs;

    }

    List<DomibusConnectorMessageType> pushedMessages = new ArrayList<>();

    private DomibusConnectorBackendDeliveryWebService createWebService(DomibusConnectorBackendClientInfo backendInfo) {
        DomibusConnectorBackendDeliveryWebService webService = new DomibusConnectorBackendDeliveryWebService() {
            @Override
            public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
                pushedMessages.add(deliverMessageRequest);
                DomibsConnectorAcknowledgementType result = new DomibsConnectorAcknowledgementType();
                result.setResult(true);
                result.setMessageId(UUID.randomUUID().toString());
                result.setResultMessage("a result message");
                return result;
            }
        };
        return webService;
    }



    @Test
    public void testPushMessage() {

        DomibusConnectorBackendClientInfo backendClientInfo = new DomibusConnectorBackendClientInfo();
        backendClientInfo.setBackendName("bob");
        backendClientInfo.setBackendDescription("description");
        backendClientInfo.setBackendKeyAlias("bob");
        backendClientInfo.setBackendPushAddress("http://localhost:8888/services/push");

        DomibusConnectorBackendMessage backendMessage = new DomibusConnectorBackendMessage();
        backendMessage.setBackendClientInfo(backendClientInfo);
        backendMessage.setDomibusConnectorMessage(DomainEntityCreator.createMessage());

        pushMessageToBackendService.push(backendMessage);
    }

}