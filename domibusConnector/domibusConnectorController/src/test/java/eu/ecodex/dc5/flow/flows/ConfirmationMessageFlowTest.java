package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.message.model.DC5BusinessMessageState;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5MessageId;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@FlowTestAnnotation
@Log4j2
class ConfirmationMessageFlowTest {

    @Autowired
    ConfirmationMessageFlow confirmationMessageFlow;

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

    @AfterEach
    public void afterEachTest() {
        CurrentBusinessDomain.clear();
    }


    public DC5Message createMessage() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createAlreadyOutgoneEpoFormAMessage();
            dc5Message.setMessageLaneId(DC5BusinessDomain.getDefaultBusinessDomainId());
            dc5Message.setConnectorMessageId(DC5MessageId.ofRandom());
            DC5Message m = messageRepo.save(dc5Message);
            return dc5Message;
        });
    }


    public DC5MessageId createRelayRemmdRejectMessage(DC5Message refTo) {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createRelayRemmdRejectEvidenceForMessage(refTo);
            dc5Message.setMessageLaneId(DC5BusinessDomain.getDefaultBusinessDomainId());
            dc5Message.setConnectorMessageId(DC5MessageId.ofRandom());
            DC5Message m = messageRepo.save(dc5Message);
            return m.getConnectorMessageId();
        });
    }
    public DC5MessageId createRelayRemmdAcceptMessage(DC5Message refTo) {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        return txTemplate.execute(state -> {
            DC5Message dc5Message = DomainEntityCreator.createRelayRemmdAcceptanceEvidenceForMessage(refTo);
            dc5Message.setMessageLaneId(DC5BusinessDomain.getDefaultBusinessDomainId());
            dc5Message.setConnectorMessageId(DC5MessageId.ofRandom());
            DC5Message m = messageRepo.save(dc5Message);
            return dc5Message.getConnectorMessageId();
        });
    }

    @Test
    public void testRelayRemmdAcceptanceRcv() {
        DC5Message message = createMessage();

        DC5MessageId msgId = createRelayRemmdAcceptMessage(message);

        messageProcessManager.startProcess();

        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

        txTemplate.execute(state -> {
            DC5Message msg = messageRepo.getByConnectorMessageId(msgId);
            CurrentBusinessDomain.setCurrentBusinessDomain(DC5BusinessDomain.getDefaultBusinessDomainId());
            confirmationMessageFlow.processMessage(msg);
            return msg;
        });

        txTemplate.executeWithoutResult(s -> {
            DC5Message businessMsg = messageRepo.getByConnectorMessageId(message.getConnectorMessageId());

            assertThat(businessMsg.getMessageContent().getCurrentState().getState())
                    .as("Message state must be relayed")
                    .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.RELAYED);

        });

    }

    @Test
    public void testRelayRemmdRejectRcv() {
        DC5Message message = createMessage();

        DC5MessageId remmdReject = createRelayRemmdRejectMessage(message);

        messageProcessManager.startProcess();

        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

        txTemplate.execute(state -> {
            DC5Message msg = messageRepo.getByConnectorMessageId(remmdReject);
            CurrentBusinessDomain.setCurrentBusinessDomain(DC5BusinessDomain.getDefaultBusinessDomainId());
            confirmationMessageFlow.processMessage(msg);
            return msg;
        });

        txTemplate.executeWithoutResult(s -> {
            DC5Message businessMsg = messageRepo.getByConnectorMessageId(message.getConnectorMessageId());

            assertThat(businessMsg.getMessageContent().getCurrentState().getState())
                    .as("Message state must be reject")
                    .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.REJECTED);

        });

    }

}