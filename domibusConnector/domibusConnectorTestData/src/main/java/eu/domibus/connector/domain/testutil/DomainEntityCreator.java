
package eu.domibus.connector.domain.testutil;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.ecodex.dc5.message.model.*;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 *
 */
public class DomainEntityCreator {

    public static DC5EcxAddress defaultSender() {
        return DC5EcxAddress.builder()
                .party(defaultSenderParty())
                .role(DC5Role.builder().role("urn:e-codex:role:initiator").roleType(DC5RoleType.INITIATOR).build())
                .ecxAddress("sender")
                .build();
    }

    public static DC5EcxAddress defaultRecipient() {
        return DC5EcxAddress.builder()
                .party(defaultRecipientParty())
                .role(DC5Role.builder().role("urn:e-codex:role:responder").roleType(DC5RoleType.RESPONDER).build())
                .ecxAddress("recipient")
                .build();
    }

    public static DC5Party defaultSenderParty() {
        return DC5Party.builder()
                .partyId("gw01")
                .partyIdType("urn:e-codex:services:party")
                .build();
    }

    public static DC5Party defaultRecipientParty() {
        return DC5Party.builder()
                .partyId("gw02")
                .partyIdType("urn:e-codex:services:party")
                .build();
    }

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
        return new DomibusConnectorParty("domibus-blue","urn:oasis:names:tc:ebcore:partyid-type:iso3166-1", "GW");
    }
    
    public static DC5Action createActionForm_A() {
        return DC5Action.builder().action("Form_A").build();
    }
    
    public static DC5Action createActionRelayREMMDAcceptanceRejection() {
        return DC5Action.builder().action("RelayREMMDAcceptanceRejection").build();
    }
    
    public static DC5Service createServiceEPO() {
        return DC5Service.builder().service("EPO").serviceType("urn:e-codex:services:").build();
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

    public static DC5Confirmation createMessageRelayRemmdRejectConfirmation() {
        DC5Confirmation confirmation = new DC5Confirmation();
        confirmation.setEvidence("<EVIDENCE1_RELAY_REMMD_REJECT/>".getBytes());
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION);
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
        return DomibusConnectorMessageAttachment.builder()
                .attachment(connectorBigDataReferenceFromDataSource("attachment"))
                .identifier("simple_attachment")
                .hash("213213")
                .hashAlgorithm("sha1")
                .build();
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
        messageDetails.setEbmsMessageId(EbmsMessageId.ofString("ebms1"));
        
        DC5MessageContent messageContent = new DC5MessageContent();
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
        
        DC5MessageContent messageContent = new DC5MessageContent();
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

    public static final String EPO_BACKEND = "epo_backend";

    public static DC5Message createOutgoingEpoFormAMessage() {
        return createOutgoingEpoFormAMessage(DomibusConnectorMessageId.ofRandom(), BackendMessageId.ofRandom()).build();
    }

    public static DC5Message createAlreadyOutgoneEpoFormAMessage() {
        return createOutgoingEpoFormAMessage(DomibusConnectorMessageId.ofRandom(), BackendMessageId.ofRandom())
                .target(MessageTargetSource.GATEWAY)
                .source(MessageTargetSource.BACKEND)
                .gatewayLinkName("gateway")
                .backendLinkName("backend")
                .ebmsData(createOutgoingEpoFormAMessageEbmsData()
                        .ebmsMessageId(EbmsMessageId.ofRandom())
                        .build())
                .messageContent(createEpoFormABusinessContent()
                        .ecodexContent(DC5EcodexContent.builder()
                                .asicContainer(createSimpleMessageAttachment()) //better use real asic-s
                                .trustTokenXml(createSimpleMessageAttachment())
                                .businessXml(createSimpleMessageAttachment())
                                .build())
                        .messageStates(Stream.of(DC5BusinessMessageState.builder()
                                                .event(DC5BusinessMessageState.BusinessMessageEvents.NEW_MSG)
                                                .state(DC5BusinessMessageState.BusinessMessagesStates.CREATED)
                                        .build(),
                                        DC5BusinessMessageState.builder()
                                                .event(DC5BusinessMessageState.BusinessMessageEvents.SUBMISSION_ACCEPTANCE_RCV)
                                                .state(DC5BusinessMessageState.BusinessMessagesStates.SUBMITTED)
                                                .confirmation(DC5Confirmation.builder()
                                                        .evidence("<evidence />".getBytes())    //better use real evidence...
                                                        .evidenceType(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE)
                                                        .build())
                                                .build()
                                ).collect(Collectors.toList()))
                        .build())
                .build();
    }

    public static DC5Message.DC5MessageBuilder createOutgoingEpoFormAMessage(DomibusConnectorMessageId connectorMessageId, BackendMessageId backendMessageId) {
        return DC5Message.builder()
                .backendData(createOutgoingEpoFormAMessageBackendData(backendMessageId))
                .ebmsData(createOutgoingEpoFormAMessageEbmsData().build())
                .backendLinkName(EPO_BACKEND)
                .connectorMessageId(connectorMessageId)
                .target(MessageTargetSource.GATEWAY)
                .source(MessageTargetSource.BACKEND)
                .messageContent(createEpoFormABusinessContent().build());

    }

    private static DC5MessageContent.DC5MessageContentBuilder createEpoFormABusinessContent() {
        return DC5MessageContent.builder()
                .businessContent(DC5BackendContent.builder()
                        .businessXml(createFormAAttachment())
                        .businessDocument(createSimpleMessageAttachment())
                        .build());

    }

    private static DomibusConnectorMessageAttachment createFormAAttachment() {
        return DomibusConnectorMessageAttachment.builder()
                .identifier("Form_A.xml")
                .attachment(connectorBigDataReferenceFromDataSource("<testxml />")) //better load real form A here!
                .hashAlgorithm("sha1")
                .hash("21321")
                .build();
    }

    public static DC5Message createEpoMessageFormAFromGwdomibusRed() {
        DC5Ebms messageDetails = createDomibusConnectorEpoMessageFormAFromGWdomibusRed();

        DC5MessageContent messageContent = new DC5MessageContent(); //TODO: should be a asic container
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


    public static DC5Message createRelayRemmdRejectEvidenceForMessage(DC5Message message) {
        DC5Ebms.DC5EbmsBuilder dc5EbmsBuilder = message.getEbmsData().toBuilder();

        dc5EbmsBuilder.refToEbmsMessageId(message.getEbmsData().getEbmsMessageId());     //reference the previous message

        dc5EbmsBuilder.action(createActionRelayREMMDAcceptanceRejection());

        dc5EbmsBuilder.receiver(message.getEbmsData().getSender());
        dc5EbmsBuilder.sender(message.getEbmsData().getReceiver());

        DC5Confirmation messageDeliveryConfirmation = createMessageRelayRemmdRejectConfirmation();

        return DC5Message.builder()
                .ebmsData(dc5EbmsBuilder.build())
                .target(MessageTargetSource.BACKEND)
                .source(MessageTargetSource.GATEWAY)
                .gatewayLinkName("gateway")
                .transportedMessageConfirmation(messageDeliveryConfirmation)
                .build();
    }

    public static DC5Message createRelayRemmdAcceptanceEvidenceForMessage(DC5Message message) {
        DC5Ebms.DC5EbmsBuilder dc5EbmsBuilder = message.getEbmsData().toBuilder();

        dc5EbmsBuilder.refToEbmsMessageId(message.getEbmsData().getEbmsMessageId());     //reference the previous message

        dc5EbmsBuilder.action(createActionRelayREMMDAcceptanceRejection());

        dc5EbmsBuilder.receiver(message.getEbmsData().getSender());
        dc5EbmsBuilder.sender(message.getEbmsData().getReceiver());

        DC5Confirmation messageDeliveryConfirmation = createMessageRelayRemmdAcceptanceConfirmation();

        return DC5Message.builder()
                .ebmsData(dc5EbmsBuilder.build())
                .target(MessageTargetSource.BACKEND)
                .source(MessageTargetSource.GATEWAY)
                .gatewayLinkName("gateway")
                .transportedMessageConfirmation(messageDeliveryConfirmation)
                .build();

    }

    public static DC5Message creatEvidenceMsgForMessage(DC5Message message, DC5Confirmation confirmation) {
        DC5Ebms.DC5EbmsBuilder ebmsBuilder = DC5Ebms.builder();

        ebmsBuilder.conversationId(message.getEbmsData().getConversationId());
        ebmsBuilder.ebmsMessageId(null);
        ebmsBuilder.receiver(message.getEbmsData().getReceiver());
        ebmsBuilder.sender(message.getEbmsData().getSender());
        ebmsBuilder.refToEbmsMessageId(message.getEbmsData().getEbmsMessageId());     //reference the previous message

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
        ebmsBuilder.refToEbmsMessageId(message.getEbmsData().getEbmsMessageId());     //reference the previous message

        ebmsBuilder.action(createActionForm_A());
        ebmsBuilder.service(createServiceEPO());

        return DC5Message.builder()
                .ebmsData(ebmsBuilder.build())
                .transportedMessageConfirmation(createMessageDeliveryConfirmation())
                .build();

    }


    public static DC5Ebms.DC5EbmsBuilder createOutgoingEpoFormAMessageEbmsData() {
        DC5Ebms.DC5EbmsBuilder ebmsBuilder = DC5Ebms.builder();
        ebmsBuilder.conversationId(null);      //first message no conversation set yet!

        ebmsBuilder.receiver(createAtReceiver());
        ebmsBuilder.sender(createDeSender());
        ebmsBuilder.refToEbmsMessageId(null);     //is the first message

        ebmsBuilder.action(createActionForm_A());
        ebmsBuilder.service(createServiceEPO());

        return ebmsBuilder;
    }

    public static DC5BackendData createOutgoingEpoFormAMessageBackendData(BackendMessageId backendMessageId) {
        return DC5BackendData.builder()
                .backendMessageId(backendMessageId)
                .build();

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
        messageDetails.setEbmsMessageId(EbmsMessageId.ofString("ebms5123"));
//        messageDetails.setBackendMessageId(null);   //has not been processed by the backend yet
//        messageDetails.setFinalRecipient("finalRecipient");
//        messageDetails.setOriginalSender("originalSender");
        messageDetails.setRefToEbmsMessageId(null);     //is the first message

        messageDetails.setAction(createActionForm_A());
        messageDetails.setService(createServiceEPO());
//        messageDetails.setToParty(createPartyDomibusBlue());
//        messageDetails.setFromParty(createPartyDomibusRed());

        return messageDetails;
    }

    public static DC5Ebms createDomibusConnectorMessageDetails() {
        DC5Ebms messageDetails = new DC5Ebms();
        messageDetails.setConversationId("conversation1");
        messageDetails.setEbmsMessageId(EbmsMessageId.ofString("ebms1"));
//        messageDetails.setBackendMessageId("national1");
//        messageDetails.setFinalRecipient("finalRecipient");
//        messageDetails.setOriginalSender("originalSender");
        messageDetails.setRefToEbmsMessageId(EbmsMessageId.ofString(("refToMessageId")));

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
                
        return DomibusConnectorMessageAttachment.builder()
                .attachment(connectorBigDataReferenceFromDataSource("attachment"))
                .identifier("identifier")
                .hash("12134")
                .hashAlgorithm("sha1")
                .build(); 
    }

//    public static DomibusConnectorMessageDocument createDocumentWithNoSignature() {
//        ClassPathResource pdf = new ClassPathResource("/pdf/ExamplePdf.pdf");
//        DomibusConnectorMessageDocument doc = DomibusConnectorMessageDocumentBuilder.createBuilder()
//                .setName("name")
//                .setContent(connectorBigDataReferenceFromDataSource(pdf))
//                .build();
//        return doc;
//    }

//    public static DomibusConnectorMessageDocument createDocumentWithSignature() {
//        ClassPathResource pdf = new ClassPathResource("/pdf/ExamplePdfSigned.pdf");
//        DomibusConnectorMessageDocument doc = DomibusConnectorMessageDocumentBuilder.createBuilder()
//                .setName("name")
//                .setContent(connectorBigDataReferenceFromDataSource(pdf))
//                .build();
//        return doc;
//    }
    
    public static DC5MessageContent createMessageContentWithDocumentWithNoSignature()  {
//        try {
            DC5MessageContent messageContent = new DC5MessageContent();
//            messageContent.setXmlContent("<xmlContent/>".getBytes("UTF-8"));
//            messageContent.setDocument(createDocumentWithNoSignature());
                        
            return messageContent;
//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    public static DC5MessageContent createMessageContentWithDocumentWithNoPdfDocument() {
//        try {
            DC5MessageContent messageContent = new DC5MessageContent();
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

    public static DC5Message createIncomingEpoFormAMessage() {
        return DC5Message.builder()
                .source(MessageTargetSource.GATEWAY)
                .target(MessageTargetSource.BACKEND)
                .ebmsData(DC5Ebms.builder()
                        .ebmsMessageId(EbmsMessageId.ofRandom())
                        .service(createServiceEPO())
                        .action(createActionForm_A())
                        .sender(defaultSender())
                        .receiver(defaultRecipient())
                        .build()
                )
                .messageContent(DC5MessageContent.builder()
                        .ecodexContent(DC5EcodexContent.builder()
                                .asicContainer(createMessageAttachment())   //just a simple attachment Improve by using correct signed asic-s
                                .trustTokenXml(createMessageAttachment())   //just a simple attachment Improve by using correct signed xml
                                .businessXml(createFormAAttachment())       //just a simple attachment Improve by using correct xml
                                .build())
                        .build()
                )
                .transportedMessageConfirmation(DC5Confirmation.builder()
                        .evidenceType(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE)
                        .evidence("<evidence/>".getBytes()) //just a simple evidence Improve by using correct created and signed evidence xml
                        .build())
                .build();
    }


}
