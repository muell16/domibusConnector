package eu.ecodex.dc5.message;

import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.ecodex.dc5.flow.flows.FlowTestAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@FlowTestAnnotation
class FindBusinessMessageByMsgIdTest {
    @Autowired
    FindBusinessMessageByMsgId findBusinessMessageByMsgId;

    @MockBean
    SubmitToLinkService submitToLinkService;
    @Test
    void findBusinessMsgByConversationId() {

        String conversationId = "testConvId";

        findBusinessMessageByMsgId.findBusinessMsgByConversationId(conversationId);
    }
}