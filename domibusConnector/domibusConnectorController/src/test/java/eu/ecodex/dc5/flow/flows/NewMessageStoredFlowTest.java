package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.firststartup.CreateDefaultBusinessDomainOnFirstStart;
import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@FlowTestAnnotation
@Log4j2
@Import({CreateDefaultBusinessDomainOnFirstStart.class})
class NewMessageStoredFlowTest {

    @Autowired
    NewMessageStoredFlow newMessageStoredFlow;

    @Autowired
    DC5MessageRepo messageRepo;

    @Autowired
    PlatformTransactionManager txManager;


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

        newMessageStoredFlow.handleNewMessageStoredEvent(event);

    }
}