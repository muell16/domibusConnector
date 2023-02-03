package eu.domibus.connector.domain.transformer;

import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.domain.transition.*;
import eu.domibus.connector.domain.transition.tools.ConversionTools;
import eu.domibus.connector.domain.transition.tools.TransitionHelper;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.process.MessageProcessManager;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

import javax.activation.DataHandler;
import javax.validation.constraints.NotNull;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Transforms the TransitionObjects to the internal domainModel
 */
@Service
public class DomibusConnectorDomainMessageTransformerService {

    public static final String ASICS_CONTAINER_IDENTIFIER = "ASIC-S";

    public static final String TOKEN_XML_IDENTIFIER = "tokenXML";

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorDomainMessageTransformerService.class);
    private final LargeFilePersistenceService largeFilePersistenceService;
    private final MessageProcessManager messageProcessManager;

    public List<DomibusConnectorMessageError> transformTransitionToDomain(List<DomibusConnectorMessageErrorType> messageErrors) {
        return messageErrors.stream()
                .map(this::transformMessageErrorTransitionToDomain)
                .collect(Collectors.toList());
    }


    /**
     * This exception is thrown when the provided
     * domain model object does not fullfill the requirements of the
     * transition model in a way that there is no mapping possibly
     */
    public static class CannotBeMappedToTransitionException extends RuntimeException {

        private CannotBeMappedToTransitionException(String text) {
            super(text);
        }

        private CannotBeMappedToTransitionException(String text, Throwable thr) {
            super(text, thr);
        }

    }

    public DomibusConnectorDomainMessageTransformerService(LargeFilePersistenceService largeFilePersistenceService, MessageProcessManager messageProcessManager) {
        this.largeFilePersistenceService = largeFilePersistenceService;
        this.messageProcessManager = messageProcessManager;
    }

    /**
     * Holds the current domibus connector message id / message processing id
     * is package private so can be access by Test
     */
    ThreadLocal<DC5MessageId> messageIdThreadLocal = new ThreadLocal<>();


    /**
     * transforms a message from domain model to
     * transition model
     * <p>
     * this method does not check if the message makes sense
     * <p>
     * <p>
     * DomibusConnectorMessage#getMessageDetails must be not null!
     *
     * @param domainMessage - the message (domain model)
     * @return - the transformed message (transition model)
     * @throws CannotBeMappedToTransitionException if a
     */
    public @NotNull
    DomibusConnectorMessageType transformDomainToTransition(final @NotNull DC5Message domainMessage) throws CannotBeMappedToTransitionException {
        LOGGER.debug("transformDomainToTransition: with domainMessage [{}]", domainMessage);
        if (domainMessage.getEbmsData() == null) {
            throw new CannotBeMappedToTransitionException("DomibusConnectorMessage.getMessageDetails() is not allowed to be null!");
        }
        DomibusConnectorMessageType toMessageType = new DomibusConnectorMessageType();

        //map messageDetails
        toMessageType.setMessageDetails(transformMessageDetailsDomainToTransition(domainMessage));

        //map message confirmations
        if (!domainMessage.isIncomingBusinessMessage() && domainMessage.getTransportedMessageConfirmation() != null) {
            toMessageType.getMessageConfirmations()
                    .add(transformMessageConfirmationDomainToTransition(domainMessage.getTransportedMessageConfirmation()));
        }

        if (domainMessage.isOutgoingBusinessMessage()) {
            Objects.requireNonNull(domainMessage.getMessageContent(), "A business message must have a business content!");
            Objects.requireNonNull(domainMessage.getMessageContent().getEcodexContent(), "A business message to GW must have a eCodex Content!");
            //map business XML
            toMessageType.setMessageContent(transformMessageContentDomainToTransition(domainMessage.getMessageContent().getEcodexContent().getBusinessXml()));
            //map asic-s container
            toMessageType.getMessageAttachments().add(mapAttachment(domainMessage.getMessageContent().getEcodexContent().getAsicContainer(), ASICS_CONTAINER_IDENTIFIER));
            //map xml-token
            toMessageType.getMessageAttachments().add(mapAttachment(domainMessage.getMessageContent().getEcodexContent().getAsicContainer(), TOKEN_XML_IDENTIFIER));
        }

        if (domainMessage.isIncomingBusinessMessage()) {
            Objects.requireNonNull(domainMessage.getMessageContent(), "A business message must have a business content!");
            Objects.requireNonNull(domainMessage.getMessageContent().getBusinessContent(), "A business message to Backend must have a Backend Content!");
            //map business xml
            toMessageType.setMessageContent(transformMessageContentDomainToTransition(domainMessage.getMessageContent().getBusinessContent().getBusinessXml()));
            //map trust token xml
            toMessageType.getMessageAttachments().add(transformMessageAttachmentDomainToTransition(domainMessage.getMessageContent().getBusinessContent().getTrustTokenXml()));
            //map trust token PDF
            toMessageType.getMessageAttachments().add(transformMessageAttachmentDomainToTransition(domainMessage.getMessageContent().getBusinessContent().getTrustTokenPDF()));
            //map attachments from asic-s container
            for (DC5MessageAttachment msgAttach : domainMessage.getMessageContent().getBusinessContent().getAttachments()) {
                toMessageType.getMessageAttachments()
                        .add(transformMessageAttachmentDomainToTransition(msgAttach));
            }
            //if it's a business message map all related confirmations
            for (DC5Confirmation confirmation : domainMessage.getMessageContent().getRelatedConfirmations()) {
                toMessageType.getMessageConfirmations()
                        .add(transformMessageConfirmationDomainToTransition(confirmation));
            }
        }


        return toMessageType;
    }

    private DomibusConnectorMessageAttachmentType mapAttachment(DC5MessageAttachment asicContainer, String asicsContainerIdentifier) {
        DomibusConnectorMessageAttachmentType messageAttachmentType = new DomibusConnectorMessageAttachmentType();
        messageAttachmentType.setMimeType(asicContainer.getMimeType());
        messageAttachmentType.setIdentifier(asicsContainerIdentifier);
        messageAttachmentType.setName(asicContainer.getName());
        messageAttachmentType.setAttachment(this.convertBigDataReferenceToDataHandler(asicContainer.getAttachment(), asicContainer.getMimeType()));
        return messageAttachmentType;
    }

    /**
     * converts a messageConfirmation from domain model to
     * transition model
     * the getEvidence byteArray must be not null!
     *
     * @param messageConfirmation the messageConfirmation
     * @return the messageConfirmation in transition model
     * @throws IllegalArgumentException is thrown if DomibusConnectorMessageConfirmation#getEvidence returns null or
     *                                  DomibusConnectorMessageConfirmation#getEvidenceType returns null
     */
    @NotNull
    DomibusConnectorMessageConfirmationType transformMessageConfirmationDomainToTransition(final @NotNull DC5Confirmation messageConfirmation) {
        DomibusConnectorMessageConfirmationType confirmationTO = new DomibusConnectorMessageConfirmationType();
        if (messageConfirmation.getEvidence() == null) {
            throw new CannotBeMappedToTransitionException("byte array getEvidence() is not allowed to be null!");
        }
        if (messageConfirmation.getEvidenceType() == null) {
            throw new CannotBeMappedToTransitionException("evidenceType is not allowed to be null!");
        }

        StreamSource streamSource = new StreamSource(new ByteArrayInputStream(
                //byte[] is copied because domain model is not immutable
                Arrays.copyOf(messageConfirmation.getEvidence(), messageConfirmation.getEvidence().length)));
        confirmationTO.setConfirmation(streamSource);

        //confirmationTO.setConfirmation(Arrays.copyOf(messageConfirmation.getEvidence(), messageConfirmation.getEvidence().length));        


        confirmationTO.setConfirmationType(DomibusConnectorConfirmationType.valueOf(messageConfirmation.getEvidenceType().name()));

        return confirmationTO;

    }

    /**
     * Translates messageError from domain model to transition model
     *
     * @param messageError - the (domain model) messageError
     * @return the translated (transition model) messageError
     */
    @NotNull
    DomibusConnectorMessageErrorType transformMessageErrorDomainToTransition(final @NotNull DomibusConnectorMessageError messageError) {
        DomibusConnectorMessageErrorType errorTO = new DomibusConnectorMessageErrorType();
        BeanUtils.copyProperties(messageError, errorTO);
        return errorTO;
    }


    /**
     * Translates messageAttachment from domain model to transition model
     * the attachment and identifier property must not be null!
     *
     * @param messageAttachment - the (domain model) messageAttachment
     * @return the translated (transition model) messageAttachment
     */
    @NotNull
    DomibusConnectorMessageAttachmentType transformMessageAttachmentDomainToTransition(final @NotNull DC5MessageAttachment messageAttachment) {
        DomibusConnectorMessageAttachmentType attachmentTO = new DomibusConnectorMessageAttachmentType();
        if (messageAttachment.getAttachment() == null) {
            throw new CannotBeMappedToTransitionException("attachment is not allowed to be null!");
        }
        if (messageAttachment.getIdentifier() == null) {
            throw new CannotBeMappedToTransitionException("identifier is not allowed to be null!");
        }
        attachmentTO.setName(messageAttachment.getName());
        attachmentTO.setIdentifier(messageAttachment.getIdentifier());
        attachmentTO.setMimeType(messageAttachment.getMimeType());
        attachmentTO.setDescription(messageAttachment.getDescription());

        attachmentTO.setAttachment(convertBigDataReferenceToDataHandler(messageAttachment.getAttachment(), messageAttachment.getMimeType()));
        return attachmentTO;
    }

    /**
     * Translates messageContent from domain model to transition model
     *
     * @param businessXml - the (domain model) messageContent
     * @return the translated (transition model) messageContent or null if null provided
     */
    @Nullable
    DomibusConnectorMessageContentType transformMessageContentDomainToTransition(final @Nullable DC5MessageAttachment businessXml
    ) {
        if (businessXml == null) {
            return null;
        }
        DomibusConnectorMessageContentType messageContentTO = new DomibusConnectorMessageContentType();
        if (businessXml.getAttachment() == null) {
            throw new CannotBeMappedToTransitionException("xmlContent of content must be not null!");
        }

//        if(LOGGER.isTraceEnabled()) {
//        	LOGGER.trace("Business content XML before transformed to stream: {}", new String(businessXml.getXmlContent()));
//        }
        StreamSource streamSource = null;
        try {
            streamSource = new StreamSource(            //byte[] is copied because domain model is not immutable
                    //                Arrays.copyOf(messageContent.getXmlContent(), messageContent.getXmlContent().length)
                    largeFilePersistenceService.getReadableDataSource(businessXml.getAttachment()).getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        messageContentTO.setXmlContent(streamSource);

        //maps Document of messageContent
//        DomibusConnectorMessageDocument document = messageContent.get
        DomibusConnectorMessageDocumentType documentTO = new DomibusConnectorMessageDocumentType();

//        if (document != null) {
//            LargeFileReference docDataRef = document.getDocument();
//            documentTO.setDocument(convertBigDataReferenceToDataHandler(docDataRef, null));
//            documentTO.setDocumentName(document.getDocumentName());
//            messageContentTO.setDocument(documentTO);
//
//            //map signature type of document
//            DetachedSignature detachedSignature = document.getDetachedSignature();
//            if (detachedSignature != null) {
//                DomibusConnectorDetachedSignatureType detachedSignatureTypeTO = new DomibusConnectorDetachedSignatureType();
//                detachedSignatureTypeTO.setDetachedSignature(
//                        Arrays.copyOf(detachedSignature.getDetachedSignature(), detachedSignature.getDetachedSignature().length));
//                detachedSignatureTypeTO.setDetachedSignatureName(detachedSignature.getDetachedSignatureName());
//                detachedSignatureTypeTO.setMimeType(
//                        DomibusConnectorDomainDetachedSignatureEnumTransformer
//                                .transformDetachedSignatureMimeTypeDomainToTransition(detachedSignature.getMimeType()));
//                documentTO.setDetachedSignature(detachedSignatureTypeTO);
//            } else {
//                LOGGER.debug("#transformMessageContentDomainToTransition: no detached signature to map!");
//            }
//        } else {
//            LOGGER.debug("#transformMessageContentDomainToTransition: document contains no document data");
//        }


        return messageContentTO;
    }


    @NotNull
    DataHandler convertBigDataReferenceToDataHandler(@NotNull LargeFileReference largeFileReference, @Nullable String mimeType) {
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        LargeFileReference readableDataSource = this.largeFilePersistenceService.getReadableDataSource(largeFileReference);
        DataHandler dataHandler = new DataHandler(readableDataSource);
        return dataHandler;
    }


    @NotNull
    DomibusConnectorMessageDetailsType transformMessageDetailsDomainToTransition(final @NotNull DC5Message message) {
        DC5Ebms ebmsData = message.getEbmsData();
        LOGGER.debug("transformMessageDetailsDomaintToTransition: messageDetails are [{}]", ebmsData);
        if (ebmsData == null) {
            throw new CannotBeMappedToTransitionException("messageDetails are not allowed to be null!");
        }
        DomibusConnectorMessageDetailsType toDetailsType = new DomibusConnectorMessageDetailsType();

        //map action
        DomibusConnectorActionType action = new DomibusConnectorActionType();
        action.setAction(ebmsData.getAction().getAction());
        toDetailsType.setAction(action);

        //map service
        DomibusConnectorServiceType service = new DomibusConnectorServiceType();
        service.setServiceType(ebmsData.getService().getServiceType());
        service.setService(ebmsData.getService().getService());
        toDetailsType.setService(service);

        //map ebms id
        toDetailsType.setEbmsMessageId(ebmsData.getEbmsMessageId().toString());

        //map conversation id
        toDetailsType.setConversationId(ebmsData.getConversationId());

        //map
        if (message.getTarget() == MessageTargetSource.GATEWAY) {
            toDetailsType.setRefToMessageId(ebmsData.getRefToEbmsMessageId().toString());

        } else if (message.getTarget() == MessageTargetSource.BACKEND) {
            boolean messageHasBackendMessageId = message.getBackendData() != null && message.getBackendData().getBackendMessageId() != null;
            if (messageHasBackendMessageId) {
                toDetailsType.setRefToMessageId(message.getBackendData().getBackendMessageId().toString());
            }
        }

        //map backend id
        boolean messageHasBackendMessageId = message.getBackendData() != null && message.getBackendData().getBackendMessageId() != null;
        if (messageHasBackendMessageId) {
            toDetailsType.setBackendMessageId(message.getBackendData().getBackendMessageId().toString());
        }

        //map sender/fromParty
        DC5Ebms.EbmsSender sender = ebmsData.getSender(message.getTarget());
        toDetailsType.setOriginalSender(sender.getOriginalSender());
        DomibusConnectorPartyType fromParty = new DomibusConnectorPartyType();
        fromParty.setRole(sender.getRole().getRole());
        fromParty.setPartyId(sender.getParty().getPartyId());
        fromParty.setPartyIdType(sender.getParty().getPartyIdType());
        toDetailsType.setFromParty(fromParty);

        //map recipient/toParty
        DC5Ebms.EbmsReceiver receiver = ebmsData.getReceiver(message.getTarget());
        toDetailsType.setFinalRecipient(receiver.getFinalRecipient());
        DomibusConnectorPartyType toParty = new DomibusConnectorPartyType();
        toParty.setRole(receiver.getRole().getRole());
        toParty.setPartyId(receiver.getParty().getPartyId());
        toParty.setPartyIdType(receiver.getParty().getPartyIdType());
        toDetailsType.setFromParty(toParty);


        return toDetailsType;
    }


    /**
     * @param toMessage - the TransitionMessage
     * @return the domainModel message
     */
    public @NotNull
    DC5Message transformTransitionToDomain(final @NotNull MessageTargetSource target,
                                           final @NotNull DomibusConnectorLinkPartner.LinkPartnerName sourcePartner,
                                           final @NotNull DomibusConnectorMessageType toMessage,
                                           final @NotNull DC5MessageId messageId) {
        Objects.requireNonNull(target, "MessageTarget must not be null!");

        messageIdThreadLocal.set(messageId);
        try {
            LOGGER.trace("#transformTransitionToDomain: transforming transition message object [{}] to domain message object", toMessage);
            DomibusConnectorMessageDetailsType messageDetailsTO = toMessage.getMessageDetails();


            DC5Ebms ebmsData = transformMessageDetailsTransitionToDomain(target, messageDetailsTO);
            DC5BackendData backendData = transformMessageDetailsToBackendData(target, messageDetailsTO);

            //DomibusConnectorMessage
            DC5Message.DC5MessageBuilder dc5MessageBuilder = DC5Message.builder();
            dc5MessageBuilder.process(messageProcessManager.getCurrentProcess());
            dc5MessageBuilder.source(target.getOpposite());
            dc5MessageBuilder.target(target);
            dc5MessageBuilder.connectorMessageId(messageId);
            if (target == MessageTargetSource.GATEWAY) {
                dc5MessageBuilder.gatewayLinkName(sourcePartner);
            } else if (target == MessageTargetSource.BACKEND) {
                dc5MessageBuilder.backendLinkName(sourcePartner);
            }

            //map ebms data
            dc5MessageBuilder.ebmsData(ebmsData);

            //map backend data
            dc5MessageBuilder.backendData(backendData);

            //map confirmations
            int numberOfConfirmations = toMessage.getMessageConfirmations().size();
            LOGGER.trace("#transformTransitionToDomain: transitionMessage has [{}] confirmations", numberOfConfirmations);
            if (numberOfConfirmations > 1) {
                throw new CannotBeMappedToTransitionException("A message is only allowed to transport 1 confirmation!");
            }
            if (numberOfConfirmations == 1) {
                dc5MessageBuilder.transportedMessageConfirmation(transformMessageConfirmationTransitionToDomain(toMessage.getMessageConfirmations().get(0)));
            }

            //map attachemnts, content
            boolean isBusinessMessage = !TransitionHelper.isConfirmationMessage(toMessage);
            if (isBusinessMessage) {
                DC5MessageContent.DC5MessageContentBuilder messageContentBuilder = DC5MessageContent.builder();
//                DC5MessageContent.DC5MessageContentBuilder contentBuilder = DC5MessageContent.builder();
                if (target == MessageTargetSource.GATEWAY) {
                    DC5BackendContent.DC5BackendContentBuilder backendContentBuilder = DC5BackendContent.builder();
                    backendContentBuilder.businessXml(transformMessageXmlContentToDomain(toMessage.getMessageContent()));
                    backendContentBuilder.businessDocument(transformBusinessContentToDomain(toMessage.getMessageContent()));
                    backendContentBuilder.attachments(transformAttachmentsToDomain(toMessage));

                    messageContentBuilder.businessContent(backendContentBuilder.build());
                }
                if (target == MessageTargetSource.BACKEND) {
                    DC5EcodexContent.DC5EcodexContentBuilder ecodexContentBuilder = DC5EcodexContent.builder();
                    ecodexContentBuilder.businessXml(transformMessageXmlContentToDomain(toMessage.getMessageContent()));
                    ecodexContentBuilder.trustTokenXml(transformMessageContentToTrustTokenDomain(toMessage));
                    ecodexContentBuilder.asicContainer(transformMessageToAsicContainerDomain(toMessage));

                }
                dc5MessageBuilder.messageContent(messageContentBuilder.build());

            }

            return dc5MessageBuilder.build();

        } finally {
            messageIdThreadLocal.remove();
        }
    }

    private DC5MessageAttachment transformMessageToAsicContainerDomain(DomibusConnectorMessageType toMessage) {
        DomibusConnectorMessageAttachmentType messageAttachmentType = toMessage.getMessageAttachments().stream()
                .filter(a -> StringUtils.equals(a.getIdentifier(), ASIC_S_IDENTIFIER))
                .findAny()
                .orElseThrow(() -> new CannotBeMappedToTransitionException("Message contains no TrustXML attachment (identifier name: [" + ASIC_S_IDENTIFIER + "])"));

        return transformMessageAttachmentTransitionToDomain(messageAttachmentType);
    }

    public static final String TRUST_TOKEN_XML_IDENTIFIER = "tokenXML";
    public static final String ASIC_S_IDENTIFIER = "ASIC-S";

    private DC5MessageAttachment transformMessageContentToTrustTokenDomain(DomibusConnectorMessageType toMessage) {
        DomibusConnectorMessageAttachmentType messageAttachmentType = toMessage.getMessageAttachments().stream()
                .filter(a -> StringUtils.equals(a.getIdentifier(), TRUST_TOKEN_XML_IDENTIFIER))
                .findAny()
                .orElseThrow(() -> new CannotBeMappedToTransitionException("Message contains no TrustXML attachment (identifier name: [" + TRUST_TOKEN_XML_IDENTIFIER + "])"));

        return transformMessageAttachmentTransitionToDomain(messageAttachmentType);
    }

    private Collection<? extends DC5MessageAttachment> transformAttachmentsToDomain(DomibusConnectorMessageType toMessage) {
        return toMessage.getMessageAttachments().stream()
                .map(this::transformMessageAttachmentTransitionToDomain)
                .collect(Collectors.toList());
    }

    private DC5BackendData transformMessageDetailsToBackendData(MessageTargetSource target, DomibusConnectorMessageDetailsType messageDetailsTO) {
        DC5BackendData.DC5BackendDataBuilder backendDataBuilder = DC5BackendData.builder();

        if (target == MessageTargetSource.GATEWAY) {
            backendDataBuilder.backendMessageId(BackendMessageId.ofString(messageDetailsTO.getBackendMessageId()));
        }
        if (target == MessageTargetSource.GATEWAY && StringUtils.isNotBlank(messageDetailsTO.getRefToMessageId())) {
            backendDataBuilder.refToBackendMessageId(BackendMessageId.ofString(messageDetailsTO.getRefToMessageId()));
        }

        return backendDataBuilder.build();
    }

    private void setMessageProcessProperties(DC5Message DC5Message, DomibusConnectorMessageType transitionMessage) {
        if (transitionMessage.getMessageContent() != null &&
                transitionMessage.getMessageContent().getDocument() != null &&
                transitionMessage.getMessageContent()
                        .getDocument()
                        .getAesType() != null
        ) {
            DomibusConnectorDocumentAESType aesType = transitionMessage.getMessageContent()
                    .getDocument()
                    .getAesType();

            AdvancedElectronicSystemType advancedElectronicSystemType = AdvancedElectronicSystemType.valueOf(aesType.name());

            DC5Message.getDcMessageProcessSettings()
                    .setValidationServiceName(advancedElectronicSystemType);
            LOGGER.trace("#transformTransitionToDomain: setting AES type to ", advancedElectronicSystemType);
        }
    }


    @NotNull
    DC5MessageAttachment transformMessageAttachmentTransitionToDomain(final @NotNull DomibusConnectorMessageAttachmentType messageAttachmentTO) {

        @NotNull LargeFileReference ref = convertDataHandlerToBigFileReference(messageAttachmentTO.getAttachment());
        return DC5MessageAttachment.builder()
                .attachment(ref)
                .identifier(messageAttachmentTO.getIdentifier())
                .name(messageAttachmentTO.getName())
                .mimeType(messageAttachmentTO.getMimeType())
                .description(messageAttachmentTO.getDescription())
                .size(ref.getSize())
                .digest(ref.getDigest())
                .build();

    }


    @NotNull
    DomibusConnectorMessageError transformMessageErrorTransitionToDomain(final @NotNull DomibusConnectorMessageErrorType errorTypeTO) {
        return DomibusConnectorMessageErrorBuilder.createBuilder()
                .setText(errorTypeTO.getErrorMessage())
                .setDetails(errorTypeTO.getErrorDetails())
                .setSource(errorTypeTO.getErrorSource())
                .build();
    }

    DC5Confirmation transformMessageConfirmationTransitionToDomain(final @NotNull DomibusConnectorMessageConfirmationType messageConfirmationTO) {
        DC5Confirmation confirmation = new DC5Confirmation();

        Source evidence = messageConfirmationTO.getConfirmation();
        if (evidence != null) {
            confirmation.setEvidence(ConversionTools.convertXmlSourceToByteArray(evidence));
        }
        confirmation.setEvidenceType(DomibusConnectorEvidenceType.valueOf(messageConfirmationTO.getConfirmationType().name()));

        return confirmation;
    }


    public static final String BUSINESS_XML_DEFAULT_NAME = "BUSINESS_XML";
    public static final String BUSINESS_XML_DEFAULT_IDENTIFIER = "BUSINESS_XML";
    public static final String BUSINESS_XML_DEFAULT_MIME_TYPE = MimeTypeUtils.TEXT_XML_VALUE;
    public static final String BUSINSS_DOC_DEFAULT_IDENTIFIER = "BUSINESS_DOC";
    public static final String DEFAULT_MIMETYPE = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;

    DC5MessageAttachment transformBusinessContentToDomain(final @NotNull DomibusConnectorMessageContentType messageContentTO) {
        DC5MessageAttachment.DC5MessageAttachmentBuilder attachmentBuilder = DC5MessageAttachment.builder();

        Objects.requireNonNull(messageContentTO.getDocument(), "Document must not be null!");

        if (messageContentTO.getDocument().getDetachedSignature() != null) {
            attachmentBuilder.DC5DetachedSignature(DC5DetachedSignature.builder()
                    .detachedSignature(messageContentTO.getDocument().getDetachedSignature().getDetachedSignature())
                    .detachedSignatureName(messageContentTO.getDocument().getDetachedSignature().getDetachedSignatureName())
                    .mimeType(DetachedSignatureMimeType.fromCode(messageContentTO.getDocument().getDetachedSignature().getMimeType().value()))
                    .build());
        }

        attachmentBuilder.name(messageContentTO.getDocument().getDocumentName());

        LargeFileReference largeFileReference = convertDataHandlerToBigFileReference(messageContentTO.getDocument().getDocument());
        largeFileReference.setName(messageContentTO.getDocument().getDocumentName());
        attachmentBuilder.size(largeFileReference.getSize());
        attachmentBuilder.digest(largeFileReference.getDigest());
        attachmentBuilder.identifier(BUSINSS_DOC_DEFAULT_IDENTIFIER);
        attachmentBuilder.mimeType(largeFileReference.getMimetype());

        return attachmentBuilder.build();
    }

    DC5MessageAttachment transformMessageXmlContentToDomain(final @NotNull DomibusConnectorMessageContentType messageContentTO) {
        DC5MessageAttachment.DC5MessageAttachmentBuilder attachmentBuilder = DC5MessageAttachment.builder();

        byte[] result = ConversionTools.convertXmlSourceToByteArray(messageContentTO.getXmlContent());

        LargeFileReference businessXml = largeFilePersistenceService.createDomibusConnectorBigDataReference(this.messageIdThreadLocal.get(), BUSINESS_XML_DEFAULT_NAME, BUSINESS_XML_DEFAULT_MIME_TYPE);


        try (OutputStream os = businessXml.getOutputStream();
             CountingOutputStream countingOutputStream = new CountingOutputStream(os);
             DigestOutputStream digestOutputStream = new DigestOutputStream(countingOutputStream, getDefaultMessageDigest());) {
            StreamUtils.copy(result, digestOutputStream);
            attachmentBuilder.attachment(businessXml);
            attachmentBuilder.digest(Digest.ofMessageDigest(digestOutputStream.getMessageDigest()));
            attachmentBuilder.identifier(BUSINESS_XML_DEFAULT_IDENTIFIER);
            attachmentBuilder.name(BUSINESS_XML_DEFAULT_NAME);
            attachmentBuilder.size(countingOutputStream.getByteCount());

            return attachmentBuilder.build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageDigest getDefaultMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256"); //TODO: put into configuration properties!
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    LargeFileReference convertDataHandlerToBigFileReference(DataHandler dataHandler) {
        DC5MessageId DC5MessageId = messageIdThreadLocal.get();
        LargeFileReference dataRef = largeFilePersistenceService.createDomibusConnectorBigDataReference(DC5MessageId, dataHandler.getName(), dataHandler.getContentType());
        try (InputStream is = dataHandler.getInputStream();
             DigestInputStream digestInputStream = new DigestInputStream(is, getDefaultMessageDigest());
             CountingInputStream countingInputStream = new CountingInputStream(digestInputStream);
             OutputStream os = dataRef.getOutputStream()) {
            StreamUtils.copy(countingInputStream, os);
            dataRef.setSize(countingInputStream.getByteCount());
            dataRef.setDigest(Digest.ofMessageDigest(digestInputStream.getMessageDigest()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataRef;

    }

    @NotNull
    DC5Ebms transformMessageDetailsTransitionToDomain(
            final @NotNull MessageTargetSource target,
            final @NotNull DomibusConnectorMessageDetailsType messageDetailsTO) {

        DC5Ebms.DC5EbmsBuilder ebmsBuilder = DC5Ebms.builder();

        //map service
        ebmsBuilder.service(DC5Service.builder()
                .service(messageDetailsTO.getService().getService())
                .serviceType(messageDetailsTO.getService().getServiceType())
                .build());
        //map action
        ebmsBuilder.action(DC5Action.builder()
                .action(messageDetailsTO.getAction().getAction())
                .build());

        DC5Partner.builder()
                .partnerRole(DC5Role.builder()
                        .role(messageDetailsTO.getFromParty().getRole())
                        .roleType(DC5RoleType.INITIATOR)
                        .build());

        DomibusConnectorPartyType fromParty = messageDetailsTO.getFromParty();
        ebmsBuilder.initiator(DC5Partner.builder()
                .partnerRole(DC5Role.builder()
                        .role(fromParty.getRole())
                        .roleType(DC5RoleType.INITIATOR)
                        .build()).build());

        DomibusConnectorPartyType toParty = messageDetailsTO.getFromParty();
        ebmsBuilder.initiator(DC5Partner.builder().partnerRole(DC5Role.builder()
                        .role(toParty.getRole())
                        .roleType(DC5RoleType.RESPONDER)
                        .build())
                .build());

        DC5EcxAddress.DC5EcxAddressBuilder fromAddrBuilder = DC5EcxAddress.builder()
                .party(DC5Party.builder()
                        .partyId(fromParty.getPartyId())
                        .partyIdType(fromParty.getPartyIdType())
                        .build());

        DC5EcxAddress.DC5EcxAddressBuilder toAddrBuilder = DC5EcxAddress.builder()
                .party(DC5Party.builder()
                        .partyId(fromParty.getPartyId())
                        .partyIdType(fromParty.getPartyIdType())
                        .build());

        if (target == MessageTargetSource.GATEWAY) {
            ebmsBuilder.responder(DC5Partner.builder()
                    .partnerAddress(toAddrBuilder
                            .ecxAddress(messageDetailsTO.getFinalRecipient())
                            .build())
                    .build());

            ebmsBuilder.initiator(DC5Partner.builder()
                    .partnerAddress(fromAddrBuilder
                            .ecxAddress(messageDetailsTO.getOriginalSender())
                            .build())
                    .build());
        }
        if (target == MessageTargetSource.BACKEND) {
            ebmsBuilder.responder(DC5Partner.builder()
                    .partnerAddress(fromAddrBuilder
                            .ecxAddress(messageDetailsTO.getOriginalSender())
                            .build())
                    .build());

            ebmsBuilder.initiator(DC5Partner.builder().partnerAddress(toAddrBuilder
                            .ecxAddress(messageDetailsTO.getFinalRecipient())
                            .build())
                    .build());
        }

        //map ebms id
        if (target == MessageTargetSource.BACKEND) {
            ebmsBuilder.ebmsMessageId(EbmsMessageId.ofString(messageDetailsTO.getEbmsMessageId()));
        }
        //map refToEbmsId if message comes from GW
        if (target == MessageTargetSource.BACKEND && StringUtils.isNotBlank(messageDetailsTO.getRefToMessageId())) {
            ebmsBuilder.refToEbmsMessageId(EbmsMessageId.ofString(messageDetailsTO.getRefToMessageId()));
        }
        //map conversation id
        ebmsBuilder.conversationId(messageDetailsTO.getConversationId());

        return ebmsBuilder.build();

    }

}