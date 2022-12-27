package eu.ecodex.dc5.flow.flows;

import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.ecodex.dc5.message.model.DC5BusinessMessageState;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@FlowTestAnnotation
@Log4j2
class ProcessIncomingBusinessMessageFlowTest {

    @Autowired
    ProcessIncomingBusinessMessageFlow processIncomingBusinessMessageFlow;

    @Autowired
    DC5MessageRepo messageRepo;

    @Autowired
    PlatformTransactionManager txManager;

    @MockBean
    DomibusConnectorSecurityToolkit securityToolkit;

    @MockBean
    SubmitToLinkService submitToLinkService;

    @Autowired
    MessageProcessManager messageProcessManager;

//    @Autowired
//    private ApplicationEvents applicationEvents;

    @Before
    public void before() {
        Mockito.when(securityToolkit.validateContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
        Mockito.when(securityToolkit.buildContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
    }

    @AfterEach
    public void afterEach() {
        CurrentBusinessDomain.clear();
    }

    public DomibusConnectorMessageId createMessage() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createIncomingEpoFormAMessage();
            dc5Message.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            dc5Message.setConnectorMessageId(DomibusConnectorMessageId.ofRandom());
            DC5Message m = messageRepo.save(dc5Message);
            return dc5Message.getConnectorMessageId();
        });
    }


    @Test
    public void testIncomingBusinessMessage() {
        try (MessageProcessManager.CloseableMessageProcess process = messageProcessManager.startProcess(); ){

            DomibusConnectorMessageId msgId = createMessage();

            TransactionTemplate txTemplate = new TransactionTemplate(txManager);

            txTemplate.execute(state -> {
                DC5Message msg = messageRepo.getByConnectorMessageId(msgId);
                CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
                processIncomingBusinessMessageFlow.processMessage(msg);
                return msg;
            });


            txTemplate.executeWithoutResult(s -> {
                DC5Message msg = messageRepo.getByConnectorMessageId(msgId);


                //TODO: verify!!!
                assertThat(msg.getMessageContent().getCurrentState().getState())
                        .as("Message state must be relayed")
                        .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.RELAYED);
            });


//        System.out.println("events: \n" + applicationEvents.stream()
//                .map(e -> e.toString())
//                .collect(Collectors.joining("\n")));
        }

    }

}