
package eu.domibus.connector.domain.testutil;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageAttachmentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.ecodex.dc5.message.model.*;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *
 */
public class DomainEntityCreator {

    public static DomibusConnectorParty createPartyATasInitiator() {
        DomibusConnectorParty p = new DomibusConnectorParty("AT", "urn:oasis:names:tc:ebcore:partyid-type:iso3166-1", "GW");
        p.setRoleType(DomibusConnectorParty.PartyRoleType.INITIATOR);
        return p;
    }
    
    public static DomibusConnectorParty createPartyDE() {
        DomibusConnectorParty p = new DomibusConnectorParty("DE", "urn:oasis:names:tc:ebcore:partyid-type:iso3166-1", "GW");
        p.setRoleType(DomibusConnectorParty.PartyRoleType.RESPONDER);
        return p;
    }

    public static DomibusConnectorParty createPartyDomibusRed() {
        DomibusConnectorParty p = new DomibusConnectorParty("domibus-red","urn:oasis:names:tc:ebcore:partyid-type:iso3166-1", "GW");
        return p;
    }

    public static DomibusConnectorParty createPartyDomibusBlue() {
        DomibusConnectorParty p = new DomibusConnectorParty("domibus-blue","urn:oasis:names:tc:ebcore:partyid-type:iso3166-1", "GW");
        return p;
    }
    
    public static DC5Action createActionForm_A() {
        DC5Action a = new DC5Action("Form_A");
//        DomibusConnectorAction a = new DomibusConnectorAction("Form_A", true);
        return a;                
    }
    
    public static DC5Action createActionRelayREMMDAcceptanceRejection() {
        DC5Action a = new DC5Action("RelayREMMDAcceptanceRejection");
//        DomibusConnectorAction a = new DomibusConnectorAction("RelayREMMDAcceptanceRejection", true);
        return a;
    }
    
    public static DC5Service createServiceEPO() {
        DC5Service s = new DC5Service("EPO", "urn:e-codex:services:");
        return s;
    }

    public static DC5Confirmation createMessageSubmissionAcceptanceConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("<EVIDENCE1_SUBMISSION_ACCEPTANCE/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);
        return confirmation;
    }



    public static DC5Confirmation createMessageSubmissionRejectionConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("<EVIDENCE1_SUBMISSION_REJECT/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.SUBMISSION_REJECTION);
        return confirmation;
    }

    public static DC5Confirmation createMessageRelayRemmdAcceptanceConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("<EVIDENCE1_RELAY_REMMD/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE);
        return confirmation;
    }
    
    public static DC5Confirmation createMessageDeliveryConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("<EVIDENCE1_DELIVERY/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.DELIVERY);
        return confirmation;
    }
    
    public static DC5Confirmation createMessageNonDeliveryConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("<EVIDENCE1_NON_DELIVERY/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.NON_DELIVERY);
        return confirmation;
    }

    public static DC5Confirmation createRetrievalEvidenceMessage() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("<EVIDENCE_RETRIEVAL/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.RETRIEVAL);
        return confirmation;
    }
    
    public static DomibusConnectorMessageAttachment createSimpleMessageAttachment() {
        DomibusConnectorMessageAttachment attachment = 
                new DomibusConnectorMessageAttachment(connectorBigDataReferenceFromDataSource("attachment"), "identifier");

        attachment.setName("name");
        attachment.setMimeType("application/garbage");
        attachment.setDescription("a description");
        return attachment;
    }

    public static LargeFileReference connectorBigDataReferenceFromDataSource(String input) {
        LargeFileReferenceGetSetBased reference =
                new LargeFileReferenceGetSetBased();
        
        reference.setBytes(input.getBytes());
        reference.setReadable(true);
//        reference.setInputStream(new ByteArrayInputStream(input.getBytes()));
        
        return reference;
    }

    @SneakyThrows
    public static LargeFileReference connectorBigDataReferenceFromDataSource(Resource input) {
        LargeFileReferenceGetSetBased reference =
                new LargeFileReferenceGetSetBased();

        reference.setBytes(StreamUtils.copyToByteArray(input.getInputStream()));
        reference.setReadable(true);
//        reference.setInputStream(input.getInputStream());

        return reference;
    }
    
