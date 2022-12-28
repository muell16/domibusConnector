package eu.domibus.connector.controller.test.util;

import eu.ecodex.dc5.message.model.*;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.domain.testutil.LargeFileReferenceGetSetBased;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class ConnectorControllerTestDomainCreator {

    public static DC5Action createActionForm_A() {
        DC5Action a = DC5Action.builder().action("Form_A").build();
//        DomibusConnectorAction a = new DomibusConnectorAction("Form_A", true);
        return a;                
    }
    
    public static DC5Action createActionRelayREMMDAcceptanceRejection() {
        DC5Action a = DC5Action.builder().action("RelayREMMDAcceptanceRejection").build();
//        DomibusConnectorAction a = new DomibusConnectorAction("RelayREMMDAcceptanceRejection", true); 
        return a;
    }
    

    
    public static DC5Confirmation createMessageDeliveryConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("EVIDENCE1_DELIVERY".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.DELIVERY);
        return confirmation;
    }
    
    public static DC5Confirmation createMessageNonDeliveryConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("EVIDENCE1_NON_DELIVERY".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.NON_DELIVERY);
        return confirmation;
    }

    public static DC5MessageAttachment createSimpleMessageAttachment() {
        LargeFileReferenceGetSetBased inMemory = new LargeFileReferenceGetSetBased();
        inMemory.setBytes("attachment".getBytes());

        return DC5MessageAttachment.builder()
                .attachment(inMemory)
                .identifier("identifier")
                .name("name")
                .mimeType("application/garbage")
                .build();
    }
    

    public static DC5Message createSimpleTestMessage() {
        
        DC5Ebms messageDetails = new DC5Ebms();
        messageDetails.setConversationId("conversation1");
        messageDetails.setEbmsMessageId(EbmsMessageId.ofString("ebms1"));
        
        DC5MessageContent messageContent = new DC5MessageContent();
//        DC5Message msg = new DC5Message(messageDetails, messageContent);
        //msg.setDbMessageId(78L);
        //msg.getMessageDetails().
//        return msg;
        return null;
    }
    

    /**
     * creates a error message with
     * #createSimpleDomibusConnectorMessage as message
     * "error detail message" as details
     * "error message" as text
     * "error source" as error source
     * @return the MessageError
     */
    public static DomibusConnectorMessageError createMessageError() {
        return DomibusConnectorMessageErrorBuilder.createBuilder()
                .setDetails("error detail message")
                .setText("error message")
                .setSource("error source")
                .build();
    }


}
