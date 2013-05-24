package eu.ecodex.connector.gwc.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

import backend.ecodex.org._1_0.SendRequest;
import backend.ecodex.org._1_0.SendResponse;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ECodexConnectorPayload;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ECodexPayloadType;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ObjectFactory;
import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class SendMessageHelper {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SendMessageHelper.class);

    private static final String XML_MIME_TYPE = "text/xml";
    private static final String PDF_MIME_TYPE = "application/octet-stream";
    private static final String CONTENT_XML_NAME = "ECodexContentXML";
    private static final String CONTENT_PDF_NAME = "ContentPDF";
    private static final ObjectFactory connectorPayloadObjectFactory = new ObjectFactory();

    private ECodexConnectorProperties connectorProperties;
    private ECodexConnectorPersistenceService persistenceService;

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void buildMessage(SendRequest request, Messaging ebMSHeaderInfo, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = new UserMessage();

        buildSendRequestAndPayloadInfo(userMessage, request, message);

        userMessage.setMessageProperties(buildMessageProperties(message));

        userMessage.setPartyInfo(buildPartyInfo(message.getMessageDetails()));

        userMessage.setCollaborationInfo(buildCollaborationInfo(message.getMessageDetails()));

        MessageInfo info = new MessageInfo();

        info.setRefToMessageId(message.getMessageDetails().getRefToMessageId());

        userMessage.setMessageInfo(info);

        ebMSHeaderInfo.getUserMessage().add(userMessage);
    }

    private MessageProperties buildMessageProperties(Message message) {
        if (message.getMessageDetails().getFinalRecipient() == null
                && message.getMessageDetails().getOriginalSender() == null) {
            return null;
        }
        MessageProperties mp = new MessageProperties();

        if (message.getMessageDetails().getFinalRecipient() != null) {

            Property finalRecipient = new Property();
            finalRecipient.setName("finalRecipient");
            finalRecipient.setValue(message.getMessageDetails().getFinalRecipient());
            mp.getProperty().add(finalRecipient);
        }

        if (message.getMessageDetails().getOriginalSender() != null) {
            Property originalSender = new Property();
            originalSender.setName("originalSender");
            originalSender.setValue(message.getMessageDetails().getOriginalSender());
            mp.getProperty().add(originalSender);
        }

        return mp;
    }

    private void buildSendRequestAndPayloadInfo(UserMessage userMessage, SendRequest request, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {
        PayloadInfo pli = new PayloadInfo();

        MessageContent messageContent = message.getMessageContent();
        if (messageContent != null && messageContent.getECodexContent() != null
                && messageContent.getECodexContent().length > 0) {
            DataHandler payload;
            try {
                payload = buildECodexConnectorPayload(messageContent.getECodexContent(), CONTENT_XML_NAME,
                        XML_MIME_TYPE, ECodexPayloadType.CONTENT_XML);
            } catch (Exception e) {
                throw new ECodexConnectorGatewayWebserviceClientException("Could not build Payload for content XML!", e);
            }
            request.getPayload().add(payload);
            pli.getPartInfo().add(buildPartInfo(CONTENT_XML_NAME));
        }

        boolean asicsFound = false;
        if (message.getAttachments() != null) {
            for (MessageAttachment attachment : message.getAttachments()) {
                if (attachment.getAttachment() != null && attachment.getAttachment().length > 0) {
                    DataHandler payload;
                    try {
                        payload = buildECodexConnectorPayload(attachment.getAttachment(), attachment.getName(),
                                attachment.getMimeType(), ECodexPayloadType.ATTACHMENT);

                    } catch (Exception e) {
                        throw new ECodexConnectorGatewayWebserviceClientException(
                                "Could not build Payload for attachment " + attachment.getName(), e);
                    }
                    request.getPayload().add(payload);
                    pli.getPartInfo().add(buildPartInfo(attachment.getName()));

                    if (attachment.getName().endsWith(".asics")) {
                        asicsFound = true;
                    }
                }
            }
        }

        if (!asicsFound && messageContent != null && messageContent.getPdfDocument() != null
                && messageContent.getPdfDocument().length > 0) {
            DataHandler payload;
            try {
                payload = buildECodexConnectorPayload(messageContent.getECodexContent(), CONTENT_PDF_NAME,
                        PDF_MIME_TYPE, ECodexPayloadType.CONTENT_PDF);
            } catch (Exception e) {
                throw new ECodexConnectorGatewayWebserviceClientException("Could not build Payload for content XML!", e);
            }
            request.getPayload().add(payload);
            pli.getPartInfo().add(buildPartInfo(CONTENT_XML_NAME));
        }

        if (message.getConfirmations() != null) {
            // Pick only the last produced evidence
            MessageConfirmation messageConfirmation = message.getConfirmations().get(
                    message.getConfirmations().size() - 1);

            if (checkMessageConfirmationValid(messageConfirmation)) {
                DataHandler payload;
                try {
                    payload = buildECodexConnectorPayload(messageConfirmation.getEvidence(), messageConfirmation
                            .getEvidenceType().toString(), XML_MIME_TYPE, ECodexPayloadType.EVIDENCE);
                } catch (Exception e) {
                    throw new ECodexConnectorGatewayWebserviceClientException("Could not build Payload for evidence "
                            + messageConfirmation.getEvidenceType().toString(), e);
                }

                request.getPayload().add(payload);
                pli.getPartInfo().add(buildPartInfo(messageConfirmation.getEvidenceType().toString()));
            }
        }

        userMessage.setPayloadInfo(pli);

        if (request.getPayload().isEmpty()) {
            throw new ECodexConnectorGatewayWebserviceClientException("No payload to send. Message without content?");
        }
    }

    private boolean checkMessageConfirmationValid(MessageConfirmation messageConfirmation) {
        return messageConfirmation != null && messageConfirmation.getEvidence() != null
                && messageConfirmation.getEvidence().length > 0 && messageConfirmation.getEvidenceType() != null;
    }

    private DataHandler buildECodexConnectorPayload(byte[] data, String name, String mimeType,
            ECodexPayloadType payloadType) throws JAXBException, IOException {
        ECodexConnectorPayload payload = new ECodexConnectorPayload();

        payload.setName(name);
        payload.setMimeType(mimeType);
        payload.setPayloadType(payloadType);

        payload.setPayloadContent(buildByteArrayDataHandler(data, mimeType));

        JAXBElement<ECodexConnectorPayload> payloadElement = connectorPayloadObjectFactory
                .createEcodexConnectorPayload(payload);

        JAXBContext ctx = JAXBContext.newInstance(ECodexConnectorPayload.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Marshaller marshaller = ctx.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(payloadElement, byteArrayOutputStream);

        byte[] buffer = byteArrayOutputStream.toByteArray();

        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        DataHandler dh = buildByteArrayDataHandler(buffer, XML_MIME_TYPE);

        return dh;
    }

    private DataHandler buildByteArrayDataHandler(byte[] data, String mimeType) {
        DataSource ds = new ByteArrayDataSource(data, mimeType);
        DataHandler dh = new DataHandler(ds);

        return dh;
    }

    private PartInfo buildPartInfo(String name) {
        PartInfo pi = new PartInfo();

        LOGGER.debug("PartInfo Description is [{}]", name);

        Description desc = new Description();

        desc.setValue(name);

        pi.setDescription(desc);

        return pi;
    }

    public void extractEbmsMessageIdAndPersistIntoDB(SendResponse response, Message message) {
        if (response.getMessageID() != null && !response.getMessageID().isEmpty()) {
            String ebmsMessageId = response.getMessageID().get(0);
            if (!ebmsMessageId.isEmpty()) {
                message.getDbMessage().setEbmsMessageId(ebmsMessageId);
                persistenceService.mergeMessageWithDatabase(message);
            }
        }
    }

    private PartyInfo buildPartyInfo(MessageDetails messageDetails) {
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        if (messageDetails.getFromPartner() != null) {
            partyId.setValue(messageDetails.getFromPartner().getName());
            from.setRole(messageDetails.getFromPartner().getRole());
        } else {
            partyId.setValue(connectorProperties.getGatewayName());
            from.setRole(connectorProperties.getGatewayRole());
        }
        from.getPartyId().add(partyId);
        partyInfo.setFrom(from);

        To to = new To();
        PartyId partyId2 = new PartyId();
        partyId2.setValue(messageDetails.getToPartner().getName());
        to.getPartyId().add(partyId2);
        to.setRole(messageDetails.getToPartner().getRole());
        partyInfo.setTo(to);

        return partyInfo;
    }

    private CollaborationInfo buildCollaborationInfo(MessageDetails messageDetails) {
        CollaborationInfo info = new CollaborationInfo();

        info.setAction(messageDetails.getAction().toString());
        Service service = new Service();
        service.setValue(messageDetails.getService().toString());
        info.setService(service);

        info.setConversationId(messageDetails.getConversationId());

        // AgreementRef ref = new AgreementRef();
        // ref.setValue("dummy");
        // info.setAgreementRef(ref);

        return info;
    }

}
