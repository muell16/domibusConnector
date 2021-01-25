package eu.domibus.connector.link.impl.domibusgwwsplugin.childctx;


import eu.domibus.common.model.org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.*;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.PullFromLink;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.domain.transition.DomibusConnectorConfirmationType;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.common.DomibusGwConstants;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.persistence.largefiles.provider.LargeFilePersistenceProvider;
import eu.domibus.plugin.webService.generated.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import javax.activation.DataHandler;
import javax.xml.ws.Holder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DomibusGwWsPluginPullFromGw implements PullFromLink {

    private static final Logger LOGGER = LogManager.getLogger(DomibusGwWsPluginPullFromGw.class);

    @Autowired
    DomibusGwWsPluginBackendInterfaceFactory backendInterfaceFactory;

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    private LargeFilePersistenceProvider largeFilePersistenceProvider;

    @Autowired
    private DomibusConnectorMessageIdGenerator idGenerator;


    @Override
    public void pullMessagesFrom(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        Optional<ActiveLinkPartner> activeLinkPartnerByName = linkManagerService.getActiveLinkPartnerByName(linkPartnerName);
        DomibusGwWsPluginActiveLinkPartner activeLinkPartner = (DomibusGwWsPluginActiveLinkPartner) activeLinkPartnerByName.get();
        BackendInterface backendInterface = backendInterfaceFactory.getWebserviceProxyClient(activeLinkPartner);


        ListPendingMessagesResponse listPendingMessagesResponse = backendInterface.listPendingMessages(new Object());

        listPendingMessagesResponse.getMessageID().forEach(id -> this.pullMessage(backendInterface, id));
    }


    private void pullMessage(BackendInterface backendInterface, String messageId) {
        RetrieveMessageRequest retrieveMessageRequest = new RetrieveMessageRequest();
        retrieveMessageRequest.setMessageID(messageId);

        Holder<RetrieveMessageResponse> retrieveMessageResponseHolder = new Holder<>();
        Holder<Messaging> messagingHolder = new Holder<Messaging>();

        DomibusConnectorMessage.DomibusConnectorMessageId dcMsgId = idGenerator.generateDomibusConnectorMessageId();

        try {
            backendInterface.retrieveMessage(retrieveMessageRequest, retrieveMessageResponseHolder, messagingHolder);
            RetrieveMessageResponse messaging = retrieveMessageResponseHolder.value;
            Messaging msgPayloads = messagingHolder.value;

            DomibusConnectorMessageBuilder msgBuilder = DomibusConnectorMessageBuilder.createBuilder();
            msgBuilder.setConnectorMessageId(dcMsgId);

            mapMessageDetails(msgBuilder, messaging, msgPayloads);
            mapPayloads(dcMsgId, msgBuilder, messaging, msgPayloads);
            //any body content is ignored...



        } catch (RetrieveMessageFault retrieveMessageFault) {
            LOGGER.error("Error occured while retrieving message with ID [{}] from GW", messageId);
        } catch (Exception ex) {
            LOGGER.error("Error occured", ex);
        }
    }

    private void mapPayloads(DomibusConnectorMessage.DomibusConnectorMessageId dcMsgId, DomibusConnectorMessageBuilder msgBuilder, RetrieveMessageResponse response, Messaging messaging) {
        Map<String, PartInfo> partInfoMap = messaging
                .getUserMessage()
                .getPayloadInfo()
                .getPartInfo()
                .stream()
                .collect(Collectors.toMap(p -> p.getHref(), Function.identity()));

        DomibusConnectorMessageContentBuilder contentBuilder = DomibusConnectorMessageContentBuilder.createBuilder();
        response.getPayload().forEach(p -> this.mapPayload(dcMsgId, contentBuilder, msgBuilder, partInfoMap, p));
    }

    private static final Set<String> ConfirmationNames = Arrays.stream(DomibusConnectorConfirmationType.values()).map(c -> c.value()).collect(Collectors.toSet());

    private void mapPayload(DomibusConnectorMessage.DomibusConnectorMessageId dcMsgId, DomibusConnectorMessageContentBuilder contentBuilder, DomibusConnectorMessageBuilder msgBuilder, Map<String, PartInfo> partInfoMap, LargePayloadType largePayloadType) {
        String payloadId = largePayloadType.getPayloadId();
        PartInfo partInfo = partInfoMap.get(payloadId);
        if (partInfo == null) {
            throw new IllegalArgumentException(String.format("No payload found with ID [%s]", payloadId));
        }
        Optional<String> description = getPropertyValueByName(partInfo, DomibusGwWsPluginConstants.PART_PROPERTY_DESCRIPTION);
        Optional<String> name = getPropertyValueByName(partInfo, DomibusGwWsPluginConstants.PART_PROPERTY_NAME);
        Optional<String> mimeType = getPropertyValueByName(partInfo, DomibusGwWsPluginConstants.PART_PROPERTY_MIME_TYPE);
        if (!(description.isPresent() || name.isPresent())) {
            LOGGER.warn("Payload without description OR name found! Ignoring this payload!");
            return;
        }
        String desc = description.orElse(name.get());

        if (desc.equals(DomibusGwConstants.ASIC_S_DESCRIPTION_NAME)) {
            mapAsics(dcMsgId, contentBuilder, msgBuilder, partInfo, largePayloadType);
        } else if (desc.equals(DomibusGwConstants.MESSAGE_CONTENT_DESCRIPTION_NAME)) {
            mapBusinessXml(contentBuilder, msgBuilder, partInfo, largePayloadType);
        } else if (ConfirmationNames.contains(desc)){
            mapConfirmationXml(desc, msgBuilder, partInfo, largePayloadType);
        } else {
            //Ignore anything else..
            LOGGER.warn("Received an unknown payload with name or description [{}] ", desc);
        }

    }

    private void mapConfirmationXml(String name, DomibusConnectorMessageBuilder msgBuilder, PartInfo partInfo, LargePayloadType largePayloadType) {
        msgBuilder.addConfirmation(DomibusConnectorMessageConfirmationBuilder.createBuilder()
                .setEvidence(convertDataHandlerToByteArray(largePayloadType.getValue()))
                .setEvidenceType(DomibusConnectorEvidenceType.valueOf(name))
        .build());
    }

    private void mapBusinessXml(DomibusConnectorMessageContentBuilder contentBuilder, DomibusConnectorMessageBuilder msgBuilder, PartInfo partInfo, LargePayloadType largePayloadType) {
        contentBuilder.setXmlContent(convertDataHandlerToByteArray(largePayloadType.getValue()));
    }

    private byte[] convertDataHandlerToByteArray(DataHandler value) {
        try (InputStream is = value.getInputStream();) {
            return StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            LOGGER.error("Exception occured", e);
            return new byte[0];
        }
    }

    private void mapAsics(DomibusConnectorMessage.DomibusConnectorMessageId dcMsgId, DomibusConnectorMessageContentBuilder contentBuilder, DomibusConnectorMessageBuilder msgBuilder, PartInfo partInfo, LargePayloadType largePayloadType) {
        LargeFileReference docRef  =
                largeFilePersistenceProvider.createDomibusConnectorBigDataReference(dcMsgId.getConnectorMessageId(), DomainModelHelper.ASICS_CONTAINER_IDENTIFIER, DomibusGwConstants.ASIC_S_MIMETYPE);

        copyDataHandlerToLargeFileRef(largePayloadType, docRef);

        contentBuilder.setDocument(
                DomibusConnectorMessageDocumentBuilder.createBuilder()
                        .setContent(docRef)
                        .setName(DomainModelHelper.ASICS_CONTAINER_IDENTIFIER)
                        .build()
        );
    }

    private void copyDataHandlerToLargeFileRef(LargePayloadType largePayloadType, LargeFileReference docRef) {
        DataHandler dh = largePayloadType.getValue();
        try (InputStream is = dh.getInputStream(); java.io.OutputStream os = docRef.getOutputStream()) {
            StreamUtils.copy(is, os);
        } catch (IOException e) {
            LOGGER.error("Exception occured!", e);
        }
    }

//    @NotNull
//    private LargeFileReference convertDataHandlerToBigFileReference(DataHandler dataHandler) {
//
//    }

    private Optional<String> getPropertyValueByName(PartInfo partInfo, String name) {
        return partInfo.getPartProperties().getProperty().stream()
                .filter(p -> name.equals(p.getName()))
                .map(p -> p.getValue())
                .findFirst();
    }

    private void mapMessageDetails(DomibusConnectorMessageBuilder msgBuilder, RetrieveMessageResponse response, Messaging msgPayloads) {
        From fromParty = msgPayloads.getUserMessage().getPartyInfo().getFrom();
        To toParty = msgPayloads.getUserMessage().getPartyInfo().getTo();

        String finalRecipient = msgPayloads.getUserMessage().getMessageProperties().getProperty().stream()
                .filter(p -> DomibusGwWsPluginConstants.FINAL_RECIPIENT_PROPERTY_NAME.equals(p.getName()))
                .map(Property::getValue)
                .findFirst()
                .get();

        String originalSender = msgPayloads.getUserMessage().getMessageProperties().getProperty().stream()
                .filter(p -> DomibusGwWsPluginConstants.ORIGINAL_SENDER_PROPERTY_NAME.equals(p.getName()))
                .map(Property::getValue)
                .findFirst()
                .get();

        msgBuilder.setMessageDetails(
                DomibusConnectorMessageDetailsBuilder.create()
                .withAction(msgPayloads.getUserMessage().getCollaborationInfo().getAction())
                .withFromParty(DomibusConnectorPartyBuilder.createBuilder()
                        .setPartyId(fromParty.getPartyId().getValue())
                        .withPartyIdType(fromParty.getPartyId().getType())
                        .setRole(fromParty.getRole())
                        .build()
                )
                .withToParty(DomibusConnectorPartyBuilder.createBuilder()
                        .setPartyId(toParty.getPartyId().getValue())
                        .withPartyIdType(toParty.getPartyId().getType())
                        .setRole(toParty.getRole())
                        .build()
                )
                .withService(msgPayloads.getUserMessage().getCollaborationInfo().getService().getValue(),
                        msgPayloads.getUserMessage().getCollaborationInfo().getService().getType())
                .withConversationId(msgPayloads.getUserMessage().getCollaborationInfo().getConversationId())
                .withEbmsMessageId(msgPayloads.getUserMessage().getMessageInfo().getMessageId())
                .withRefToMessageId(msgPayloads.getUserMessage().getMessageInfo().getRefToMessageId())
                .withFinalRecipient(finalRecipient)
                .withOriginalSender(originalSender)
                .build());

    }

}
