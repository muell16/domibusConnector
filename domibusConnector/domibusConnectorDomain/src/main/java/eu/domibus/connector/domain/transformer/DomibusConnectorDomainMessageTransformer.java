package eu.domibus.connector.domain.transformer;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DetachedSignature;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.transition.DomibusConnectorActionType;
import eu.domibus.connector.domain.transition.DomibusConnectorConfirmationType;
import eu.domibus.connector.domain.transition.DomibusConnectorDetachedSignatureType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageAttachmentType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageConfirmationType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageContentType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageDetailsType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageDocumentType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageErrorType;
import eu.domibus.connector.domain.transition.DomibusConnectorPartyType;
import eu.domibus.connector.domain.transition.DomibusConnectorServiceType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 11:59:43
 */
public class DomibusConnectorDomainMessageTransformer {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorDomainMessageTransformer.class);
    
    /**
     * This exception is thrown when the provided
     * domain model object does not fullfill the requirements of the
     * transition model in a way that there is no mapping possibly
     */
    public static class CannotBeMappedToTransitionException extends RuntimeException {

        private CannotBeMappedToTransitionException(String text) {
            super(text);
        }
        
    }
    
    
	/**
	 * transforms a message from domain model to
     * transition model
     * 
     * this method does not check if the message makes sense
     *
     *  
     *  DomibusConnectorMessage#getMessageDetails must be not null!
     * @throws CannotBeMappedToTransitionException if a
	 * @param domainMessage - the message (domain model)
     * @return - the transformed message (transition model)
	 */
	public static @Nonnull DomibusConnectorMessageType transformDomainToTransition(final @Nonnull DomibusConnectorMessage domainMessage){
        LOGGER.debug("transformDomainToTransition: with domainMessage [{domainMessage}]");
        if (domainMessage == null) {
            throw new CannotBeMappedToTransitionException("domainMessage is not allowed to be null!");
        }
        if (domainMessage.getMessageDetails() == null) {
            throw new CannotBeMappedToTransitionException("DomibusConnectorMessage.getMessageDetails() is not allowed to be null!");
        }       
        DomibusConnectorMessageType TOMessageType = new DomibusConnectorMessageType();
        
        //map messageDetails
        TOMessageType.setMessageDetails(transformMessageDetailsDomainToTransition(domainMessage.getMessageDetails()));
        //map messageContent
        TOMessageType.setMessageContent(transformMessageContentDomainToTransition(domainMessage.getMessageContent()));
        //map message confirmations
        List<DomibusConnectorMessageConfirmation> messageConfirmations = domainMessage.getMessageConfirmations();
        for (DomibusConnectorMessageConfirmation msgConfirm : messageConfirmations) {
            TOMessageType.getMessageConfirmations()
                    .add(transformMessageConfirmationDomainToTransition(msgConfirm));
        }
        //map message attachments
        for (DomibusConnectorMessageAttachment msgAttach : domainMessage.getMessageAttachments()) {
            TOMessageType.getMessageAttachments()
                    .add(transformMessageAttachmentDomainToTransition(msgAttach));
        }        
        //map message errors
        for (DomibusConnectorMessageError msgError : domainMessage.getMessageErrors()) {
            TOMessageType.getMessageErrors()
                .add(transformMessageErrorDomainToTransition(msgError));
        }
        
		return TOMessageType;
	}
    
    /**
     * converts a messageConfirmation from domain model to
     * transition model
     *  the getEvidence byteArray must be not null!
     *  
     * @throws IllegalArgumentException is thrown if DomibusConnectorMessageConfirmation#getEvidence returns null or 
     *  DomibusConnectorMessageConfirmation#getEvidenceType returns null
     * @param messageConfirmation the messageConfirmation
     * @return the messageConfirmation in transition model
     */
    static @Nonnull DomibusConnectorMessageConfirmationType transformMessageConfirmationDomainToTransition(final @Nonnull DomibusConnectorMessageConfirmation messageConfirmation) {
        DomibusConnectorMessageConfirmationType confirmationTO = new DomibusConnectorMessageConfirmationType();
        if (messageConfirmation.getEvidence() == null) {
            throw new CannotBeMappedToTransitionException("byte array getEvidence() is not allowed to be null!");
        }
        if (messageConfirmation.getEvidenceType() == null) {
            throw new CannotBeMappedToTransitionException("evidenceType is not allowed to be null!");
        }
        confirmationTO.setConfirmation(Arrays.copyOf(messageConfirmation.getEvidence(), messageConfirmation.getEvidence().length));        
        confirmationTO.setConfirmationType(DomibusConnectorConfirmationType.valueOf(messageConfirmation.getEvidenceType().name()));
        
        return confirmationTO;
        
    }
    
    /**
     * Translates messageError from domain model to transition model
     * 
     * @param messageError - the (domain model) messageError
     * @return the translated (transition model) messageError
     */
    static @Nonnull DomibusConnectorMessageErrorType transformMessageErrorDomainToTransition(final @Nonnull DomibusConnectorMessageError messageError) {
        DomibusConnectorMessageErrorType errorTO = new DomibusConnectorMessageErrorType();
        BeanUtils.copyProperties(messageError, errorTO);
        return errorTO;
    }
    
    
    /**
     *      
     * Translates messageAttachment from domain model to transition model
     *  the attachment and identifier property must not be null!
     * 
     * @param messageAttachment - the (domain model) messageAttachment
     * @return the translated (transition model) messageAttachment
     */
    static @Nonnull DomibusConnectorMessageAttachmentType transformMessageAttachmentDomainToTransition(final @Nonnull DomibusConnectorMessageAttachment messageAttachment) {
        DomibusConnectorMessageAttachmentType attachmentTO = new DomibusConnectorMessageAttachmentType();  
        if (messageAttachment.getAttachment() == null) {            
            throw new CannotBeMappedToTransitionException("attachment is not allowed to be null!");
        }
        if (messageAttachment.getIdentifier() == null) {
            throw new CannotBeMappedToTransitionException("identifier is not allowed to be null!");
        }
        BeanUtils.copyProperties(messageAttachment, attachmentTO);        
        return attachmentTO;
    }
    
    /**
     * Translates messageContent from domain model to transition model
     *  
     * @param messageContent - the (domain model) messageContent
     * @return the translated (transition model) messageContent or null if null provided
     */
    static @Nullable DomibusConnectorMessageContentType transformMessageContentDomainToTransition(final @Nullable DomibusConnectorMessageContent messageContent) {
        if (messageContent == null) {
            return null;
        }
        DomibusConnectorMessageContentType messageContentTO = new DomibusConnectorMessageContentType();        
        if (messageContent.getXmlContent() == null) {
            throw new CannotBeMappedToTransitionException("xmlContent of content must be not null!");
        }
        messageContentTO.setXmlContent(Arrays.copyOf(messageContent.getXmlContent(), messageContent.getXmlContent().length));
        
        //maps Document of messageContent
        DomibusConnectorMessageDocument document = messageContent.getDocument();
        DomibusConnectorMessageDocumentType documentTO = new DomibusConnectorMessageDocumentType();
        documentTO.setDocument(Arrays.copyOf(document.getDocument(), document.getDocument().length));
        documentTO.setDocumentName(document.getDocumentName());
        messageContentTO.setDocument(documentTO);
        
        //map signature type of document
        DetachedSignature detachedSignature = document.getDetachedSignature();
        DomibusConnectorDetachedSignatureType detachedSignatureTypeTO = new DomibusConnectorDetachedSignatureType();
        detachedSignatureTypeTO.setDetachedSignature(
                Arrays.copyOf(detachedSignature.getDetachedSignature(), detachedSignature.getDetachedSignature().length));
        detachedSignatureTypeTO.setDetachedSignatureName(detachedSignature.getDetachedSignatureName());
        detachedSignatureTypeTO.setMimeType(
                DomibusConnectorDomainDetachedSignatureEnumTransformer
                        .transformDetachedSignatureMimeTypeDomainToTransition(detachedSignature.getMimeType()));
        documentTO.setDetachedSignature(detachedSignatureTypeTO);
        
        return messageContentTO;
    }
    
    
    static @Nonnull DomibusConnectorMessageDetailsType transformMessageDetailsDomainToTransition(final @Nonnull DomibusConnectorMessageDetails messageDetails) {        
        LOGGER.debug("transformMessageDetailsDomaintToTransition: messageDetails are [{}]", messageDetails);
        if (messageDetails == null) {
            throw new CannotBeMappedToTransitionException("messageDetails are not allowed to be null!");
        }
        DomibusConnectorMessageDetailsType TODetailsType = new DomibusConnectorMessageDetailsType();    
        
        if (messageDetails.getFinalRecipient() == null) {
            throw new CannotBeMappedToTransitionException("final recipient is mandatory!");
        }
        if (messageDetails.getOriginalSender() == null) {
            throw new CannotBeMappedToTransitionException("original sender is mandatory!");
        }
        //map all properties with same name and type: backendMessageId, conversationId, finalRecipient, originalSender, refToMessageId
        BeanUtils.copyProperties(messageDetails, TODetailsType);
        
        //map action        
        if (messageDetails.getAction() == null) {
            throw new CannotBeMappedToTransitionException("action is mandatory in messageDetails!");
        }
        DomibusConnectorActionType actionTO = new DomibusConnectorActionType();
        BeanUtils.copyProperties(messageDetails.getAction(), actionTO);
        TODetailsType.setAction(actionTO);
        
        //map fromParty
        if (messageDetails.getFromParty() == null) {
            throw new CannotBeMappedToTransitionException("fromParty is mandatory in messageDetails");
        }
        DomibusConnectorPartyType fromPartyTO = new DomibusConnectorPartyType();
        BeanUtils.copyProperties(messageDetails.getFromParty(), fromPartyTO);
        TODetailsType.setFromParty(fromPartyTO);
        
        //map toParty
        if (messageDetails.getToParty() == null) {
            throw new CannotBeMappedToTransitionException("toParty is mandatory in messageDetails");
        }
        DomibusConnectorPartyType toPartyTO = new DomibusConnectorPartyType();
        BeanUtils.copyProperties(messageDetails.getToParty(), toPartyTO);
        TODetailsType.setToParty(toPartyTO);
        
        //map service
        if (messageDetails.getService()== null) {
            throw new CannotBeMappedToTransitionException("service is mandatory in messageDetails");
        }
        DomibusConnectorServiceType serviceTO = new DomibusConnectorServiceType();
        BeanUtils.copyProperties(messageDetails.getService(), serviceTO);
        TODetailsType.setService(serviceTO);
                
        return TODetailsType;
    }
    
    

	/**
	 * 
	 * @param transitionMessage
	 */
	public static @Nonnull DomibusConnectorMessage transformTransitionToDomain(final @Nonnull DomibusConnectorMessageType transitionMessage){        
        DomibusConnectorMessageDetailsType messageDetailsTO = transitionMessage.getMessageDetails();
        DomibusConnectorMessageDetails messageDetails = transformMessageDetailsTransitionToDomain(messageDetailsTO);
        //DomibusConnectorMessage
        DomibusConnectorMessage domibusConnectorMessage = null;
        
        //map confirmations
        List<DomibusConnectorMessageConfirmation> confirmations = 
                transitionMessage.getMessageConfirmations().stream().map( c -> transformMessageConfirmationTransitionToDomain(c)).collect(Collectors.toList());
                
        if (transitionMessage.getMessageContent() == null && confirmations.size() > 0) {
            DomibusConnectorMessageConfirmation confirmation = confirmations.remove(0);            
            domibusConnectorMessage = new DomibusConnectorMessage(messageDetails, confirmation);   
            for (DomibusConnectorMessageConfirmation c : confirmations) {
                domibusConnectorMessage.addConfirmation(c);
            }
            
        } else if (transitionMessage.getMessageContent() != null) {
            //DomibusConnectorMessageContentType messageContent = transitionMessage.getMessageContent();
            DomibusConnectorMessageContent messageContent = transformMessageContentTransitionToDomain(transitionMessage.getMessageContent());
            domibusConnectorMessage = new DomibusConnectorMessage(messageDetails, messageContent);   
            for (DomibusConnectorMessageConfirmation c : confirmations) {
                domibusConnectorMessage.addConfirmation(c);
            }
        } else {
            //should not end up here!
            throw new IllegalArgumentException("cannot map provided transition model!");
        }
        
        //map message errors
        for (DomibusConnectorMessageErrorType error : transitionMessage.getMessageErrors()) {
            domibusConnectorMessage.addError(
                    transformMessageErrorTransitionToDomain(error));
        }
                
        //map message attachments
        for (DomibusConnectorMessageAttachmentType attachment : transitionMessage.getMessageAttachments()) {
            domibusConnectorMessage.addAttachment(
                    transformMessageAttachmentTransitionToDomain(attachment));
        }
                
		return domibusConnectorMessage;
	}

    
    static @Nonnull DomibusConnectorMessageAttachment transformMessageAttachmentTransitionToDomain(final @Nonnull DomibusConnectorMessageAttachmentType messageAttachmentTO) {
        DomibusConnectorMessageAttachment messageAttachment = new DomibusConnectorMessageAttachment(
                Arrays.copyOf(messageAttachmentTO.getAttachment(), messageAttachmentTO.getAttachment().length),
                messageAttachmentTO.getIdentifier()
        );        
        BeanUtils.copyProperties(messageAttachmentTO, messageAttachment);
        
        return messageAttachment;
    }
    
    
    static @Nonnull DomibusConnectorMessageError transformMessageErrorTransitionToDomain(final @Nonnull DomibusConnectorMessageErrorType errorTypeTO) {
        //(final String text, final String details, final String source){
        DomibusConnectorMessageError error = new DomibusConnectorMessageError(
                errorTypeTO.getErrorMessage(),
                errorTypeTO.getErrorDetails(),
                errorTypeTO.getErrorSource()
        );
        return error;
    }
    
    static DomibusConnectorMessageConfirmation transformMessageConfirmationTransitionToDomain(final @Nonnull DomibusConnectorMessageConfirmationType messageConfirmationTO) {
        DomibusConnectorMessageConfirmation confirmation = new DomibusConnectorMessageConfirmation();

        confirmation.setEvidence(Arrays.copyOf(messageConfirmationTO.getConfirmation(), messageConfirmationTO.getConfirmation().length));
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.valueOf(messageConfirmationTO.getConfirmationType().name()));
        
        return confirmation;
    }
    
    
    static @Nonnull DomibusConnectorMessageContent transformMessageContentTransitionToDomain(final @Nonnull DomibusConnectorMessageContentType messageContentTO) {
        DomibusConnectorMessageContent messageContent = new DomibusConnectorMessageContent();
        
        messageContent.setXmlContent(Arrays.copyOf(messageContentTO.getXmlContent(), messageContentTO.getXmlContent().length));
        
        //maps Document of messageContent
        DomibusConnectorMessageDocumentType documentTO = messageContentTO.getDocument();
        
        
        //maps signature of document     
        DomibusConnectorDetachedSignatureType detachedSignatureTO = documentTO.getDetachedSignature();
        
        DetachedSignature detachedSignature = new DetachedSignature(
                Arrays.copyOf(detachedSignatureTO.getDetachedSignature(), detachedSignatureTO.getDetachedSignature().length),
                detachedSignatureTO.getDetachedSignatureName(),
//                eu.domibus.connector.domain.model.DetachedSignatureMimeType.valueOf(detachedSignatureTO.getMimeType().name())
                DomibusConnectorDomainDetachedSignatureEnumTransformer
                        .transformDetachedSignatureMimeTypeTransitionToDomain(detachedSignatureTO.getMimeType())
        );
        
        //maps Document of messageContent
        DomibusConnectorMessageDocument document = 
                new DomibusConnectorMessageDocument(
                        Arrays.copyOf(documentTO.getDocument(), documentTO.getDocument().length),
                        documentTO.getDocumentName(),
                        detachedSignature
                ); 
        messageContent.setDocument(document);
        
        
        return messageContent;
    }
    
    
    static @Nonnull DomibusConnectorMessageDetails transformMessageDetailsTransitionToDomain(final @Nonnull DomibusConnectorMessageDetailsType messageDetailsTO) {
        DomibusConnectorMessageDetails messageDetails = new DomibusConnectorMessageDetails();
        
        //map all properties with same name and type: backendMessageId, conversationId, finalRecipient, originalSender, refToMessageId
        BeanUtils.copyProperties(messageDetailsTO, messageDetails);
        
        //map action
        DomibusConnectorActionType actionTO = messageDetailsTO.getAction();
        
        DomibusConnectorAction action = 
                new DomibusConnectorAction(actionTO.getAction(), true); //default mapping is assumed true!
        messageDetails.setAction(action);
        
        //map service
        DomibusConnectorServiceType serviceTO = messageDetailsTO.getService();
        DomibusConnectorService service = 
                new DomibusConnectorService(serviceTO.getService(), serviceTO.getServiceType());
        messageDetails.setService(service);
        
        //map partyTO
        DomibusConnectorPartyType toPartyTO = messageDetailsTO.getToParty();
        DomibusConnectorParty toParty = 
                new DomibusConnectorParty(toPartyTO.getPartyId(), toPartyTO.getPartyIdType(), toPartyTO.getRole());
        messageDetails.setToParty(toParty);
        
        //map partyFrom
        DomibusConnectorPartyType fromPartyTO = messageDetailsTO.getFromParty();
        DomibusConnectorParty fromParty = 
                new DomibusConnectorParty(fromPartyTO.getPartyId(), fromPartyTO.getPartyIdType(), fromPartyTO.getRole());
        messageDetails.setFromParty(fromParty);
        
        
        return messageDetails;
    }
    
}