package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.domain.transition.DomibusConnectorConfirmationType;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

import javax.activation.DataHandler;
import javax.jms.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPluginConstants.*;

@Component
public class ReveiveFromGwJmsPlugin implements MessageListener {

    private static final Logger LOGGER = LogManager.getLogger(ReveiveFromGwJmsPlugin.class);

    @Autowired
    GwJmsPluginConfigurationProperties configurationProperties;

    @Autowired
    TransportStatusService transportStatusService;

    @Autowired
    SubmitToConnector submitToConnector;

    @Override
    public void onMessage(Message message) {
        LOGGER.debug("JMS Message from Gateway received: [{}]", message);

        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_JMS_ID, message.getJMSMessageID())) {


            String messageType = message.getStringProperty(MESSAGE_TYPE_PROPERTY_NAME);
            if (MESSAGE_TYPE_VALUE_INCOMING_MESSAGE.equals(messageType)) {
                LOGGER.debug("Incoming message");
                if (message instanceof MapMessage) {
                    handleIncomingMessage((MapMessage) message);
                } else {
                    throw new RuntimeException("Incoming message is not a map message!");
                }

            } else if (MESSAGE_TYPE_VALUE_SUBMIT_RESPONSE.equals(messageType)) {
                LOGGER.debug("Incoming submit response");
                handleSubmitResponse(message);
            } else if (MESSAGE_TYPE_VALUE_MESSAGE_SENT.equals(messageType)) {
                LOGGER.debug("Incoming message sent notification");
                handleMessageSentNotification(message);
            } else if (MESSAGE_TYPE_VALUE_MESSAGE_SENT_FAILURE.equals(messageType)) {
                LOGGER.debug("Message is a sent failure message");
                handleMessageSentFailedNotification(message);
            } else if (MESSAGE_TYPE_VLAUE_MESSAGE_RECEIVE_FAILURE.equals(messageType)) {
                LOGGER.debug("Message is a receive on gateway failed message");
                handleMessageReceiveFailureNotification(message);
            }


        } catch (JMSException e) {
            LOGGER.error("Processing of JMS message failed", e);
        }
    }

    private void handleMessageReceiveFailureNotification(Message message) {
        LOGGER.warn("Receiving of a message failed on gateway! Look for detail on the gateway!");
    }

    private void handleMessageSentFailedNotification(Message message) throws JMSException {
        String jmsCorrelationID = message.getJMSCorrelationID();
        LOGGER.warn("message [{}] transported failed to remote Access Point. Look for details on gateway!", jmsCorrelationID);
    }

    private void handleMessageSentNotification(Message message) throws JMSException {
        String jmsCorrelationID = message.getJMSCorrelationID();
        LOGGER.info("message [{}] successfully transported to remote Access Point", jmsCorrelationID);
        //TODO: set here extended Transport State!
    }



    private void handleSubmitResponse(Message message) throws JMSException {
        String jmsCorrelationID = message.getJMSCorrelationID();
        String messageId = message.getStringProperty(EBMS_MESSAGE_ID_PROPERTY_NAME);
        String errorDetail = message.getStringProperty(ERROR_DETAIL_PROPERTY_NAME);

        TransportStatusService.DomibusConnectorTransportState transportState = new TransportStatusService.DomibusConnectorTransportState();
        transportState.setConnectorTransportId(jmsCorrelationID);
        transportState.setRemoteTransportId(messageId);

        if (errorDetail != null) {
            DomibusConnectorMessageError messageError = new DomibusConnectorMessageError("Error from Gateway", errorDetail, this.getClass().getName());
            transportState.setMessageErrorList(Stream.of(messageError).collect(Collectors.toList()));
            transportState.setStatus(TransportStatusService.TransportState.FAILED);
        }
        if (messageId != null) {
            transportState.setStatus(TransportStatusService.TransportState.ACCEPTED);
        }

        transportStatusService.updateTransportStatus(transportState);
    }

    private void handleIncomingMessage(MapMessage message) throws JMSException {
//        String message.getStringProperty()
        DomibusConnectorMessageBuilder messageBuilder = DomibusConnectorMessageBuilder
                .createBuilder();

        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        details.setEbmsMessageId(message.getStringProperty(EBMS_MESSAGE_ID_PROPERTY_NAME));
        details.setRefToMessageId(message.getStringProperty(EBMS_REF_TO_MESSAGE_ID_PROPERTY_NAME));
        details.setConversationId(message.getStringProperty(EBMS_CONVERSATION_ID_PROPERTY_NAME));

        String serviceType = message.getStringProperty(EBMS_SERVICE_TYPE_PROPERTY_NAME);
        if (serviceType == null) {
            serviceType = SERVICE_TYPE_DEFAULT_VALUE;
        }
        details.setService(DomibusConnectorServiceBuilder
                .createBuilder()
                .setService(message.getStringProperty(EBMS_SERVICE_PROPERTY_NAME))
                .withServiceType(serviceType)
                .build());

        details.setAction(DomibusConnectorActionBuilder
                .createBuilder()
                .setAction(message.getStringProperty(EBMS_ACTION_PROPERTY_NAME))
                .build()
        );

        details.setFromParty(DomibusConnectorPartyBuilder
                .createBuilder()
                .setPartyId(message.getStringProperty(EBMS_FROM_PARTY_ID_PROPERTY_NAME))
                .withPartyIdType(EBMS_FROM_PARTY_ID_TYPE_PROPERTY_NAME)
                .setRole(EBMS_FROM_PARTY_ROLE_PROPERTY_NAME)
                .build()
        );

        details.setToParty(DomibusConnectorPartyBuilder
            .createBuilder()
                .setPartyId(message.getStringProperty(EBMS_TO_PARTY_ID_PROPERTY_NAME))
                .withPartyIdType(message.getStringProperty(EBMS_TO_PARTY_ID_TYPE_PROPERTY_NAME))
                .setRole(message.getStringProperty(EBMS_TO_PARTY_ROLE_PROPERTY_NAME))
                .build()
        );

        messageBuilder.setMessageDetails(details);
//                .setMessageContent(content)
//                .build();

        DomibusConnectorMessageContentBuilder contentBuilder = DomibusConnectorMessageContentBuilder.createBuilder();

        int payloadNumber = message.getIntProperty(TOTAL_NUMBER_OF_PAYLOADS);
        for (int i = 0; i <payloadNumber; i++) {
            handleRcvPayload(message, messageBuilder, contentBuilder, i);
        }


        DomibusConnectorMessage build = messageBuilder.build();

        try {
            submitToConnector.submitToConnector(build);
        } catch (DomibusConnectorSubmitToLinkException exception) {
            LOGGER.error("The message could not be processed by the connector!");
        }


    }


    private void handleRcvPayload(MapMessage message, DomibusConnectorMessageBuilder messageBuilder, DomibusConnectorMessageContentBuilder contentBuilder, int counter) throws JMSException {
        final String payContID = String.valueOf(MessageFormat.format(PAYLOAD_MIME_CONTENT_ID_FORMAT, counter));

        final String payMimeTypeProp = String.valueOf(MessageFormat.format(PAYLOAD_MIME_TYPE_FORMAT, counter));
        final String payFileNameProp = String.valueOf(MessageFormat.format(PAYLOAD_FILE_NAME_FORMAT, counter));
        final String payDescription = String.valueOf(MessageFormat.format(PAYLOAD_DESCRIPTION_ID_FORMAT, counter));

        LOGGER.debug("Handling payload [{}]\n" +
                "\tpayload_{}_description={}\n" +
                "\tpayload_{}_MimeContentID={}\n" +
                "\tpayload_{}_MimeType={}\n" +
                "\tpayload_{}_FileName={}", counter, counter, payDescription, counter, payContID, counter, payMimeTypeProp, counter, payFileNameProp);


        if (ASIC_S_DESCRIPTION_NAME.equals(payDescription)) {
            LOGGER.debug("Adding payload [{}] as ASIC-S", counter);
            DomibusConnectorMessageDocumentBuilder docBuilder = DomibusConnectorMessageDocumentBuilder.createBuilder();
            docBuilder
                    .setName(payDescription)
                    .setContent(createDataReferencce(message, counter));
        }

        if (MESSAGE_CONTENT_DESCRIPTION_NAME.equals(payDescription)) {
            LOGGER.debug("Adding payload [{}] as business XML", counter);
            contentBuilder.setXmlContent(getBytes(message, counter));
        }


        if (MimeTypeUtils.TEXT_XML_VALUE.equals(payMimeTypeProp) && EVIDENCE_TYPE_NAMES.contains(payDescription)) {
            LOGGER.debug("Adding payload [{}] as evidence", counter);
            DomibusConnectorMessageConfirmationBuilder confirmationBuilder = DomibusConnectorMessageConfirmationBuilder.createBuilder();
            confirmationBuilder
                    .setEvidence(getBytes(message, counter))
                    .setEvidenceType(DomibusConnectorEvidenceType.valueOf(payDescription));
            messageBuilder.addConfirmation(confirmationBuilder.build());
        }

        if (MimeTypeUtils.TEXT_XML_VALUE.equals(payMimeTypeProp) && TOKEN_XML_DESCRIPTION_NAME.equals(payDescription)) {
            LOGGER.debug("Adding payload [{}] as attachment [{}]", counter, payDescription);
            DomibusConnectorMessageAttachment attachment = DomibusConnectorMessageAttachmentBuilder.createBuilder()
                    .withMimeType(payMimeTypeProp)
                    .withName(payDescription)
                    .withDescription(payDescription)
                    .setIdentifier(payDescription)
                    .setAttachment(createDataReferencce(message, counter))
                    .build();
            messageBuilder.addAttachment(attachment);
        }

    }

    private byte[] getBytes(MapMessage msg, int counter) throws JMSException {
        if (configurationProperties.isPutAttachmentInQueue()) {
            final String payFileNameProp = String.valueOf(MessageFormat.format(PAYLOAD_FILE_NAME_FORMAT, counter));
            Path resolve = configurationProperties.getAttachmentStorageLocation().resolve(payFileNameProp);
            try (FileInputStream fis = new FileInputStream(resolve.toFile())){
                return StreamUtils.copyToByteArray(fis);
            } catch (IOException ioe) {
                throw new RuntimeException("Error while reading payload file!", ioe);
            }
        } else {
            final String propPayload = String.valueOf(MessageFormat.format(PAYLOAD_NAME_FORMAT, counter));
            return msg.getBytes(propPayload);
        }
    }

    private DomibusConnectorBigDataReference createDataReferencce(MapMessage msg, int counter) throws JMSException {
        final String payMimeTypeProp = String.valueOf(MessageFormat.format(PAYLOAD_MIME_TYPE_FORMAT, counter));
        final String payFileNameProp = String.valueOf(MessageFormat.format(PAYLOAD_FILE_NAME_FORMAT, counter));
        final String payDescription = String.valueOf(MessageFormat.format(PAYLOAD_DESCRIPTION_ID_FORMAT, counter));

        String mimeType = msg.getStringProperty(payMimeTypeProp);
        String description = msg.getStringProperty(payDescription);

        DomibusConnectorBigDataReference domibusConnectorBigDataReference;
        if (configurationProperties.isPutAttachmentInQueue()) {
            final String propPayload = String.valueOf(MessageFormat.format(PAYLOAD_NAME_FORMAT, counter));
            byte[] bytes = msg.getBytes(propPayload);
            domibusConnectorBigDataReference =  new GatewayByteBackedDomibusConnectorBigDataReference(bytes, description, mimeType);
        } else {
            Path resolve = configurationProperties.getAttachmentStorageLocation().resolve(msg.getStringProperty(payFileNameProp));
            domibusConnectorBigDataReference =
                    new GatewayBackedDomibusConnectorBigDataReference(resolve, description, mimeType);

        }
        return domibusConnectorBigDataReference;
    }

    private static class GatewayBackedDomibusConnectorBigDataReference extends DomibusConnectorBigDataReference {

        public GatewayBackedDomibusConnectorBigDataReference(Path fileLocation, String name, String mimeType) {
            this.setStorageIdReference(fileLocation.toAbsolutePath().toString());
            this.setMimetype(mimeType);
            this.setName(name);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(this.getStorageIdReference());
        }
    }

    private static class GatewayByteBackedDomibusConnectorBigDataReference extends DomibusConnectorBigDataReference {

        private final byte[] bytes;

        public GatewayByteBackedDomibusConnectorBigDataReference(byte[] bytes, String name, String mimeType) {
            this.bytes = bytes;
            this.setStorageIdReference(name);
            this.setMimetype(mimeType);
            this.setName(name);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }
    }


}
