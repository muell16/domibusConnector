package eu.ecodex.dc5.flow.flows;

import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

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

    @Before
    public void before() {
        Mockito.when(securityToolkit.validateContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
        Mockito.when(securityToolkit.buildContainer(Mockito.any())).thenAnswer(a ->a.getArgument(0));
    }

    public DC5Message createMessage() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createIncomingEpoFormAMessage();
            dc5Message.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            DC5Message m = messageRepo.save(dc5Message);
            return m;
        });
    }


    @Test
    public void testIncomingBusinessMessage() {
        messageProcessManager.startProcess();

        Long msgId = createMessage().getId();

        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

        txTemplate.execute(state -> {
            DC5Message msg = messageRepo.getById(msgId);
            CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            processIncomingBusinessMessageFlow.processMessage(msg);
            return msg;
        });


    }

}