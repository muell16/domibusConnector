package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.UUID;

import static eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPluginConstants.*;

@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
@Service
public class SubmitToGwJmsPlugin implements SubmitToLink {

    private static final Logger LOGGER = LogManager.getLogger(SubmitToGwJmsPlugin.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private GwJmsPluginConfigurationProperties configurationProperties;

    @Autowired
    private TransportStatusService transportStatusService;


    @Override
    public void submitToLink(DomibusConnectorMessage message) throws DomibusConnectorSubmitToLinkException {
        final DomibusConnectorMessageDetails msgDetails = message.getMessageDetails();

        try {
            jmsTemplate.send(configurationProperties.getToDomibusGateway(), session -> {
                MapMessage jmsMsg = session.createMapMessage();

                jmsMsg.setStringProperty(MESSAGE_TYPE_PROPERTY_NAME, MESSAGE_TYPE_VALUE_SUBMIT_MESSAGE);
                jmsMsg.setJMSCorrelationID(message.getConnectorMessageId());

                jmsMsg.setStringProperty(PROTOCOL_PROPERTY_NAME, PROTOCOL_VALUE_DEFAULT);

                jmsMsg.setStringProperty(EBMS_SERVICE_PROPERTY_NAME, msgDetails.getService().getService());
                jmsMsg.setStringProperty(EBMS_SERVICE_TYPE_PROPERTY_NAME, message.getMessageDetails().getService().getServiceType());
                jmsMsg.setStringProperty(EBMS_ACTION_PROPERTY_NAME, message.getMessageDetails().getAction().getAction());
                //message ids
                jmsMsg.setStringProperty(EBMS_MESSAGE_ID_PROPERTY_NAME, msgDetails.getEbmsMessageId());
                jmsMsg.setStringProperty(EBMS_CONVERSATION_ID_PROPERTY_NAME, msgDetails.getConversationId());
                jmsMsg.setStringProperty(EBMS_REF_TO_MESSAGE_ID_PROPERTY_NAME, msgDetails.getRefToMessageId());
                //from party
                jmsMsg.setStringProperty(EBMS_FROM_PARTY_ID_PROPERTY_NAME, msgDetails.getFromParty().getPartyId());
                jmsMsg.setStringProperty(EBMS_FROM_PARTY_ID_TYPE_PROPERTY_NAME, msgDetails.getFromParty().getPartyIdType());
                jmsMsg.setStringProperty(EBMS_FROM_PARTY_ROLE_PROPERTY_NAME, msgDetails.getFromParty().getRole());
                //to party
                jmsMsg.setStringProperty(EBMS_TO_PARTY_ID_PROPERTY_NAME, msgDetails.getToParty().getPartyId());
                jmsMsg.setStringProperty(EBMS_TO_PARTY_ID_TYPE_PROPERTY_NAME, msgDetails.getToParty().getPartyIdType());
                jmsMsg.setStringProperty(EBMS_TO_PARTY_ROLE_PROPERTY_NAME, msgDetails.getToParty().getRole());
                //final recipient original sender
                jmsMsg.setStringProperty(EBMS_ORIGINAL_SENDER_PROPERTY_NAME, msgDetails.getOriginalSender());
                jmsMsg.setStringProperty(EBMS_FINAL_RECIPIENT_PROPERTY_NAME, msgDetails.getFinalRecipient());

                int payloadCounter = 1;

                if (message.getMessageContent() != null && message.getMessageContent().getXmlContent() != null) {
                    message.getMessageContent().getXmlContent();
                    addPayload(message, jmsMsg, payloadCounter, message.getMessageContent().getXmlContent(), MESSAGE_CONTENT_DESCRIPTION_NAME, MimeTypeUtils.TEXT_XML_VALUE);
                    payloadCounter++;
                }
                if (message.getMessageContent() != null && message.getMessageContent().getDocument() != null) {
                    DomibusConnectorMessageDocument document = message.getMessageContent().getDocument();
                    addPayload(message, jmsMsg, payloadCounter, document.getDocument(), ASIC_S_DESCRIPTION_NAME, ASIC_S_MIMETYPE);
                    payloadCounter++;
                }
                Iterator<DomibusConnectorMessageAttachment> iterator = message.getMessageAttachments().iterator();
                while (iterator.hasNext()) {
                    DomibusConnectorMessageAttachment attachment = iterator.next();
                    addPayload(message, jmsMsg, payloadCounter, attachment.getAttachment(), attachment.getIdentifier(), attachment.getMimeType());
                    payloadCounter++;
                }

                Iterator<DomibusConnectorMessageConfirmation> confirmationIterator = message.getMessageConfirmations().iterator();
                while (iterator.hasNext()) {
                    DomibusConnectorMessageConfirmation c = confirmationIterator.next();
                    addPayload(message, jmsMsg, payloadCounter, c.getEvidence(), c.getEvidenceType().toString(), MimeTypeUtils.TEXT_XML_VALUE);
                    payloadCounter++;
                }

                jmsMsg.setInt(TOTAL_NUMBER_OF_PAYLOADS, payloadCounter);

                return jmsMsg;
            });

        } catch (JmsException jmsException) {
            throw new DomibusConnectorSubmitToLinkException(message, DomibusConnectorRejectionReason.OTHER, "JMS exception occured while sending jms message", jmsException);
        }
    }

    private void addPayload(DomibusConnectorMessage msg, MapMessage jmsMessage, int counter, byte[] bytes, String description, String mimeType) throws JMSException {
        try (InputStream is = new ByteArrayInputStream(bytes)){
            addPayload(msg, jmsMessage, counter, is, description, mimeType);
        } catch (IOException ioe) {
            throw new DomibusConnectorSubmitToLinkException(msg, DomibusConnectorRejectionReason.OTHER, "IOException while reading from InputStream", ioe);
        }
    }

    private void addPayload(DomibusConnectorMessage msg, MapMessage jmsMessage, int counter, DomibusConnectorBigDataReference ref, String description, String mimeType) throws JMSException {
        try (InputStream is = ref.getInputStream()){
            addPayload(msg, jmsMessage, counter, is, description, mimeType);
        } catch (IOException ioe) {
            throw new DomibusConnectorSubmitToLinkException(msg, DomibusConnectorRejectionReason.OTHER, "IOException while reading from InputStream", ioe);
        }
    }


    //should maybe replaced by builder pattern to reduce number of arguments or wrapper around jmsMessage
    private void addPayload(DomibusConnectorMessage msg, MapMessage jmsMessage, int counter, InputStream is, String description, String mimeType) throws IOException, JMSException {
        //TODO: set description + mimeType!


        if (configurationProperties.isPutAttachmentInQueue()) {
            final String propPayload = String.valueOf(MessageFormat.format(PAYLOAD_NAME_FORMAT, counter));
            jmsMessage.setBytes(propPayload, StreamUtils.copyToByteArray(is));
        } else {
            String filePropertyName = String.valueOf(MessageFormat.format(PAYLOAD_FILE_NAME_FORMAT, counter));;
            String fileName = "payload_" + UUID.randomUUID().toString();
            Path filePath = configurationProperties.getAttachmentStorageLocation().resolve(fileName);
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                StreamUtils.copy(is, fos);
            } catch (IOException ioe) {
                throw new DomibusConnectorSubmitToLinkException(msg, DomibusConnectorRejectionReason.OTHER, "Error while writing file to attachment location", ioe);
            }
            LOGGER.debug("payload [{}] setting file property to [{}] for file [{}]", counter, fileName, filePath.toAbsolutePath());
            jmsMessage.setStringProperty(filePropertyName, fileName);

        }
    }


}

