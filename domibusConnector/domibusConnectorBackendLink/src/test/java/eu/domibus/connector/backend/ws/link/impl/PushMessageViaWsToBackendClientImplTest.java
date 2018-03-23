package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.impl.BigDataWithMessagePersistenceServiceImpl;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;

public class PushMessageViaWsToBackendClientImplTest {

    PushMessageViaWsToBackendClientImpl pushMessageOverWs;

    BackendClientInfoPersistenceService backendClientInfoPersistenceService;

    BackendClientWebServiceClientFactory webServiceClientFactory;

    BigDataWithMessagePersistenceServiceImpl bigDataMessageService;

    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Before
    public void setUp() {
        this.backendClientInfoPersistenceService = Mockito.mock(BackendClientInfoPersistenceService.class);

        this.webServiceClientFactory = Mockito.mock(BackendClientWebServiceClientFactory.class);
        Mockito.when(webServiceClientFactory.createWsClient(any(DomibusConnectorBackendClientInfo.class)))
                .then((InvocationOnMock invocation) -> {
                    DomibusConnectorBackendClientInfo backendInfo = invocation.getArgumentAt(0, DomibusConnectorBackendClientInfo.class);
                    return createWebService(backendInfo);
                });

        //just pass message through bigDataMessageService
        this.bigDataMessageService = Mockito.mock(BigDataWithMessagePersistenceServiceImpl.class);
        Mockito.when(bigDataMessageService.loadAllBigFilesFromMessage(any(DomibusConnectorMessage.class)))
                .then(new Answer<DomibusConnectorMessage>() {
                    @Override
                    public DomibusConnectorMessage answer(InvocationOnMock invocation) throws Throwable {
                        return invocation.getArgumentAt(0, DomibusConnectorMessage.class);
                    }
                });

        this.messagePersistenceService = Mockito.mock(DomibusConnectorMessagePersistenceService.class);
        this.pushMessageOverWs = new PushMessageViaWsToBackendClientImpl();

        pushMessageOverWs.setBackendClientPersistenceService(backendClientInfoPersistenceService);
        pushMessageOverWs.setBackendClientWebServiceClientFactory(this.webServiceClientFactory);
        pushMessageOverWs.setBigDataMessageService(this.bigDataMessageService);
        pushMessageOverWs.setMessagePersistenceService(this.messagePersistenceService);
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

        pushMessageOverWs.push(backendMessage);
    }

}