    public static DC5Message createEvidenceNonDeliveryMessage() {
        DC5Ebms messageDetails = createDomibusConnectorMessageDetails();
        DC5Confirmation nonDeliveryConfirmation = createMessageNonDeliveryConfirmation();
        
        DC5Message msg = DomibusConnectorMessageBuilder.createBuilder()
                .setConnectorMessageId("id1")
                .setMessageDetails(messageDetails)
                .addTransportedConfirmations(nonDeliveryConfirmation)
                .build();
        
        return msg;
    }


    public static DC5Message createSimpleTestMessage() {
        
        DC5Ebms messageDetails = new DC5Ebms();

        messageDetails.setConversationId("conversation1");
        messageDetails.setEbmsMessageId("ebms1");
        
        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
        DC5Message msg = DC5Message.builder()
                .ebmsData(messageDetails)
                .build();

        msg.setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
        //msg.setDbMessageId(78L);
        //msg.getMessageDetails().
        return msg;
    }
    
    public static DC5Message createMessage() {
        DC5Ebms messageDetails = createDomibusConnectorMessageDetails();
        
        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
//        messageContent.setXmlContent("<xmlContent/>".getBytes());
        
        DetachedSignature detachedSignature = new DetachedSignature("detachedSignature".getBytes(), "signaturename", DetachedSignatureMimeType.BINARY);
                
        DomibusConnectorMessageDocument messageDocument = new DomibusConnectorMessageDocument(connectorBigDataReferenceFromDataSource("documentbytes"), "Document1.pdf", detachedSignature);
                        
//        messageContent.setDocument(messageDocument);

        return DC5Message.builder()
                        .transportedMessageConfirmation(createMessageDeliveryConfirmation())
                .ebmsData(messageDetails)
                .messageContent(messageContent)
                .build();

    }

    public static DC5Message createEpoMessage() {
        DC5Ebms messageDetails = createDomibusConnectorEpoMessageDetails();

        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
//        messageContent.setXmlContent("<xmlContent/>".getBytes());

        DetachedSignature detachedSignature = new DetachedSignature("detachedSignature".getBytes(), "signaturename", DetachedSignatureMimeType.BINARY);

        DomibusConnectorMessageDocument messageDocument = new DomibusConnectorMessageDocument(connectorBigDataReferenceFromDataSource("documentbytes"), "Document1.pdf", detachedSignature);
//        messageContent.setDocument(messageDocument);

        DC5Message message = DomibusConnectorMessageBuilder.createBuilder()
                .addAttachment(createSimpleMessageAttachment())
                .setMessageDetails(messageDetails)
                .setMessageContent(messageContent)
                .setConnectorMessageId("MSG1")
                .build();

        return message;
    }

    public static DC5Message createEpoMessageFormAFromGwdomibusRed() {
        DC5Ebms messageDetails = createDomibusConnectorEpoMessageFormAFromGWdomibusRed();

        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent(); //TODO: should be a asic container
//        messageContent.setXmlContent("<xmlContent/>".getBytes());

        DetachedSignature detachedSignature = new DetachedSignature("detachedSignature".getBytes(), "signaturename", DetachedSignatureMimeType.BINARY);

        DomibusConnectorMessageDocument messageDocument = new DomibusConnectorMessageDocument(connectorBigDataReferenceFromDataSource("documentbytes"), "Document1.pdf", detachedSignature);
//        messageContent.setDocument(messageDocument);

        DC5Message message = DomibusConnectorMessageBuilder.createBuilder()
                .addAttachment(createSimpleMessageAttachment())
                .setMessageDetails(messageDetails)
                .setMessageContent(messageContent)
                .build();

        message.addTransportedMessageConfirmation(createMessageSubmissionAcceptanceConfirmation());


        return message;
    }


