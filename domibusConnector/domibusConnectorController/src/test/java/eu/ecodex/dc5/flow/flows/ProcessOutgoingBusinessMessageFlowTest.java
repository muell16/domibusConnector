package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.message.model.DC5BusinessMessageState;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
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
import static org.junit.jupiter.api.Assertions.*;


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


    @Before
    public void before() {
        Mockito.when(securityToolkit.validateContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
        Mockito.when(securityToolkit.buildContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
    }


    public DC5Message createMessage() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createOutgoingEpoFormAMessage();
            dc5Message.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            DC5Message m = messageRepo.save(dc5Message);
            return m;
        });
    }

    @Test
    void processMessage() {

        messageProcessManager.startProcess();

        Long msgId = createMessage().getId();

        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

        txTemplate.execute(state -> {
            DC5Message msg = messageRepo.getById(msgId);
            CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            processOutgoingBusinessMessageFlow.processMessage(msg);
            return msg;
        });


        txTemplate.executeWithoutResult(s -> {
            DC5Message msg = messageRepo.getById(msgId);


            //TODO: verify!!!
            assertThat(msg.getMessageContent().getCurrentState().getState())
                    .as("Message state must be created")
                    .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.CREATED);
        });
    }

    @Test
    void processMessageWithSecurityError() {

        Mockito.clearInvocations(securityToolkit);
        Mockito.when(securityToolkit.buildContainer(Mockito.any())).thenThrow(new DomibusConnectorSecurityException());

        messageProcessManager.startProcess();

        Long msgId = createMessage().getId();

        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

        txTemplate.execute(state -> {
            DC5Message msg = messageRepo.getById(msgId);
            CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            processOutgoingBusinessMessageFlow.processMessage(msg);
            return msg;
        });


        txTemplate.executeWithoutResult(s -> {
            DC5Message msg = messageRepo.getById(msgId);


            //TODO: verify!!!
            assertThat(msg.getMessageContent().getCurrentState().getState())
                    .as("Message state must be rejected")
                    .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.REJECTED);
        });


    }



}