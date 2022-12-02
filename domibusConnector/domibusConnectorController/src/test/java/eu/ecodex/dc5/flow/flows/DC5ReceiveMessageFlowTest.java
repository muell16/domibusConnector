package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.ecodex.dc5.flow.steps.VerifyPModesStep;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.flow.api.DC5TransformToDomain;
import eu.ecodex.dc5.flow.api.TransformMessageException;
import eu.ecodex.dc5.message.model.DC5Action;
import eu.ecodex.dc5.message.model.DC5Message;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@FlowTestAnnotation
@Log4j2
class DC5ReceiveMessageFlowTest {

    @MockBean
    DCBusinessDomainManager businessDomainManager;

    @MockBean
    DomibusConnectorSecurityToolkit securityToolkit;    //TODO: use real IMPL!

    @MockBean
    VerifyPModesStep verifyPModesStep; //TODO: finish step!!!

//    @MockBean
//    SubmitToLinkService submitToLinkService; //TODO: remove mock!

    @Autowired
    ReceiveMessageFlow receiveMessageFlow;


    eu.ecodex.dc5.flow.api.DC5TransformToDomain<DomibusConnectorMessageType> DC5TransformToDomain = new DC5TransformToDomain<DomibusConnectorMessageType>() {

        @Override
        public DC5Message transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws TransformMessageException {
            return DomainEntityCreator.createOutgoingEpoFormAMessage();
        }
    };

    @Test
    public void test() {
        assertThat(receiveMessageFlow).isNotNull();
    }



    DC5TransformToDomain<DomibusConnectorMessageType> DC5TransformToDomainWithException = new DC5TransformToDomain<DomibusConnectorMessageType>() {
        @Override
        public DC5Message transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws TransformMessageException {
            throw new TransformMessageException("ERROR");
        }
    };

    DC5TransformToDomain<DomibusConnectorMessageType> DC5TransformToDomainWithJPAException = new DC5TransformToDomain<DomibusConnectorMessageType>() {
        @Override
        public DC5Message transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws TransformMessageException {
            DC5Message m = DomainEntityCreator.createOutgoingEpoFormAMessage();
//            m.getEbmsData().setAction(DC5Action.builder().action("").build());
            return m;
        }
    };



    @Test
    void receiveMessage() {
        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        assertThat(receiveMessageFlow).isNotNull();
        assertThat(epoMessage).isNotNull();
        assertThat(DC5TransformToDomain).isNotNull();

        ReceiveMessageFlow.ReceiveMessageFlowResult receiveMessageFlowResult = receiveMessageFlow.receiveMessage(epoMessage, DC5TransformToDomain);

        //TODO: check if event is created!
        //TODO: check db storage
        assertThat(receiveMessageFlowResult.getError()).isEmpty();

    }
//

    @Test
    void testReceiveWithException() {
        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        ReceiveMessageFlow.ReceiveMessageFlowResult receiveMessageFlowResult = receiveMessageFlow.receiveMessage(epoMessage, DC5TransformToDomainWithException);



        assertThat(receiveMessageFlowResult.getError()).map(Throwable::getMessage).contains("Failed step: [ConvertMessageStep]");
    }

    @Test
    void testReceiveWithJpaException() {
        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        ReceiveMessageFlow.ReceiveMessageFlowResult receiveMessageFlowResult = receiveMessageFlow.receiveMessage(epoMessage, DC5TransformToDomainWithJPAException);

        assertThat(receiveMessageFlowResult.getError()).map(Throwable::getMessage).contains("Failed step: [SaveMessage]");
        log.info(receiveMessageFlowResult);
    }

}