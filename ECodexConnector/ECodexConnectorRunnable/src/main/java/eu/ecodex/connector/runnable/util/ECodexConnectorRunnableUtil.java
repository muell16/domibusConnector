package eu.ecodex.connector.runnable.util;

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

import eu.ecodex.connector.common.db.model.ECodexAction;
import eu.ecodex.connector.common.db.model.ECodexParty;
import eu.ecodex.connector.common.db.model.ECodexService;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;
import eu.ecodex.connector.runnable.exception.ECodexConnectorRunnableException;

public class ECodexConnectorRunnableUtil {

    static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorRunnableUtil.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");

    private static final MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();

    @Value("${gateway.name}")
    private String gatewayName;

    @Value("${gateway.role}")
    private String gatewayRole;

    private ECodexConnectorPersistenceService persistenceService;

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
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

    public void convertMessagePropertiesToMessageDetails(ECodexConnectorMessageProperties properties,
            MessageDetails messageDetails) throws ECodexConnectorRunnableException {

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
            ECodexParty fromParty = persistenceService.getParty(fromPartyId, fromPartyRole);
            if (fromParty == null) {
                throw new ECodexConnectorRunnableException("Could not find Party with id '" + fromPartyId
                        + "' and role '" + fromPartyRole + "'!");
            }
            messageDetails.setFromParty(fromParty);
        } else {
            throw new ECodexConnectorRunnableException(
                    "Cannot process message without definition of fromPartyId and fromPartyRole!");
        }
        String toPartyId = properties.getToPartyId();
        String toPartyRole = properties.getToPartyRole();
        if (toPartyId != null && toPartyRole != null) {
            ECodexParty toParty = persistenceService.getParty(toPartyId, toPartyRole);
            if (toParty == null) {
                throw new ECodexConnectorRunnableException("Could not find Party with id '" + toPartyId
                        + "' and role '" + toPartyRole + "'!");
            }
            messageDetails.setToParty(toParty);
        } else {
            throw new ECodexConnectorRunnableException(
                    "Cannot process message without definition of toPartyId and toPartyRole!");
        }

        String action = properties.getAction();
        if (StringUtils.hasText(action)) {
            ECodexAction dbAction = persistenceService.getAction(action);
            if (dbAction == null) {
                throw new ECodexConnectorRunnableException("Could not find Action in database by set action '" + action
                        + "'!");
            }
            messageDetails.setAction(dbAction);
        } else {
            throw new ECodexConnectorRunnableException("Cannot process message without definition of action!");
        }

        String service = properties.getService();
        if (StringUtils.hasText(service)) {
            ECodexService dbService = persistenceService.getService(service);
            if (dbService == null) {
                throw new ECodexConnectorRunnableException("Could not find Service in database by set service '"
                        + service + "'!");
            }
            messageDetails.setService(dbService);
        } else {
            throw new ECodexConnectorRunnableException("Cannot process message without definition of service!");
        }

    }

    public static void convertMessageDetailsToMessagePropertiesAndStore(File messagePropertiesFile,
            MessageDetails messageDetails) {
        if (!messagePropertiesFile.exists()) {
            try {
                messagePropertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ECodexConnectorMessageProperties messageProperties = new ECodexConnectorMessageProperties();
        if (StringUtils.hasText(messageDetails.getEbmsMessageId())) {
            messageProperties.setEbmsMessageId(messageDetails.getEbmsMessageId());
        }
        if (StringUtils.hasText(messageDetails.getNationalMessageId())) {
            messageProperties.setNationalMessageId(messageDetails.getNationalMessageId());
        }
        messageProperties.setToPartyId(messageDetails.getToParty().getPartyId());
        messageProperties.setToPartyRole(messageDetails.getToParty().getRole());
        messageProperties.setFromPartyId(messageDetails.getFromParty().getPartyId());
        messageProperties.setFromPartyRole(messageDetails.getFromParty().getRole());
        messageProperties.setFinalRecipient(messageDetails.getFinalRecipient());
        messageProperties.setOriginalSender(messageDetails.getOriginalSender());
        messageProperties.setAction(messageDetails.getAction().getAction());
        messageProperties.setService(messageDetails.getService().getService());

        messageProperties.storePropertiesToFile(messagePropertiesFile);

    }

    public static void createFile(File messageFolder, String fileName, byte[] content)
            throws ECodexConnectorNationalBackendClientException {
        String filePath = messageFolder.getAbsolutePath() + File.separator + fileName;
        LOGGER.debug("Create file {}", filePath);
        File file = new File(filePath);
        try {
            ECodexConnectorRunnableUtil.byteArrayToFile(content, file);
        } catch (IOException e) {
            throw new ECodexConnectorNationalBackendClientException("Could not create file " + file.getAbsolutePath(),
                    e);
        }
    }

    public static ECodexConnectorMessageProperties loadMessageProperties(File message, String messagePropertiesFileName) {
        String pathname = message.getAbsolutePath() + File.separator + messagePropertiesFileName;
        LOGGER.debug("Loading message properties from file {}", pathname);
        File messagePropertiesFile = new File(pathname);
        if (!messagePropertiesFile.exists()) {
            LOGGER.error("Message properties file '" + messagePropertiesFile.getAbsolutePath()
                    + "' does not exist. Message cannot be processed!");
        }
        ECodexConnectorMessageProperties details = new ECodexConnectorMessageProperties();
        details.loadPropertiesFromFile(messagePropertiesFile);

        return details;
    }

    public static Message createConfirmationMessage(ECodexEvidenceType evidenceType, Message originalMessage) {
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

    public static String generateNationalMessageId(String postfix) {
        String natMessageId = sdf.format(new Date()) + "_" + postfix;
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
