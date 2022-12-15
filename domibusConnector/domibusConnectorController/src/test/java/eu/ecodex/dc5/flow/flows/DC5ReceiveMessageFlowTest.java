package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.flow.steps.VerifyPModesStep;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.flow.api.DC5TransformToDomain;
import eu.ecodex.dc5.flow.api.TransformMessageException;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
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

    @MockBean
    SubmitToLinkService submitToLinkService; //TODO: remove mock!

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

    public static final long msgId = -100L;

    DC5TransformToDomain<DomibusConnectorMessageType> DC5TransformToDomainWithJPAException = new DC5TransformToDomain<DomibusConnectorMessageType>() {
        @Override
        public DC5Message transform(DomibusConnectorMessageType msg, DC5MsgProcess msgProcess) throws TransformMessageException {
            DC5Message m = DomainEntityCreator.createOutgoingEpoFormAMessage();
            m.setId(msgId);
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
        Assertions.setMaxStackTraceElementsDisplayed(20000);
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
    @Disabled("create somehow a DB exception...")
    void testReceiveWithJpaException() {

        DomibusConnectorMessageType epoMessage = TransitionCreator.createEpoMessage();

        ReceiveMessageFlow.ReceiveMessageFlowResult receiveMessageFlowResult = receiveMessageFlow.receiveMessage(epoMessage, DC5TransformToDomainWithJPAException);

        log.info(receiveMessageFlowResult.getError().map(ExceptionUtils::getStackTrace));
        assertThat(receiveMessageFlowResult.getError()).map(Throwable::getMessage).contains("Failed step: [SaveMessage]");

    }

}