    public static DC5Message createRelayRemmdAcceptanceEvidenceForMessage(DC5Message message) {
        DC5Ebms.DC5EbmsBuilder dc5EbmsBuilder = message.getEbmsData().toBuilder();

        //messageDetails.setConversationId(null);      //first message no conversation set yet!
//        dc5EbmsBuilder.setEbmsMessageId(null); //message from backend
//        dc5EbmsBuilder.setBackendMessageId(null);   //has not been processed by the backend yet
//        dc5EbmsBuilder.setFinalRecipient(null);
//        dc5EbmsBuilder.setOriginalSender(null);
        dc5EbmsBuilder.refToMessageId(message.getEbmsData().getEbmsMessageId());     //reference the previous message

        dc5EbmsBuilder.action(createActionForm_A());
        dc5EbmsBuilder.service(createServiceEPO());
        dc5EbmsBuilder.receiver(message.getEbmsData().getReceiver());
        dc5EbmsBuilder.sender(message.getEbmsData().getSender());

        DC5Confirmation messageDeliveryConfirmation = createMessageRelayRemmdAcceptanceConfirmation();

        return DC5Message.builder()
                .ebmsData(dc5EbmsBuilder.build())
                .transportedMessageConfirmation(messageDeliveryConfirmation)
                .build();

    }

    public static DC5Message creatEvidenceMsgForMessage(DC5Message message, DC5Confirmation confirmation) {
        DC5Ebms.DC5EbmsBuilder ebmsBuilder = DC5Ebms.builder();

        ebmsBuilder.conversationId(message.getEbmsData().getConversationId());
        ebmsBuilder.ebmsMessageId(null);
        ebmsBuilder.receiver(message.getEbmsData().getReceiver());
        ebmsBuilder.sender(message.getEbmsData().getSender());
        ebmsBuilder.refToMessageId(message.getEbmsData().getEbmsMessageId());     //reference the previous message

        ebmsBuilder.action(createActionForm_A());
        ebmsBuilder.service(createServiceEPO());

        return DC5Message.builder()
                .ebmsData(ebmsBuilder.build())
                .transportedMessageConfirmation(confirmation)
                .build();
    }

    public static DC5Message createDeliveryEvidenceForMessage(DC5Message message) {

        DC5Ebms.DC5EbmsBuilder ebmsBuilder = DC5Ebms.builder();

        ebmsBuilder.conversationId(message.getEbmsData().getConversationId());
        ebmsBuilder.ebmsMessageId(null);
        ebmsBuilder.receiver(message.getEbmsData().getReceiver());
        ebmsBuilder.sender(message.getEbmsData().getSender());
        ebmsBuilder.refToMessageId(message.getEbmsData().getEbmsMessageId());     //reference the previous message

        ebmsBuilder.action(createActionForm_A());
        ebmsBuilder.service(createServiceEPO());

        return DC5Message.builder()
                .ebmsData(ebmsBuilder.build())
                .transportedMessageConfirmation(createMessageDeliveryConfirmation())
                .build();

    }


    public static DC5Ebms createDomibusConnectorEpoMessageDetails() {
        DC5Ebms.DC5EbmsBuilder ebmsBuilder = DC5Ebms.builder();
        ebmsBuilder.conversationId(null);      //first message no conversation set yet!
        ebmsBuilder.ebmsMessageId("ebms1");
//        ebmsBuilder.backendMessageId(null);   //has not been processed by the backend yet
//        ebmsBuilder.("finalRecipient");
//        ebmsBuilder.setOriginalSender("originalSender");

//        ebmsBuilder.setToParty(createPartyATasInitiator());
//        ebmsBuilder.setFromParty(createPartyDE());
        ebmsBuilder.receiver(createAtReceiver());
        ebmsBuilder.sender(createDeSender());
        ebmsBuilder.refToMessageId(null);     //is the first message

        ebmsBuilder.action(createActionForm_A());
        ebmsBuilder.service(createServiceEPO());

        return ebmsBuilder.build();
    }

    private static DC5EcxAddress createDeSender() {
        return DC5EcxAddress.builder()
                .party(new DC5Party("DE", "ecodex"))
                .role(new DC5Role("GW", DC5RoleType.INITIATOR))
                .ecxAddress("originalSender")
                .build();
    }

    private static DC5EcxAddress createAtReceiver() {
        return DC5EcxAddress.builder()
                .party(new DC5Party("AT", "ecodex"))
                .role(new DC5Role("GW", DC5RoleType.RESPONDER))
                .ecxAddress("finalRecipient")
                .build();
    }

