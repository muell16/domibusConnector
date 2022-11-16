package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Ebms;
import eu.ecodex.dc5.message.model.DomibusConnectorParty;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 *
 *
 */
public class DomainModelHelperTest {
    
    public DomainModelHelperTest() {
    }

    @Test
    public void testIsEvidenceMessage() {
        DC5Message createSimpleTestMessage = DomainEntityCreator.createSimpleTestMessage();
        boolean isEvidenceMessage = DomainModelHelper.isEvidenceMessage(createSimpleTestMessage);
        assertThat(isEvidenceMessage).isFalse();
    }

    @Test
    public void testIsEvidenceTriggerMessage_shouldBeFalse() {
        DC5Message createSimpleTestMessage = DomainEntityCreator.createEvidenceNonDeliveryMessage();
        createSimpleTestMessage.getTransportedMessageConfirmations()
                .get(0).setEvidence("evidence".getBytes(StandardCharsets.UTF_8));
        boolean isEvidenceMessage = DomainModelHelper.isEvidenceTriggerMessage(createSimpleTestMessage);
        assertThat(isEvidenceMessage).isFalse();
    }

    @Test
    public void testIsEvidenceTriggerMessage_shouldBeTrue() {
        DC5Message createSimpleTestMessage = DomainEntityCreator.createEvidenceNonDeliveryMessage();
        createSimpleTestMessage.getTransportedMessageConfirmations()
                .get(0).setEvidence(null);
        boolean isEvidenceMessage = DomainModelHelper.isEvidenceTriggerMessage(createSimpleTestMessage);
        assertThat(isEvidenceMessage).isTrue();
    }

    @Test
    public void testIsEvidenceTriggerMessageEmptyByteArray_shouldBeTrue() {
        DC5Message createSimpleTestMessage = DomainEntityCreator.createEvidenceNonDeliveryMessage();
        createSimpleTestMessage.getTransportedMessageConfirmations()
                .get(0).setEvidence(new byte[0]);
        boolean isEvidenceMessage = DomainModelHelper.isEvidenceTriggerMessage(createSimpleTestMessage);
        assertThat(isEvidenceMessage).isTrue();
    }

    @Test
    public void testIsEvidenceTriggerMessage_businessMsg_shouldBeFalse() {
        DC5Message createSimpleTestMessage = DomainEntityCreator.createSimpleTestMessage();
        boolean isEvidenceMessage = DomainModelHelper.isEvidenceTriggerMessage(createSimpleTestMessage);
        assertThat(isEvidenceMessage).isFalse();
    }

    @Test
    void switchMessageDirection() {
//        DC5Message origMsg = DomainEntityCreator.createEpoMessage();
//        origMsg.getEbmsData().setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
//        DC5Ebms swMsgDetails = DomainModelHelper.switchMessageDirection(origMsg.getEbmsData());
//
//        swMsgDetails.getFromParty().setRole("fromRole");
//        swMsgDetails.getToParty().setRole("toRole");
//
//        DC5Ebms origMsgDetails = origMsg.getEbmsData();
//
//        //assert direction is switched
//        assertThat(swMsgDetails.getDirection()).isEqualTo(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//        //assert that party is switched
//        assertThat(swMsgDetails.getFromParty()).isNotNull();
//        assertThat(swMsgDetails.getFromParty()).isEqualToComparingOnlyGivenFields(origMsgDetails.getToParty(), "partyId", "partyIdType");
//        assertThat(swMsgDetails.getFromParty().getRoleType())
//                .as("party role type of from party is always initiator")
//                .isEqualTo(DomibusConnectorParty.PartyRoleType.INITIATOR);
//        assertThat(swMsgDetails.getFromParty().getRole())
//                .as("party role of from party is always fromRole")
//                .isEqualTo("fromRole");
//        assertThat(swMsgDetails.getToParty()).isNotNull();
//        assertThat(swMsgDetails.getToParty())
//                .isEqualToComparingOnlyGivenFields(origMsgDetails.getFromParty(), "partyId", "partyIdType");
//        assertThat(swMsgDetails.getToParty().getRoleType())
//                .as("party role type of to party is always responder")
//                .isEqualTo(DomibusConnectorParty.PartyRoleType.RESPONDER);
//        assertThat(swMsgDetails.getToParty().getRole())
//                .as("party role of to party is always toRole")
//                .isEqualTo("toRole");
//
//        //assertThat final recipient/original sender is switched
//        assertThat(swMsgDetails.getFinalRecipient()).isEqualTo(origMsgDetails.getOriginalSender());
//        assertThat(swMsgDetails.getOriginalSender()).isEqualTo(origMsgDetails.getFinalRecipient());

        Assertions.fail();

    }
}
