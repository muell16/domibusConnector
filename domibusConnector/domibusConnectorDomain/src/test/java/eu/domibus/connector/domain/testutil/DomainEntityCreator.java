
package eu.domibus.connector.domain.testutil;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageAttachmentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.domain.transformer.testutil.DataHandlerCreator;
import eu.domibus.connector.domain.transformer.util.DomibusConnectorBigDataReferenceDataHandlerBacked;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomainEntityCreator {

    public static DomibusConnectorParty createPartyAT() {
        DomibusConnectorParty p = new DomibusConnectorParty("AT", "urn:oasis:names:tc:ebcore:partyid-type:iso3166-1", "GW");
        return p;
    }
    
    public static DomibusConnectorParty createPartyDE() {
        DomibusConnectorParty p = new DomibusConnectorParty("DE", "urn:oasis:names:tc:ebcore:partyid-type:iso3166-1", "GW");        
        return p;
    }
    
    public static DomibusConnectorAction createActionForm_A() {
        DomibusConnectorAction a = new DomibusConnectorAction("Form_A", true);        
        return a;                
    }
    
    public static DomibusConnectorAction createActionRelayREMMDAcceptanceRejection() {
        DomibusConnectorAction a = new DomibusConnectorAction("RelayREMMDAcceptanceRejection", true);        
        return a;
    }
    
    public static DomibusConnectorService createServiceEPO() {
        DomibusConnectorService s = new DomibusConnectorService("EPO", "urn:e-codex:services:");
        return s;
    }
    
    public static DomibusConnectorMessageConfirmation createMessageDeliveryConfirmation() {
        DomibusConnectorMessageConfirmation confirmation = new DomibusConnectorMessageConfirmation();
        confirmation.setEvidence("<EVIDENCE1_DELIVERY/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.DELIVERY);
        return confirmation;
    }
    
    public static DomibusConnectorMessageConfirmation createMessageNonDeliveryConfirmation() {
        DomibusConnectorMessageConfirmation confirmation = new DomibusConnectorMessageConfirmation();        
        confirmation.setEvidence("<EVIDENCE1_NON_DELIVERY/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.NON_DELIVERY);
        return confirmation;
    }
    
    public static DomibusConnectorMessageAttachment createSimpleMessageAttachment() {
        DomibusConnectorMessageAttachment attachment = 
                new DomibusConnectorMessageAttachment(connectorBigDataReferenceFromDataSource("attachment"), "identifier");

        attachment.setName("name");
        attachment.setMimeType("application/garbage");
        return attachment;
    }

    public static DomibusConnectorBigDataReference connectorBigDataReferenceFromDataSource(String input) {        
        DomibusConnectorBigDataReferenceGetSetBased reference = 
                new DomibusConnectorBigDataReferenceGetSetBased();
        
        reference.setBytes(input.getBytes());
        reference.setReadable(true);
        reference.setInputStream(new ByteArrayInputStream(input.getBytes()));
        
        return reference;
    }
    
    public static DomibusConnectorMessage createEvidenceNonDeliveryMessage() {
        DomibusConnectorMessageDetails messageDetails = createDomibusConnectorMessageDetails();
        DomibusConnectorMessageConfirmation nonDeliveryConfirmation = createMessageNonDeliveryConfirmation();
        
        DomibusConnectorMessage msg = DomibusConnectorMessageBuilder.createBuilder()
                .setConnectorMessageId("id1")
                .setMessageDetails(messageDetails)
                .addConfirmation(nonDeliveryConfirmation)
                .build();
        
        return msg;
    }

    public static DomibusConnectorMessage createSimpleTestMessage() {
        
        DomibusConnectorMessageDetails messageDetails = new DomibusConnectorMessageDetails();
        messageDetails.setConversationId("conversation1");
        messageDetails.setEbmsMessageId("ebms1");
        
        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
        DomibusConnectorMessage msg = new DomibusConnectorMessage(messageDetails, messageContent);
        //msg.setDbMessageId(78L);
        //msg.getMessageDetails().
        return msg;
    }
    
    public static DomibusConnectorMessage createMessage() {
        DomibusConnectorMessageDetails messageDetails = createDomibusConnectorMessageDetails();
        
        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
        messageContent.setXmlContent("<xmlContent/>".getBytes());
        
        DetachedSignature detachedSignature = new DetachedSignature("detachedSignature".getBytes(), "signaturename", DetachedSignatureMimeType.BINARY);
                
        DomibusConnectorMessageDocument messageDocument = new DomibusConnectorMessageDocument(connectorBigDataReferenceFromDataSource("documentbytes"), "Document1.pdf", detachedSignature);
                        
        messageContent.setDocument(messageDocument);
        
        DomibusConnectorMessage msg = new DomibusConnectorMessage(messageDetails, messageContent);
        msg.addConfirmation(createMessageDeliveryConfirmation());
        msg.addAttachment(createSimpleMessageAttachment());
        msg.addError(createMessageError());
        return msg;
    }

    public static DomibusConnectorMessage createEpoMessage() {
        DomibusConnectorMessageDetails messageDetails = createDomibusConnectorMessageDetails();

        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
        messageContent.setXmlContent("<xmlContent/>".getBytes());

        DetachedSignature detachedSignature = new DetachedSignature("detachedSignature".getBytes(), "signaturename", DetachedSignatureMimeType.BINARY);

        DomibusConnectorMessageDocument messageDocument = new DomibusConnectorMessageDocument(connectorBigDataReferenceFromDataSource("documentbytes"), "Document1.pdf", detachedSignature);
        messageContent.setDocument(messageDocument);

        DomibusConnectorMessage message = DomibusConnectorMessageBuilder.createBuilder()
                .addAttachment(createSimpleMessageAttachment())
                .setMessageDetails(messageDetails)
                .setMessageContent(messageContent)
                .setConnectorMessageId("MSG1")
                .build();

        return message;
    }
    
    public static DomibusConnectorMessageDetails createDomibusConnectorMessageDetails() {
        DomibusConnectorMessageDetails messageDetails = new DomibusConnectorMessageDetails();
        messageDetails.setConversationId("conversation1");
        messageDetails.setEbmsMessageId("ebms1");
        messageDetails.setBackendMessageId("national1");
        messageDetails.setFinalRecipient("finalRecipient");
        messageDetails.setOriginalSender("originalSender");
        messageDetails.setRefToMessageId("refToMessageId");
        
        messageDetails.setAction(createActionForm_A());
        messageDetails.setService(createServiceEPO());
        messageDetails.setToParty(createPartyAT());
        messageDetails.setFromParty(createPartyDE());
        
        return messageDetails;
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
        DomibusConnectorMessageError error = new DomibusConnectorMessageError("error message", "error detail message", "error source");     
        return error;
    }

    public static DomibusConnectorMessageAttachment createMessageAttachment() {
                
        return DomibusConnectorMessageAttachmentBuilder.createBuilder()
                .setAttachment(connectorBigDataReferenceFromDataSource("attachment"))
                .setIdentifier("identifier")
                .build(); 
    }

    public static DomibusConnectorMessageDocument createDocumentWithNoSignature() {
        DomibusConnectorMessageDocument doc = DomibusConnectorMessageDocumentBuilder.createBuilder()
                .setName("name")
                .setContent(connectorBigDataReferenceFromDataSource("document"))
                .build();
        return doc;
    }
    
    public static DomibusConnectorMessageContent createMessageContentWithDocumentWithNoSignature()  {
        try {
            DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
            messageContent.setXmlContent("<xmlContent/>".getBytes("UTF-8"));
            messageContent.setDocument(createDocumentWithNoSignature());
                        
            return messageContent;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static DomibusConnectorMessageContent createMessageContentWithDocumentWithNoPdfDocument() {
        try {
            DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
            messageContent.setXmlContent("<xmlContent/>".getBytes("UTF-8"));
            messageContent.setDocument(null);
                        
            return messageContent;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }



}
