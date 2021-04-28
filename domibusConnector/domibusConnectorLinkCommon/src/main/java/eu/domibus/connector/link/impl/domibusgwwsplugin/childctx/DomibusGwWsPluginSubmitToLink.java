package eu.domibus.connector.link.impl.domibusgwwsplugin.childctx;

import eu.domibus.common.model.org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.*;
import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.link.service.SubmitToLinkPartner;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.common.DomibusGwConstants;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.plugin.webService.generated.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeTypeUtils;

import javax.activation.DataHandler;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class DomibusGwWsPluginSubmitToLink implements SubmitToLinkPartner {

    private static final Logger LOGGER = LogManager.getLogger(DomibusGwWsPluginSubmitToLink.class);

    @Autowired
    DomibusGwWsPluginBackendInterfaceFactory backendInterfaceFactory;

    @Autowired
    TransportStateService transportStateService;

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Override
    public void submitToLink(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) throws DomibusConnectorSubmitToLinkException {
        Optional<ActiveLinkPartner> activeLinkPartnerByName = linkManagerService.getActiveLinkPartnerByName(linkPartnerName);
        DomibusGwWsPluginActiveLinkPartner activeLinkPartner = (DomibusGwWsPluginActiveLinkPartner) activeLinkPartnerByName.get();
        BackendInterface backendInterface = backendInterfaceFactory.getWebserviceProxyClient(activeLinkPartner);

        TransportStateService.TransportId transportId = transportStateService.createTransportFor(message, linkPartnerName);

        SubmitRequest submitRequest = new SubmitRequest();
//        LargePayloadType largePayloadType = new LargePayloadType();

        Messaging msg = createMessage(message);

        if (DomainModelHelper.isEvidenceMessage(message)) {
            //nothing todo...
        } else {
            mapBusinessMsg(message, submitRequest);
        }
        mapAttachments(message, submitRequest);
        mapMessageConfirmations(message, submitRequest);

        TransportStateService.DomibusConnectorTransportState transportState = new TransportStateService.DomibusConnectorTransportState();
        transportState.setConnectorTransportId(transportId);
        transportState.setConnectorMessageId(new DomibusConnectorMessageId(message.getConnectorMessageIdAsString()));


        try {
            SubmitResponse submitResponse = backendInterface.submitMessage(submitRequest, msg);
            java.lang.String gatewayMessageId = submitResponse.getMessageID().get(0);
            transportState.setStatus(TransportState.ACCEPTED);
            transportState.setRemoteMessageId(gatewayMessageId);
        } catch (SubmitMessageFault submitMessageFault) {
            transportState.setStatus(TransportState.FAILED);
            transportState.addMessageError(DomibusConnectorMessageErrorBuilder.createBuilder()
                    .setDetails(submitMessageFault)
                    .setSource(this.getClass())
                    .setText("Failed to submit Message to DomibusGW")
                    .build()
            );
        }
        transportStateService.updateTransportStatus(transportState);

    }

    private Messaging createMessage(DomibusConnectorMessage message) {
        Messaging msg = new Messaging();
        UserMessage userMessage = new UserMessage();

        //party info
        PartyInfo partyInfo = new PartyInfo();
        //from gw
        From fromGw = new From();
        PartyId fromPartyId = new PartyId();
        fromPartyId.setValue(message.getMessageDetails().getFromParty().getPartyId());
        fromPartyId.setType(message.getMessageDetails().getFromParty().getPartyIdType());
        fromGw.setPartyId(fromPartyId);
        fromGw.setRole(message.getMessageDetails().getFromParty().getRole());
        partyInfo.setFrom(fromGw);
        //to gw
        To toGw = new To();
        PartyId toPartyId = new PartyId();
        toPartyId.setValue(message.getMessageDetails().getToParty().getPartyId());
        toPartyId.setType(message.getMessageDetails().getToParty().getPartyIdType());
        toGw.setPartyId(toPartyId);
        toGw.setRole(message.getMessageDetails().getToParty().getRole());
        partyInfo.setTo(toGw);
        userMessage.setPartyInfo(partyInfo);
        //set collaboration info, service, action, conversationId
        CollaborationInfo cInfo = new CollaborationInfo();
        cInfo.setAction(message.getMessageDetails().getAction().getAction());
        Service service = new Service();
        service.setValue(message.getMessageDetails().getService().getService());
        service.setType(message.getMessageDetails().getService().getServiceType());
        cInfo.setService(service);
        cInfo.setConversationId(message.getMessageDetails().getConversationId());
        userMessage.setCollaborationInfo(cInfo);
        //message info,
        MessageInfo msgInfo = new MessageInfo();
        msgInfo.setMessageId(message.getMessageDetails().getEbmsMessageId());
        msgInfo.setRefToMessageId(message.getMessageDetails().getRefToMessageId());
        userMessage.setMessageInfo(msgInfo);
        //message properties, finalRecipient, originalSender
        MessageProperties msgProperties = new MessageProperties();
        Property originalSender = new Property();
        originalSender.setName("originalSender");
        originalSender.setValue(message.getMessageDetails().getOriginalSender());
        msgProperties.getProperty().add(originalSender);
        Property finalRecipient = new Property();
        finalRecipient.setName("finalRecipient");
        finalRecipient.setValue(message.getMessageDetails().getFinalRecipient());
        msgProperties.getProperty().add(finalRecipient);
        userMessage.setMessageProperties(msgProperties);

        //setUserMessage
        msg.setUserMessage(userMessage);

        PayloadInfo payloadInfo = new PayloadInfo();
        userMessage.setPayloadInfo(payloadInfo);
        return msg;
    }

    private void mapMessageConfirmations(DomibusConnectorMessage message, SubmitRequest submitRequest) {
        message.getTransportedMessageConfirmations().forEach(c -> this.mapConfirmation(c, submitRequest));
    }

    private void mapConfirmation(DomibusConnectorMessageConfirmation c, SubmitRequest submitRequest) {
        java.lang.String confirmationPayloadId = generateCID();
        LargePayloadType confirmationPayload = new LargePayloadType();
        confirmationPayload.setContentType(MimeTypeUtils.TEXT_XML_VALUE);
        confirmationPayload.setPayloadId(confirmationPayloadId);
        confirmationPayload.setValue(convertByteArrayToDataHandler(c.getEvidence(), MimeTypeUtils.TEXT_XML_VALUE));
        submitRequest.getPayload().add(confirmationPayload);

        PartInfo xmlPartInfo = new PartInfo();
        xmlPartInfo.setHref(confirmationPayloadId);
        xmlPartInfo.setPartProperties(new PartPropertiesBuilder()
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_NAME, c.getEvidenceType().name())
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_DESCRIPTION, c.getEvidenceType().name())
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_MIME_TYPE, MimeTypeUtils.TEXT_XML_VALUE)
                .build()
        );
    }

    private void mapAttachments(DomibusConnectorMessage message, SubmitRequest submitRequest) {
        message.getMessageAttachments().forEach(a -> this.mapAttachment(a, submitRequest));
    }

    private void mapAttachment(DomibusConnectorMessageAttachment a, SubmitRequest submitRequest) {
        java.lang.String attachmentPayloadId = generateCID();
        LargePayloadType attachmentPayload = new LargePayloadType();
        attachmentPayload.setContentType(a.getMimeType());
        attachmentPayload.setPayloadId(attachmentPayloadId);
        attachmentPayload.setValue(new DataHandler(a.getAttachment()));
        submitRequest.getPayload().add(attachmentPayload);

        PartInfo xmlPartInfo = new PartInfo();
        xmlPartInfo.setHref(attachmentPayloadId);
        xmlPartInfo.setPartProperties(new PartPropertiesBuilder()
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_NAME, a.getName())
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_DESCRIPTION, a.getDescription())
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_MIME_TYPE, a.getMimeType())
                .build()
        );
    }

    private void mapBusinessMsg(DomibusConnectorMessage message, SubmitRequest submitRequest) {
        java.lang.String asicsPayloadId = generateCID();
        DataHandler dh = new DataHandler(message.getMessageContent().getDocument().getDocument());
        LargePayloadType asicsPayload = new LargePayloadType();
        asicsPayload.setContentType(DomibusGwConstants.ASIC_S_MIMETYPE);
        asicsPayload.setPayloadId(asicsPayloadId);
        asicsPayload.setValue(dh);

        submitRequest.getPayload().add(asicsPayload);

        PartInfo asicsPartInfo = new PartInfo();
        asicsPartInfo.setHref(asicsPayloadId);
        asicsPartInfo.setPartProperties(new PartPropertiesBuilder()
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_DESCRIPTION, DomibusGwConstants.ASIC_S_DESCRIPTION_NAME)
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_NAME, DomibusGwConstants.ASIC_S_DESCRIPTION_NAME)
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_MIME_TYPE, DomibusGwConstants.ASIC_S_MIMETYPE)
                .build()
        );

        java.lang.String xmlPayloadId = generateCID();
        LargePayloadType xmlPayload = new LargePayloadType();
        xmlPayload.setContentType(MimeTypeUtils.APPLICATION_XML_VALUE);
        xmlPayload.setPayloadId(xmlPayloadId);
        xmlPayload.setValue(convertByteArrayToDataHandler(message.getMessageContent().getXmlContent(), MimeTypeUtils.TEXT_XML_VALUE));
        submitRequest.getPayload().add(xmlPayload);

        PartInfo xmlPartInfo = new PartInfo();
        xmlPartInfo.setHref(xmlPayloadId);
        xmlPartInfo.setPartProperties(new PartPropertiesBuilder()
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_DESCRIPTION, DomibusGwConstants.MESSAGE_CONTENT_DESCRIPTION_NAME)
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_NAME, DomibusGwConstants.MESSAGE_CONTENT_DESCRIPTION_NAME)
                .addProperty(DomibusGwWsPluginConstants.PART_PROPERTY_MIME_TYPE, MimeTypeUtils.TEXT_XML_VALUE)
                .build()
        );

    }

    /**
     * converts a byte[] by creating a copy of the provided byte array
     * (because byte array is not immutable) and passing this byte array to
     * DataHandler constructor
     *
     * @param array    - the byte array
     * @param mimeType - the provided mimeType, can be null, if null
     *                 "application/octet-stream" mimeType will be set
     * @return the DataHandler
     */
    @NotNull
    DataHandler convertByteArrayToDataHandler(@NotNull byte[] array, @Nullable java.lang.String mimeType) {
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        DataHandler dataHandler = new DataHandler(Arrays.copyOf(array, array.length), mimeType);
        return dataHandler;
    }

    private class PartPropertiesBuilder {
        private PartProperties partProperties = new PartProperties();

        PartPropertiesBuilder addProperty(java.lang.String name, java.lang.String value) {
            Property p = new Property();
            p.setName(name);
            p.setValue(value);
            partProperties.getProperty().add(p);
            return this;
        }

        PartPropertiesBuilder addProperty(java.lang.String name, java.lang.String value, java.lang.String type) {
            Property p = new Property();
            p.setName(name);
            p.setValue(value);
            p.setType(type);
            partProperties.getProperty().add(p);
            return this;
        }

        PartProperties build() {
            return this.partProperties;
        }
    }

    private java.lang.String generateCID() {
        java.lang.String cid = "cid:payload_" + UUID.randomUUID().toString();
        return cid;
    }


}
