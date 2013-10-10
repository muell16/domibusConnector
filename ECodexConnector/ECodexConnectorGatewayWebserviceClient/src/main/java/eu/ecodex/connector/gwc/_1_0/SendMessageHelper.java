package eu.ecodex.connector.gwc._1_0;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

import backend.ecodex.org._1_0.SendRequest;
import backend.ecodex.org._1_0.SendResponse;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ECodexConnectorPayload;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ECodexPayloadType;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ObjectFactory;
<<<<<<< HEAD:ECodexConnector/ECodexConnectorGatewayWebserviceClient/src/main/java/eu/ecodex/connector/gwc/helper/SendMessageHelper.java
import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.model.ECodexParty;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
=======
>>>>>>> d404a13... complete change of the payload structure.:ECodexConnector/ECodexConnectorGatewayWebserviceClient/src/main/java/eu/ecodex/connector/gwc/_1_0/SendMessageHelper.java
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.gwc.util.CommonMessageHelper;

public class SendMessageHelper {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SendMessageHelper.class);

    private static final ObjectFactory connectorPayloadObjectFactory = new ObjectFactory();

    private CommonMessageHelper commonMessageHelper;

    public void setCommonMessageHelper(CommonMessageHelper commonMessageHelper) {
        this.commonMessageHelper = commonMessageHelper;
    }

    public void buildMessage(SendRequest request, Messaging ebMSHeaderInfo, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = commonMessageHelper.buildUserMessage(message);

        buildSendRequestAndPayloadInfo(userMessage, request, message);

<<<<<<< HEAD:ECodexConnector/ECodexConnectorGatewayWebserviceClient/src/main/java/eu/ecodex/connector/gwc/helper/SendMessageHelper.java
        userMessage.setMessageProperties(buildMessageProperties(message));

        userMessage.setPartyInfo(buildPartyInfo(message));

        userMessage.setCollaborationInfo(buildCollaborationInfo(message.getMessageDetails()));

        MessageInfo info = new MessageInfo();

        info.setRefToMessageId(message.getMessageDetails().getRefToMessageId());

        userMessage.setMessageInfo(info);

=======
>>>>>>> d404a13... complete change of the payload structure.:ECodexConnector/ECodexConnectorGatewayWebserviceClient/src/main/java/eu/ecodex/connector/gwc/_1_0/SendMessageHelper.java
        ebMSHeaderInfo.getUserMessage().add(userMessage);
    }

    private void buildSendRequestAndPayloadInfo(UserMessage userMessage, SendRequest request, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {

        int payloadCounter = 1;

        MessageContent messageContent = message.getMessageContent();
        if (messageContent != null && messageContent.getECodexContent() != null
                && messageContent.getECodexContent().length > 0) {
            DataHandler payload;
            try {
                payload = buildECodexConnectorPayload(messageContent.getECodexContent(),
                        CommonMessageHelper.CONTENT_XML_NAME, CommonMessageHelper.XML_MIME_TYPE,
                        ECodexPayloadType.CONTENT_XML);
            } catch (Exception e) {
                throw new ECodexConnectorGatewayWebserviceClientException("Could not build Payload for content XML!", e);
            }
            request.getPayload().add(payload);
            commonMessageHelper.addPartInfoToPayloadInfo(CommonMessageHelper.CONTENT_XML_NAME, userMessage, "payload_"
                    + payloadCounter);
            payloadCounter++;
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
                    commonMessageHelper.addPartInfoToPayloadInfo(attachment.getName(), userMessage, "payload_"
                            + payloadCounter);
                    payloadCounter++;

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
                payload = buildECodexConnectorPayload(messageContent.getECodexContent(),
                        CommonMessageHelper.CONTENT_PDF_NAME, CommonMessageHelper.APPLICATION_MIME_TYPE,
                        ECodexPayloadType.CONTENT_PDF);
            } catch (Exception e) {
                throw new ECodexConnectorGatewayWebserviceClientException("Could not build Payload for content XML!", e);
            }
            request.getPayload().add(payload);
            commonMessageHelper.addPartInfoToPayloadInfo(CommonMessageHelper.CONTENT_PDF_NAME, userMessage, "payload_"
                    + payloadCounter);
            payloadCounter++;
        }

        if (message.getConfirmations() != null) {
            // Pick only the last produced evidence
            MessageConfirmation messageConfirmation = message.getConfirmations().get(
                    message.getConfirmations().size() - 1);

            if (checkMessageConfirmationValid(messageConfirmation)) {
                DataHandler payload;
                try {
                    payload = buildECodexConnectorPayload(messageConfirmation.getEvidence(), messageConfirmation
                            .getEvidenceType().toString(), CommonMessageHelper.XML_MIME_TYPE,
                            ECodexPayloadType.EVIDENCE);
                } catch (Exception e) {
                    throw new ECodexConnectorGatewayWebserviceClientException("Could not build Payload for evidence "
                            + messageConfirmation.getEvidenceType().toString(), e);
                }

                request.getPayload().add(payload);
                commonMessageHelper.addPartInfoToPayloadInfo(messageConfirmation.getEvidenceType().toString(),
                        userMessage, "payload_" + payloadCounter);
                payloadCounter++;
            }
        }

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

        DataHandler dh = buildByteArrayDataHandler(buffer, CommonMessageHelper.XML_MIME_TYPE);

        return dh;
    }

    private DataHandler buildByteArrayDataHandler(byte[] data, String mimeType) {
        DataSource ds = new ByteArrayDataSource(data, mimeType);
        DataHandler dh = new DataHandler(ds);

        return dh;
    }

    public void extractEbmsMessageIdAndPersistIntoDB(SendResponse response, Message message) {
        if (response.getMessageID() != null && !response.getMessageID().isEmpty()) {
            String ebmsMessageId = response.getMessageID().get(0);
            commonMessageHelper.persistEbmsMessageIdIntoDatabase(ebmsMessageId, message);
        }
    }

