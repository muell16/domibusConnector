package eu.domibus.connector.link.impl.wsbackendplugin;


import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import eu.domibus.connector.ws.backend.webservice.GetMessageByIdRequest;
import eu.domibus.connector.ws.backend.webservice.ListPendingMessageIdsResponse;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Ebms;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.eu.domibus.connector.link.LinkTestContext;
import test.eu.domibus.connector.link.wsbackendplugin.ConnectorClientTestBackend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static test.eu.domibus.connector.link.LinkTestContext.SUBMIT_TO_CONNECTOR_QUEUE;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {LinkTestContext.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles({"wsbackendplugin-test", "plugin-test", "test", LINK_PLUGIN_PROFILE_NAME})
@Log4j2
@Disabled
public class WsBackendPluginTest {

    @LocalServerPort
    int localServerPort;

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
    public BlockingQueue<DC5Message> toConnectorSubmittedMessages;


    @Autowired
    DCMessagePersistenceService messagePersistenceServiceMock;

    @Autowired
    TransportStateService transportStateServiceMock;


    @Test
    public void testPluginIsLoaded() {
        List<LinkPlugin> availableLinkPlugins = linkManagerService.getAvailableLinkPlugins();
        assertThat(availableLinkPlugins).extracting(LinkPlugin::getPluginName).contains("wsbackendplugin");
    }

    @Test
    public void testPluginConfigs() {
        Collection<ActiveLinkPartner> activeLinkPartners = linkManagerService.getActiveLinkPartners();
        assertThat(activeLinkPartners).hasSize(2); //1 backend is configured...
    }

    //    @Test
    public void testSleep() throws InterruptedException {
        log.info("Port is: [{}]", localServerPort);
        Thread.sleep(100000);
    }


    @Test
    public void submitMessageToBackend() throws InterruptedException {
        String clientName = "bob";
        String connectorAddress = getServerAddress();

        ConnectorClientTestBackend connectorClientTestBackend = ConnectorClientTestBackend.startContext(clientName, connectorAddress, -1);
        DomibusConnectorBackendWebService domibusConnectorBackendWebService = connectorClientTestBackend.backendConnectorClientProxy();

        DomibusConnectorMessageType msg = TransitionCreator.createEpoMessage();
        msg.getMessageDetails().setEbmsMessageId("Ebms1234");
        domibusConnectorBackendWebService.submitMessage(msg);

        DC5Message poll = toConnectorSubmittedMessages.poll(20, TimeUnit.SECONDS);

        assertThat(poll).isNotNull();
        assertThat(poll)
                .extracting(DC5Message::getEbmsData)
                .extracting(DC5Ebms::getEbmsMessageId)
                .isEqualTo("Ebms1234");

    }

    /**
     * Test passive backend
     *  -) start a test connector_client
     *  -) connector client pulls message from (passive) connector backend
     */
    @Test
    public void testPassiveBackend_requestMessages() {

        String clientName = "bob";
        DomibusConnectorLinkPartner.LinkPartnerName backendName = new DomibusConnectorLinkPartner.LinkPartnerName("backend_bob");
        String connectorAddress = getServerAddress();

        DC5Message epoMessage1 = DomainEntityCreator.createEpoMessage();
        epoMessage1.setBackendLinkName("backend_bob");
        epoMessage1.setConnectorMessageId(new DomibusConnectorMessageId("con1"));
        epoMessage1.getEbmsData().setEbmsMessageId("ebms1");
        DomibusConnectorTransportStep step1 = new DomibusConnectorTransportStep();
        step1.setTransportedMessage(epoMessage1);


        DC5Message epoMessage2 = DomainEntityCreator.createEpoMessage();
        epoMessage2.setBackendLinkName("backend_bob");
        epoMessage2.setConnectorMessageId(new DomibusConnectorMessageId("con2"));
        epoMessage2.getEbmsData().setEbmsMessageId("ebms2");
        DomibusConnectorTransportStep step2 = new DomibusConnectorTransportStep();
        step2.setTransportedMessage(epoMessage2);

        //return the 2 message steps, when plugin asks for it
        Mockito.when(transportStateServiceMock.getPendingTransportsForLinkPartner(Mockito.eq(backendName)))
                .thenReturn(Stream.of(step1, step2).collect(Collectors.toList()));

        ConnectorClientTestBackend connectorClientTestBackend = ConnectorClientTestBackend.startContext(clientName, connectorAddress, -1);
        DomibusConnectorBackendWebService domibusConnectorBackendWebService = connectorClientTestBackend.backendConnectorClientProxy();

        EmptyRequestType emptyRequestType = new EmptyRequestType();
        DomibusConnectorMessagesType domibusConnectorMessagesType = domibusConnectorBackendWebService.requestMessages(emptyRequestType);

        List<DomibusConnectorMessageType> messages = domibusConnectorMessagesType.getMessages();

        assertThat(messages).hasSize(2);

        connectorClientTestBackend.shutdown();

    }

    /**
     * Test passive backend
     *  -) start a test connector_client
     *  -) connector client requests pending message ids
     *  -) downloads messages
     *  -) acks each message
     */
    @Test
    public void testPassiveBackend_requestMsgIds_and_Download() {

        String clientName = "bob";
        DomibusConnectorLinkPartner.LinkPartnerName backendName = new DomibusConnectorLinkPartner.LinkPartnerName("backend_bob");
        String connectorAddress = getServerAddress();

        DC5Message epoMessage1 = DomainEntityCreator.createEpoMessage();
        epoMessage1.setBackendLinkName("backend_bob");
        epoMessage1.setConnectorMessageId(new DomibusConnectorMessageId("con01"));
        epoMessage1.getEbmsData().setEbmsMessageId("ebms1");
        DomibusConnectorTransportStep step1 = new DomibusConnectorTransportStep();
        step1.setTransportedMessage(epoMessage1);
        TransportStateService.TransportId tid1 = new TransportStateService.TransportId("t1");
        step1.setTransportId(tid1);
        DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate update1 = new DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate();
        update1.setTransportState(TransportState.PENDING);
        step1.addTransportStatus(update1);


        DC5Message epoMessage2 = DomainEntityCreator.createEpoMessage();
        epoMessage2.setBackendLinkName("backend_bob");
        epoMessage2.setConnectorMessageId(new DomibusConnectorMessageId("con02"));
        epoMessage2.getEbmsData().setEbmsMessageId("ebms2");
        DomibusConnectorTransportStep step2 = new DomibusConnectorTransportStep();
        step2.setTransportedMessage(epoMessage2);
        TransportStateService.TransportId tid2 = new TransportStateService.TransportId("t2");
        step2.setTransportId(tid2);
        DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate update2 = new DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate();
        update2.setTransportState(TransportState.PENDING);
        step2.addTransportStatus(update2);

        //return the 2 pending message ids, when plugin asks for it
        Mockito.when(transportStateServiceMock.getPendingTransportsForLinkPartner(Mockito.eq(backendName)))
                .thenReturn(Stream.of(step1, step2).collect(Collectors.toList()));
        //return the corresponding step
        Mockito.when(transportStateServiceMock.getTransportStepById(Mockito.eq(tid1)))
                .thenReturn(Optional.of(step1));
        Mockito.when(transportStateServiceMock.getTransportStepById(Mockito.eq(tid2)))
                .thenReturn(Optional.of(step2));

        ConnectorClientTestBackend connectorClientTestBackend = ConnectorClientTestBackend.startContext(clientName, connectorAddress, -1);
        DomibusConnectorBackendWebService domibusConnectorBackendWebService = connectorClientTestBackend.backendConnectorClientProxy();

        ListPendingMessageIdsResponse listPendingMessageIdsResponseBeforeACK = domibusConnectorBackendWebService.listPendingMessageIds(new EmptyRequestType());

        List<DomibusConnectorMessageType> downloadedMessages = new ArrayList<>();
        for (String id :  listPendingMessageIdsResponseBeforeACK.getMessageTransportIds()) {
            GetMessageByIdRequest req = new GetMessageByIdRequest();
            req.setMessageTransportId(id);

            DomibusConnectorMessageType messageById = domibusConnectorBackendWebService.getMessageById(req);
            downloadedMessages.add(messageById);
        }

        Assertions.assertAll("",
                () -> assertThat(listPendingMessageIdsResponseBeforeACK.getMessageTransportIds())
                        .hasSize(2),
                () -> assertThat(downloadedMessages).hasSize(2)
        );




        connectorClientTestBackend.shutdown();

    }


    private String getServerAddress() {
        return "http://localhost:" + localServerPort + "/services/backend";
    }
}
