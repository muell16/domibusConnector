package eu.ecodex.dc5.flow;

import eu.ecodex.dc5.message.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.ecodex.dc5.core.model.*;
import eu.ecodex.dc5.flow.api.DC5TransformToDomain;
import eu.ecodex.dc5.flow.api.TransformMessageException;
import eu.ecodex.dc5.flow.flows.ReceiveMessageFlow;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(classes = eu.ecodex.dc5.DC5FlowModule.class)
@ActiveProfiles("small")
@Disabled
class DC5ReceiveMessageFlowTest {

    @Autowired
    ReceiveMessageFlow receiveMessageFlow;


    eu.ecodex.dc5.flow.api.DC5TransformToDomain<DomibusConnectorMessageType> DC5TransformToDomain = new DC5TransformToDomain<DomibusConnectorMessageType>() {

        @Override
        public DomibusConnectorMessage transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws TransformMessageException {
            return DomainEntityCreator.createEpoMessage();
        }
    };

    @Test
    public void test() {
        assertThat(receiveMessageFlow).isNotNull();
    }



    DC5TransformToDomain<DomibusConnectorMessageType> DC5TransformToDomainWithException = new DC5TransformToDomain<DomibusConnectorMessageType>() {
        @Override
        public DomibusConnectorMessage transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws TransformMessageException {
            throw new TransformMessageException("ERROR");
        }
    };



    @Test
    void receiveMessage() {
        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        ReceiveMessageFlow.ReceiveMessageFlowResult receiveMessageFlowResult = receiveMessageFlow.receiveMessage(epoMessage, DC5TransformToDomain);

        //TODO: check if event is created!
        //TODO: check db storage
        assertThat(receiveMessageFlowResult.getError()).isNull();

    }
//

    @Test
    void testReceiveWithException() {
        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        ReceiveMessageFlow.ReceiveMessageFlowResult receiveMessageFlowResult = receiveMessageFlow.receiveMessage(epoMessage, DC5TransformToDomainWithException);

        assertThat(receiveMessageFlowResult.getError()).isEqualTo("ERROR");
    }

}