<<<<<<< HEAD:ECodexConnector/ECodexConnectorGatewayWebserviceClient/src/main/java/eu/ecodex/connector/gwc/helper/SendMessageHelper.java
    private PartyInfo buildPartyInfo(Message message) {
        MessageDetails messageDetails = message.getMessageDetails();

        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        if (messageDetails.getFromParty() != null) {
            partyId.setValue(messageDetails.getFromParty().getPartyId());
            from.setRole(messageDetails.getFromParty().getRole());
        } else {
            partyId.setValue(connectorProperties.getGatewayName());
            from.setRole(connectorProperties.getGatewayRole());
            ECodexParty fromParty = persistenceService.getParty(connectorProperties.getGatewayName(),
                    connectorProperties.getGatewayRole());
            if (fromParty != null) {
                messageDetails.setFromParty(fromParty);
                persistenceService.mergeMessageWithDatabase(message);
            }
        }
        from.getPartyId().add(partyId);
        partyInfo.setFrom(from);

        To to = new To();
        PartyId partyId2 = new PartyId();
        partyId2.setValue(messageDetails.getToParty().getPartyId());
        to.getPartyId().add(partyId2);
        to.setRole(messageDetails.getToParty().getRole());
        partyInfo.setTo(to);

        return partyInfo;
    }

    private CollaborationInfo buildCollaborationInfo(MessageDetails messageDetails) {
        CollaborationInfo info = new CollaborationInfo();

        info.setAction(messageDetails.getAction().getAction());
        Service service = new Service();
        service.setValue(messageDetails.getService().getService());
        info.setService(service);

        info.setConversationId(messageDetails.getConversationId());

        // AgreementRef ref = new AgreementRef();
        // ref.setValue("dummy");
        // info.setAgreementRef(ref);

        return info;
    }

    public boolean isMessageEvidence(Message message) {
        return message.getMessageDetails().getAction().getAction().equals("RelayREMMDAcceptanceRejection")
                || message.getMessageDetails().getAction().getAction().equals("DeliveryNonDeliveryToRecipient")
                || message.getMessageDetails().getAction().getAction().equals("RetrievalNonRetrievalToRecipient");
    }

=======
>>>>>>> d404a13... complete change of the payload structure.:ECodexConnector/ECodexConnectorGatewayWebserviceClient/src/main/java/eu/ecodex/connector/gwc/_1_0/SendMessageHelper.java
}
