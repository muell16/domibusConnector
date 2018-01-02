package eu.domibus.connector.domain.transition.test.util;

import eu.domibus.connector.domain.transition.DomibusConnectorActionType;
import eu.domibus.connector.domain.transition.DomibusConnectorConfirmationType;
import eu.domibus.connector.domain.transition.DomibusConnectorDetachedSignatureMimeType;
import eu.domibus.connector.domain.transition.DomibusConnectorDetachedSignatureType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageAttachmentType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageConfirmationType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageContentType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageDetailsType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageDocumentType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageErrorType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorPartyType;
import eu.domibus.connector.domain.transition.DomibusConnectorServiceType;

/**
 * Helper class to create TransitionModel Objects for testing 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class TransitionCreator {

    
    public static DomibusConnectorMessageType createMessage() {
        DomibusConnectorMessageType message = new DomibusConnectorMessageType();        
        message.setMessageDetails(createMessageDetails());
        message.setMessageContent(createMessageContent());        
        message.getMessageConfirmations().add(createMessageConfirmationType_DELIVERY());
        message.getMessageErrors().add(createMessageError());
        message.getMessageAttachments().add(createMessageAttachment());
                
        return message;
    }

    public static DomibusConnectorMessageContentType createMessageContent() {
        DomibusConnectorMessageContentType messageContent = new DomibusConnectorMessageContentType();
        messageContent.setXmlContent("xmlContent".getBytes());
        
        DomibusConnectorMessageDocumentType document = new DomibusConnectorMessageDocumentType();
        document.setDocument("document".getBytes());
        document.setDocumentName("documentName");
        
        messageContent.setDocument(document);        
        
        DomibusConnectorDetachedSignatureType sig = new DomibusConnectorDetachedSignatureType();
        sig.setDetachedSignature("detachedSignature".getBytes());
        sig.setDetachedSignatureName("detachedSignatureName");
        sig.setMimeType(DomibusConnectorDetachedSignatureMimeType.PKCS_7);
        
        document.setDetachedSignature(sig);
                                        
        return messageContent;
    }
    
    public static DomibusConnectorMessageConfirmationType createMessageConfirmationType_DELIVERY() {
        DomibusConnectorMessageConfirmationType confirmation = new DomibusConnectorMessageConfirmationType();
        confirmation.setConfirmation("DELIVERY".getBytes());
        confirmation.setConfirmationType(DomibusConnectorConfirmationType.DELIVERY);
        return confirmation;
    }
    
    public static DomibusConnectorMessageConfirmationType createMessageConfirmationType_NON_DELIVERY() {
        DomibusConnectorMessageConfirmationType confirmation = new DomibusConnectorMessageConfirmationType();
        confirmation.setConfirmation("NON_DELIVERY".getBytes());
        confirmation.setConfirmationType(DomibusConnectorConfirmationType.NON_DELIVERY);
        return confirmation;
    }
    
    public static DomibusConnectorMessageErrorType createMessageError() {
        DomibusConnectorMessageErrorType error = new DomibusConnectorMessageErrorType();
        error.setErrorDetails("error details");
        error.setErrorMessage("error message");
        error.setErrorSource("error source");
        return error;
    }
    
    public static DomibusConnectorMessageAttachmentType createMessageAttachment() {
        DomibusConnectorMessageAttachmentType attachment = new DomibusConnectorMessageAttachmentType();
        attachment.setAttachment("attachment".getBytes());
        attachment.setDescription("description");
        attachment.setIdentifier("identifier");
        attachment.setMimeType("mimetype");
        attachment.setName("name");
        return attachment;
    }
    
    public static DomibusConnectorMessageDetailsType createMessageDetails() {
        DomibusConnectorMessageDetailsType messageDetails = new DomibusConnectorMessageDetailsType();
        
        messageDetails.setBackendMessageId("backendMessageId");
        messageDetails.setConversationId("conversationId");
        messageDetails.setFinalRecipient("finalRecipient");
        messageDetails.setOriginalSender("originalSender");
        messageDetails.setRefToMessageId("refToMessageId");
        
        messageDetails.setAction(createAction());
        messageDetails.setService(createService());
        messageDetails.setFromParty(createPartyAT());
        messageDetails.setToParty(createPartyDE());
        
        return messageDetails;        
    }
    
    public static DomibusConnectorActionType createAction() {
        DomibusConnectorActionType action = new DomibusConnectorActionType();
        action.setAction("action");        
        return action;
    }
    
    public static DomibusConnectorServiceType createService() {
        DomibusConnectorServiceType service = new DomibusConnectorServiceType();
        service.setService("service");
        service.setServiceType("serviceType");
        return service;        
    }
    
    public static DomibusConnectorPartyType createPartyAT() {
        DomibusConnectorPartyType party = new DomibusConnectorPartyType();
        party.setPartyId("AT");
        party.setPartyIdType("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");
        party.setRole("GW");
        return party;
    }
    
    public static DomibusConnectorPartyType createPartyDE() {
        DomibusConnectorPartyType party = new DomibusConnectorPartyType();
        party.setPartyId("DE");
        party.setPartyIdType("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");
        party.setRole("GW");
        return party;
    }
    
}
