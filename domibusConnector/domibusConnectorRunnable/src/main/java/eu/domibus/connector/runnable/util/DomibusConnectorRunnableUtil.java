package eu.domibus.connector.runnable.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageDetails;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;
import eu.domibus.connector.runnable.exception.DomibusConnectorRunnableException;

public class DomibusConnectorRunnableUtil {

    static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DomibusConnectorRunnableUtil.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static final MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();

    @Value("${gateway.name}")
    private String gatewayName;

    @Value("${gateway.role}")
    private String gatewayRole;

    private DomibusConnectorPersistenceService persistenceService;

    public void setPersistenceService(DomibusConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public static byte[] fileToByteArray(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();

        return data;
    }

    public static void byteArrayToFile(byte[] data, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
    }

    public void convertMessagePropertiesToMessageDetails(DomibusConnectorMessageProperties properties,
            MessageDetails messageDetails) throws DomibusConnectorRunnableException {

        messageDetails.setFinalRecipient(properties.getFinalRecipient());
        messageDetails.setOriginalSender(properties.getOriginalSender());
        String fromPartyId = properties.getFromPartyId();
        String fromPartyRole = properties.getFromPartyRole();
        if (!StringUtils.hasText(fromPartyId)) {
            fromPartyId = gatewayName;
        }
        if (!StringUtils.hasText(fromPartyRole)) {
            fromPartyRole = gatewayRole;
        }
        if (fromPartyId != null && fromPartyRole != null) {
            DomibusConnectorParty fromParty = persistenceService.getParty(fromPartyId, fromPartyRole);
            if (fromParty == null) {
                throw new DomibusConnectorRunnableException("Could not find Party with id '" + fromPartyId
                        + "' and role '" + fromPartyRole + "'!");
            }
            messageDetails.setFromParty(fromParty);
        } else {
            throw new DomibusConnectorRunnableException(
                    "Cannot process message without definition of fromPartyId and fromPartyRole!");
        }
        String toPartyId = properties.getToPartyId();
        String toPartyRole = properties.getToPartyRole();
        if (toPartyId != null && toPartyRole != null) {
            DomibusConnectorParty toParty = persistenceService.getParty(toPartyId, toPartyRole);
            if (toParty == null) {
                throw new DomibusConnectorRunnableException("Could not find Party with id '" + toPartyId
                        + "' and role '" + toPartyRole + "'!");
            }
            messageDetails.setToParty(toParty);
        } else {
            throw new DomibusConnectorRunnableException(
                    "Cannot process message without definition of toPartyId and toPartyRole!");
        }

        String action = properties.getAction();
        if (StringUtils.hasText(action)) {
            DomibusConnectorAction dbAction = persistenceService.getAction(action);
            if (dbAction == null) {
                throw new DomibusConnectorRunnableException("Could not find Action in database by set action '" + action
                        + "'!");
            }
            messageDetails.setAction(dbAction);
        } else {
            throw new DomibusConnectorRunnableException("Cannot process message without definition of action!");
        }

        String service = properties.getService();
        if (StringUtils.hasText(service)) {
            DomibusConnectorService dbService = persistenceService.getService(service);
            if (dbService == null) {
                throw new DomibusConnectorRunnableException("Could not find Service in database by set service '"
                        + service + "'!");
            }
            messageDetails.setService(dbService);
        } else {
            throw new DomibusConnectorRunnableException("Cannot process message without definition of service!");
        }

        String conversationId = properties.getConversationId();
        if(StringUtils.hasText(conversationId)){
        	messageDetails.setConversationId(conversationId);
        }
    }

    public static DomibusConnectorMessageProperties convertMessageDetailsToMessageProperties(
            MessageDetails messageDetails, Date messageReceived) {

        DomibusConnectorMessageProperties messageProperties = new DomibusConnectorMessageProperties();
        if (StringUtils.hasText(messageDetails.getEbmsMessageId())) {
            messageProperties.setEbmsMessageId(messageDetails.getEbmsMessageId());
        }
        if (StringUtils.hasText(messageDetails.getNationalMessageId())) {
            messageProperties.setNationalMessageId(messageDetails.getNationalMessageId());
        }
        if (StringUtils.hasText(messageDetails.getConversationId())) {
            messageProperties.setConversationId(messageDetails.getConversationId());
        }
        messageProperties.setToPartyId(messageDetails.getToParty().getPartyId());
        messageProperties.setToPartyRole(messageDetails.getToParty().getRole());
        messageProperties.setFromPartyId(messageDetails.getFromParty().getPartyId());
        messageProperties.setFromPartyRole(messageDetails.getFromParty().getRole());
        messageProperties.setFinalRecipient(messageDetails.getFinalRecipient());
        messageProperties.setOriginalSender(messageDetails.getOriginalSender());
        messageProperties.setAction(messageDetails.getAction().getAction());
        messageProperties.setService(messageDetails.getService().getService());
        messageProperties.setMessageReceivedDatetime(convertDateToProperty(messageReceived));

        return messageProperties;
    }
    
    public static String convertDateToProperty(Date date){
    	return sdf2.format(date);
    }

    public static void storeMessagePropertiesToFile(DomibusConnectorMessageProperties messageProperties,
            File messagePropertiesFile) {
        if (!messagePropertiesFile.exists()) {
            try {
                messagePropertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        messageProperties.storePropertiesToFile(messagePropertiesFile);
    }

    public static void createFile(File messageFolder, String fileName, byte[] content)
            throws DomibusConnectorNationalBackendClientException {
        String filePath = messageFolder.getAbsolutePath() + File.separator + fileName;
        LOGGER.debug("Create file {}", filePath);
        File file = new File(filePath);
        try {
            DomibusConnectorRunnableUtil.byteArrayToFile(content, file);
        } catch (IOException e) {
            throw new DomibusConnectorNationalBackendClientException("Could not create file " + file.getAbsolutePath(),
                    e);
        }
    }

    public static DomibusConnectorMessageProperties loadMessageProperties(File message, String messagePropertiesFileName) {
        String pathname = message.getAbsolutePath() + File.separator + messagePropertiesFileName;
        LOGGER.debug("Loading message properties from file {}", pathname);
        File messagePropertiesFile = new File(pathname);
        if (!messagePropertiesFile.exists()) {
            LOGGER.error("Message properties file '" + messagePropertiesFile.getAbsolutePath()
                    + "' does not exist. Message cannot be processed!");
            return null;
        }
        DomibusConnectorMessageProperties details = new DomibusConnectorMessageProperties();
        details.loadPropertiesFromFile(messagePropertiesFile);

        return details;
    }

    public static Message createConfirmationMessage(EvidenceType evidenceType, Message originalMessage) {
        MessageDetails details = new MessageDetails();
        details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
        details.setService(originalMessage.getMessageDetails().getService());
        details.setFromParty(originalMessage.getMessageDetails().getToParty());
        details.setToParty(originalMessage.getMessageDetails().getFromParty());

        MessageConfirmation confirmation = new MessageConfirmation();
        confirmation.setEvidenceType(evidenceType);

        Message confirmationMessage = new Message(details, confirmation);

        return confirmationMessage;
    }

    public static String generateNationalMessageId(String postfix, Date messageReceived) {
        String natMessageId = sdf.format(messageReceived) + "_" + postfix;
        return natMessageId;
    }

    public static String getMimeTypeFromFileName(File file) {
        return mimeMap.getContentType(file.getName());
    }

    public Message findOriginalMessage(String refToMessageId) {
        Message originalMessage = null;
        try {
            originalMessage = persistenceService.findMessageByEbmsId(refToMessageId);
        } catch (NoResultException e) {
            originalMessage = persistenceService.findMessageByNationalId(refToMessageId);
        }

        return originalMessage;

    }

    public void mergeMessage(Message message) {
        persistenceService.mergeMessageWithDatabase(message);
    }

    public List<Message> findUnconfirmedMessages() {
        return persistenceService.findIncomingUnconfirmedMessages();
    }

}
