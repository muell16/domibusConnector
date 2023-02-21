package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@FlowTestAnnotation
@Log4j2
class ProcessOutgoingBusinessMessageFlowTest {

    @Autowired
    ProcessOutgoingBusinessMessageFlow processOutgoingBusinessMessageFlow;

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
    private TransactionTemplate txTemplate;


    @BeforeEach
    public void before() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        this.txTemplate = new TransactionTemplate(txManager, definition);

        Mockito.when(securityToolkit.validateContainer(Mockito.any())).thenAnswer(a -> {
            DC5Message msg = a.getArgument(0);
            msg.getMessageContent().setBusinessContent(DC5BackendContent.builder().build());
            return msg;
        });


    }

    @AfterEach
    public void afterEach() {
        CurrentBusinessDomain.clear();
    }


    public DC5MessageId createMessage() {

        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createOutgoingEpoFormAMessage();
            dc5Message.setMessageLaneId(DC5BusinessDomain.getDefaultBusinessDomainId());
            DC5Message m = messageRepo.save(dc5Message);
            return dc5Message.getConnectorMessageId();
        });
    }

    @Test
    void processMessage() {
        Mockito.when(securityToolkit.buildContainer(Mockito.any())).thenAnswer(a -> {
            DC5Message msg = a.getArgument(0);
            msg.getMessageContent().setEcodexContent(DC5EcodexContent.builder().build());
            return msg;
        });

        DC5MessageId msgId = createMessage();


        try (MessageProcessManager.CloseableMessageProcess p = messageProcessManager.startProcess()) {

//        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

            txTemplate.execute(state -> {
                DC5Message msg = messageRepo.getByConnectorMessageId(msgId);
                CurrentBusinessDomain.setCurrentBusinessDomain(DC5BusinessDomain.getDefaultBusinessDomainId());
                processOutgoingBusinessMessageFlow.processMessage(msg);
                return msg;
            });


            txTemplate.executeWithoutResult(s -> {
                DC5Message msg = messageRepo.getByConnectorMessageId(msgId);


                //TODO: verify!!!
                assertThat(msg.getMessageContent().getCurrentState().getState())
                        .as("Message state must be created")
                        .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.CREATED);
            });
        }
    }

    @Test
    void processMessageWithSecurityError() {
        DC5MessageId msgId = createMessage();

        Mockito.clearInvocations(securityToolkit);
        Mockito.when(securityToolkit.buildContainer(Mockito.any())).thenThrow(new DomibusConnectorSecurityException());

        try (MessageProcessManager.CloseableMessageProcess c = messageProcessManager.startProcess();) {

            txTemplate.execute(state -> {
                DC5Message msg = messageRepo.getByConnectorMessageId(msgId);
                CurrentBusinessDomain.setCurrentBusinessDomain(DC5BusinessDomain.getDefaultBusinessDomainId());
                processOutgoingBusinessMessageFlow.processMessage(msg);
                return msg;
            });


            txTemplate.executeWithoutResult(s -> {
                DC5Message msg = messageRepo.getByConnectorMessageId(msgId);


                //TODO: verify!!!
                assertThat(msg.getMessageContent().getCurrentState().getState())
                        .as("Message state must be rejected")
                        .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.REJECTED);
            });
        }


    }


}