    public static DC5Ebms createDomibusConnectorEpoMessageFormAFromGWdomibusRed() {
        DC5Ebms messageDetails = new DC5Ebms();
        messageDetails.setConversationId("conv567");      //first message no conversation set yet!
        messageDetails.setEbmsMessageId("ebms5123");
//        messageDetails.setBackendMessageId(null);   //has not been processed by the backend yet
//        messageDetails.setFinalRecipient("finalRecipient");
//        messageDetails.setOriginalSender("originalSender");
        messageDetails.setRefToMessageId(null);     //is the first message

        messageDetails.setAction(createActionForm_A());
        messageDetails.setService(createServiceEPO());
//        messageDetails.setToParty(createPartyDomibusBlue());
//        messageDetails.setFromParty(createPartyDomibusRed());

        return messageDetails;
    }

    public static DC5Ebms createDomibusConnectorMessageDetails() {
        DC5Ebms messageDetails = new DC5Ebms();
        messageDetails.setConversationId("conversation1");
        messageDetails.setEbmsMessageId("ebms1");
//        messageDetails.setBackendMessageId("national1");
//        messageDetails.setFinalRecipient("finalRecipient");
//        messageDetails.setOriginalSender("originalSender");
        messageDetails.setRefToMessageId("refToMessageId");

//        messageDetails.setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//        messageDetails.setDeliveredToGateway(parseDateTime("2018-01-01 12:12:12"));
//        messageDetails.setDeliveredToBackend(parseDateTime("2018-01-01 12:12:12"));
        
        messageDetails.setAction(createActionForm_A());
        messageDetails.setService(createServiceEPO());
//        messageDetails.setToParty(createPartyATasInitiator());
//        messageDetails.setFromParty(createPartyDE());
        
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
        //DomibusConnectorMessageError error = new DomibusConnectorMessageError("error message", "error detail message", "error source");
        //return error;
        return DomibusConnectorMessageErrorBuilder.createBuilder()
                .setSource(Object.class)
                .setDetails("error detail message")
                .setText("error message")
                .build();
    }

    public static DomibusConnectorMessageAttachment createMessageAttachment() {
                
        return DomibusConnectorMessageAttachmentBuilder.createBuilder()
                .setAttachment(connectorBigDataReferenceFromDataSource("attachment"))
                .setIdentifier("identifier")
                .build(); 
    }

    public static DomibusConnectorMessageDocument createDocumentWithNoSignature() {
        ClassPathResource pdf = new ClassPathResource("/pdf/ExamplePdf.pdf");
        DomibusConnectorMessageDocument doc = DomibusConnectorMessageDocumentBuilder.createBuilder()
                .setName("name")
                .setContent(connectorBigDataReferenceFromDataSource(pdf))
                .build();
        return doc;
    }

    public static DomibusConnectorMessageDocument createDocumentWithSignature() {
        ClassPathResource pdf = new ClassPathResource("/pdf/ExamplePdfSigned.pdf");
        DomibusConnectorMessageDocument doc = DomibusConnectorMessageDocumentBuilder.createBuilder()
                .setName("name")
                .setContent(connectorBigDataReferenceFromDataSource(pdf))
                .build();
        return doc;
    }
    
    public static DomibusConnectorMessageContent createMessageContentWithDocumentWithNoSignature()  {
//        try {
            DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
//            messageContent.setXmlContent("<xmlContent/>".getBytes("UTF-8"));
//            messageContent.setDocument(createDocumentWithNoSignature());
                        
            return messageContent;
//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    public static DomibusConnectorMessageContent createMessageContentWithDocumentWithNoPdfDocument() {
//        try {
            DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
//            messageContent.setXmlContent("<xmlContent/>".getBytes("UTF-8"));
//            messageContent.setDocument(null);
                        
            return messageContent;
//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    public static String dateFormat = "YYYY-MM-DD HH:mm:ss";

    public static Date parseDateTime(String date) {
        try {
            return new SimpleDateFormat(dateFormat).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("date parse error!", e);
        }
    }

}
