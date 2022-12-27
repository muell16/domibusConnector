package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.ecodex.dc5.flow.steps.VerifyPModesStep;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.firststartup.CreateDefaultBusinessDomainOnFirstStart;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@FlowTestAnnotation
@Log4j2
class NewMessageStoredFlowTest {

    @Autowired
    MessageProcessManager messageProcessManager;

    @Autowired
    NewMessageStoredFlow newMessageStoredFlow;

    @Autowired
    DC5MessageRepo messageRepo;

    @Autowired
    PlatformTransactionManager txManager;

    @MockBean
    DomibusConnectorSecurityToolkit securityToolkit;

    @MockBean
    DomibusConnectorMessageIdGenerator messageIdGenerator;

    @MockBean
    SubmitToLinkService submitToLinkService;

    @MockBean
    VerifyPModesStep verifyPModesStep;


    @Before
    public void before() {
        Mockito.when(securityToolkit.validateContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
        Mockito.when(securityToolkit.buildContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
        Mockito.when(messageIdGenerator.generateDomibusConnectorMessageId()).thenReturn(DomibusConnectorMessageId.ofRandom());
    }

    public DC5Message createMessage() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createOutgoingEpoFormAMessage();
            DC5Message m = messageRepo.save(dc5Message);
            return m;
        });
    }


    @Test
    void handleNewMessageStoredEvent() {

        DC5Message m = createMessage();
        NewMessageStoredEvent event = NewMessageStoredEvent.of(m.getId());
        try (MessageProcessManager.CloseableMessageProcess closeableMessageProcess = messageProcessManager.startProcess();) {
            newMessageStoredFlow.handleNewMessageStoredEvent(event);
        }
    }
}