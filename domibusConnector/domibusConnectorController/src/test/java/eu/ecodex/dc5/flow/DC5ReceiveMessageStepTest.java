package eu.ecodex.dc5.flow;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.spring.MessageIdGeneratorConfigurer;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.ecodex.dc.core.configuration.DC5JpaConfiguration;
import eu.ecodex.dc.core.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static eu.ecodex.dc.core.model.DC5BusinessDocumentStatesEnum.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@SpringBootTest()
@ActiveProfiles("small")
class DC5ReceiveMessageStepTest {

    @SpringBootApplication
    @Import({MessageIdGeneratorConfigurer.class, DC5ReceiveMessageStep.class, DC5JpaConfiguration.class})
    public static class TestContext {

    }

    @Autowired
    DC5ReceiveMessageStep receiveMessageStep;

    DC5ReceiveMessageStep.Transformer<DomibusConnectorMessageType, DC5Msg> transformer = new DC5ReceiveMessageStep.Transformer<DomibusConnectorMessageType, DC5Msg>() {
        @Override
        public DC5Msg transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws DC5ReceiveMessageStep.TransformMessageException {
            DC5Msg dbMsg = new DC5Msg();
            DC5ContentBusinessDocument businessContent = new DC5ContentBusinessDocument();

            DC5ContentEcodex ecxContent = new DC5ContentEcodex();
            businessContent.setEcodexContent(ecxContent);
            businessContent.setCurrentState(new DC5BusinessDocumentState(CREATED));
            dbMsg.setContent(businessContent);

            DC5Ebms dc5Ebms = new DC5Ebms();

            DC5EcxAddress toParty = new DC5EcxAddress();
            DC5Party p = new DC5Party();
            p.setPartyId("id1");
            p.setPartyIdType("type1");
            toParty.setParty(p);
            toParty.setEcxAddress("finalRecipient");
            dc5Ebms.setReceiver(toParty);


            DC5EcxAddress fromParty = new DC5EcxAddress();
            DC5Party p1 = new DC5Party();
            p1.setPartyId("id1");
            p1.setPartyIdType("type1");
            fromParty.setParty(p1);
            fromParty.setEcxAddress("originalSender");
            dc5Ebms.setSender(fromParty);

            dc5Ebms.setService(new DC5Service("service", "serviceType"));
            dc5Ebms.setAction(new DC5Action("action"));
            dbMsg.setEbmsSegment(dc5Ebms);

            return dbMsg;
        }
    };

    DC5ReceiveMessageStep.Transformer<DomibusConnectorMessageType, DC5Msg> transformerWithException = new DC5ReceiveMessageStep.Transformer<DomibusConnectorMessageType, DC5Msg>() {
        @Override
        public DC5Msg transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws DC5ReceiveMessageStep.TransformMessageException {
            throw new DC5ReceiveMessageStep.TransformMessageException("ERROR");
        }
    };

    @Test
    void receiveMessage() {
        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        DC5ProcessStep dc5ProcessStep = receiveMessageStep.receiveMessage(epoMessage, transformer);
        assertThat(dc5ProcessStep.getError()).isNull();

    }

    @Test
    void testReceiveWithException() {
        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        DC5ProcessStep dc5ProcessStep = receiveMessageStep.receiveMessage(epoMessage, transformerWithException);
        assertThat(dc5ProcessStep.getError()).isEqualTo("ERROR");
    }

}