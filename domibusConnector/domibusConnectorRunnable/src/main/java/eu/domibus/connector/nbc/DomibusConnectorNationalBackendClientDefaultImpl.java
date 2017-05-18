package eu.domibus.connector.nbc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import eu.domibus.connector.common.enums.DetachedSignatureMimeType;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.enums.MessageDirection;
import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageAttachment;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageContent;
import eu.domibus.connector.common.message.MessageError;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;
import eu.domibus.connector.runnable.exception.DomibusConnectorRunnableException;
import eu.domibus.connector.runnable.util.DomibusConnectorMessageProperties;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableUtil;

public class DomibusConnectorNationalBackendClientDefaultImpl implements DomibusConnectorNationalBackendClient,
InitializingBean {

	org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DomibusConnectorNationalBackendClientDefaultImpl.class);

	private File incomingMessagesDir;

	private File outgoingMessagesDir;

	@Value("${incoming.messages.directory:}")
	private String incomingMessagesDirectory;

	@Value("${outgoing.messages.directory:}")
	private String outgoingMessagesDirectory;

	@Value("${message.properties.file.name:}")
	private String messagePropertiesFileName;

	@Autowired
	private DomibusConnectorRunnableUtil util;

	@Override
	public String[] requestMessagesUnsent() throws DomibusConnectorNationalBackendClientException,
	ImplementationMissingException {

		LOGGER.debug("Start searching dir {} for folder with ending {}", outgoingMessagesDir.getAbsolutePath(),
				DomibusConnectorRunnableConstants.MESSAGE_READY_FOLDER_POSTFIX);
		List<File> messagesUnsent = new ArrayList<File>();

		if (outgoingMessagesDir.listFiles().length > 0) {
			for (File sub : outgoingMessagesDir.listFiles()) {
				if (sub.isDirectory()
						&& sub.getName().endsWith(DomibusConnectorRunnableConstants.MESSAGE_READY_FOLDER_POSTFIX)) {
					messagesUnsent.add(sub);
				}
			}
		}

		List<String> messageIds = new ArrayList<String>();
		if (!messagesUnsent.isEmpty()) {
			LOGGER.info("Found {} new outgoing messages to process!", messagesUnsent.size());
			for (File messageFolder : messagesUnsent) {
				LOGGER.debug("Processing new message folder {}", messageFolder.getAbsolutePath());
				if (messageFolder.listFiles().length > 0) {
					String nationalMessageId = null;
					DomibusConnectorMessageProperties messageProperties = DomibusConnectorRunnableUtil
							.loadMessageProperties(messageFolder, messagePropertiesFileName);
					if (messageProperties != null) {
						nationalMessageId = messageProperties.getNationalMessageId();
						LOGGER.debug("Found nationalMessageId in Properties: {}", nationalMessageId);
					}
					if (!StringUtils.hasText(nationalMessageId)) {
						nationalMessageId = DomibusConnectorRunnableUtil.generateNationalMessageId(messageProperties
								.getOriginalSender(), new Date());
						LOGGER.debug("No national message ID resolved. Generated " + nationalMessageId);
					}

					String newMessageFolderPath = messageFolder.getAbsolutePath().substring(0,
							messageFolder.getAbsolutePath().lastIndexOf(File.separator))
							+ File.separator + nationalMessageId;

					File workMessageFolder = new File(newMessageFolderPath);

					LOGGER.debug("Try to rename message folder {} to {}", messageFolder.getAbsolutePath(),
							newMessageFolderPath);
					try {
						FileUtils.moveDirectory(messageFolder, workMessageFolder);
					} catch (IOException e1) {
						throw new DomibusConnectorNationalBackendClientException("Could not rename message folder "
								+ messageFolder.getAbsolutePath() + " to " + workMessageFolder.getAbsolutePath());
					}

					messageIds.add(nationalMessageId);
				}
			}
		} else {
			LOGGER.debug("No new messages found!");
		}

		return messageIds.toArray(new String[messageIds.size()]);
	}

	@Override
	public void requestMessage(Message message) throws DomibusConnectorNationalBackendClientException,
	ImplementationMissingException {

		String nationalMessageId = message.getMessageDetails().getNationalMessageId();

		LOGGER.info("Start processing outgoing message with national ID {}", nationalMessageId);
		File messageFolder = new File(outgoingMessagesDir + File.separator + nationalMessageId);

		if (messageFolder.exists() && messageFolder.isDirectory() && messageFolder.listFiles().length > 0) {
			String oldMessageFolderName = messageFolder.getAbsolutePath();

			File workMessageFolder = new File(oldMessageFolderName
					+ DomibusConnectorRunnableConstants.MESSAGE_PROCESSING_FOLDER_POSTFIX);
			LOGGER.debug("Try to rename message folder {} to {}", messageFolder.getAbsolutePath(),
					workMessageFolder.getAbsolutePath());
			try {
				FileUtils.moveDirectory(messageFolder, workMessageFolder);
			} catch (IOException e1) {
				throw new DomibusConnectorNationalBackendClientException("Could not rename message folder "
						+ messageFolder.getAbsolutePath() + " to " + workMessageFolder.getAbsolutePath());
			}

			DomibusConnectorMessageProperties messageProperties = DomibusConnectorRunnableUtil.loadMessageProperties(
					workMessageFolder, messagePropertiesFileName);
			try {
				processMessageFolderFiles(message, workMessageFolder, messageProperties);
			} catch (Exception e) {
				File failedMessageFolder = new File(oldMessageFolderName
						+ DomibusConnectorRunnableConstants.MESSAGE_FAILED_FOLDER_POSTFIX);
				LOGGER.debug("Try to rename message folder {} to {}", workMessageFolder.getAbsolutePath(),
						failedMessageFolder.getAbsolutePath());
				try {
					FileUtils.moveDirectory(workMessageFolder, failedMessageFolder);
				} catch (IOException e1) {
					throw new DomibusConnectorNationalBackendClientException("Could not rename message folder "
							+ workMessageFolder.getAbsolutePath() + " to " + failedMessageFolder.getAbsolutePath());
				}
				throw e;
			}

			messageProperties.setMessageSentDatetime(DomibusConnectorRunnableUtil.convertDateToProperty(new Date()));
			DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(messageProperties, new File(workMessageFolder, messagePropertiesFileName));
			File doneMessageFolder = new File(oldMessageFolderName
					+ DomibusConnectorRunnableConstants.MESSAGE_SENT_FOLDER_POSTFIX);

			LOGGER.debug("Try to rename message folder {} to {}", workMessageFolder.getAbsolutePath(),
					doneMessageFolder.getAbsolutePath());
			try {
				FileUtils.moveDirectory(workMessageFolder, doneMessageFolder);
			} catch (IOException e1) {
				LOGGER.error("Could not rename message folder {} to {}", messageFolder.getAbsolutePath(),
						doneMessageFolder.getAbsolutePath());
			}

		}
	}

	@Override
	public void deliverMessage(Message message) throws DomibusConnectorNationalBackendClientException,
	ImplementationMissingException {
		Date messageReceived = new Date();
		String natMessageId = message.getMessageDetails().getNationalMessageId();

		if (!StringUtils.hasText(natMessageId)) {
			natMessageId = DomibusConnectorRunnableUtil.generateNationalMessageId(message.getMessageDetails()
					.getFromParty().getPartyId(), messageReceived);
			LOGGER.debug("Generated national message ID for incoming message {}", natMessageId);
			message.getMessageDetails().setNationalMessageId(natMessageId);
			util.mergeMessage(message);
		}
		String pathname = incomingMessagesDir.getAbsolutePath() + File.separator + natMessageId;
		LOGGER.debug("Deliver new message into folder {}", pathname);
		File messageFolder = new File(pathname);
		if (!messageFolder.exists()) {
			messageFolder.mkdir();
		}

		DomibusConnectorMessageProperties msgProps = null;
		File messagePropertiesFile = null;
		String action = null;
		if (message.getMessageDetails() != null) {
			if (message.getMessageDetails().getAction() != null)
				action = message.getMessageDetails().getAction().getAction();
			String messagePropertiesPath = messageFolder.getAbsolutePath() + File.separator + messagePropertiesFileName;

			messagePropertiesFile = new File(messagePropertiesPath);
			msgProps = DomibusConnectorRunnableUtil.convertMessageDetailsToMessageProperties(message
					.getMessageDetails(), messageReceived);
		}

		MessageContent messageContent = message.getMessageContent();
		if (messageContent != null) {
			if (messageContent.getPdfDocument() != null && messageContent.getPdfDocument().length > 0) {
				String fileName = null;
				if (StringUtils.hasText(messageContent.getPdfDocumentName())) {
					fileName = messageContent.getPdfDocumentName();
				} else {
					fileName = action != null ? action + DomibusConnectorRunnableConstants.PDF_FILE_EXTENSION
							: DomibusConnectorRunnableConstants.DEFAULT_PDF_FILE_NAME;

				}
				msgProps.setContentPdfFileName(fileName);
				DomibusConnectorRunnableUtil.createFile(messageFolder, fileName, messageContent.getPdfDocument());
			}
			if ((messageContent.getNationalXmlContent() != null && messageContent.getNationalXmlContent().length > 0)
					|| (messageContent.getInternationalContent() != null && messageContent.getInternationalContent().length > 0)) {
				byte[] content = messageContent.getNationalXmlContent() != null ? messageContent
						.getNationalXmlContent() : messageContent.getInternationalContent();
						String fileName = action != null ? action + DomibusConnectorRunnableConstants.XML_FILE_EXTENSION
								: DomibusConnectorRunnableConstants.DEFAULT_XML_FILE_NAME;
						msgProps.setContentXmlFileName(fileName);
						DomibusConnectorRunnableUtil.createFile(messageFolder, fileName, content);
			}
			if (messageContent.getDetachedSignature() != null && messageContent.getDetachedSignature().length > 0
					&& messageContent.getDetachedSignatureMimeType() != null) {
				String fileName = null;
				if (StringUtils.hasText(messageContent.getDetachedSignatureName())
						&& !messageContent.getDetachedSignatureName().equals(
								DomibusConnectorRunnableConstants.DETACHED_SIGNATURE_FILE_NAME)) {
					fileName = messageContent.getDetachedSignatureName();
				} else {
					fileName = DomibusConnectorRunnableConstants.DETACHED_SIGNATURE_FILE_NAME;
					if (messageContent.getDetachedSignatureMimeType().equals(DetachedSignatureMimeType.XML))
						fileName += DomibusConnectorRunnableConstants.XML_FILE_EXTENSION;
					else if (messageContent.getDetachedSignatureMimeType().equals(DetachedSignatureMimeType.PKCS7))
						fileName += DomibusConnectorRunnableConstants.PKCS7_FILE_EXTENSION;
				}
				msgProps.setDetachedSignatureFileName(fileName);
				DomibusConnectorRunnableUtil.createFile(messageFolder, fileName, messageContent.getDetachedSignature());
			}
		}
		LOGGER.debug("Store message properties to file {}", messagePropertiesFile.getAbsolutePath());
		DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(msgProps, messagePropertiesFile);

		if (message.getAttachments() != null) {
			for (MessageAttachment attachment : message.getAttachments()) {
				DomibusConnectorRunnableUtil
				.createFile(messageFolder, attachment.getName(), attachment.getAttachment());
			}
		}

		if (message.getConfirmations() != null) {
			for (MessageConfirmation confirmation : message.getConfirmations()) {
				DomibusConnectorRunnableUtil.createFile(messageFolder, confirmation.getEvidenceType().name()
						+ DomibusConnectorRunnableConstants.XML_FILE_EXTENSION, confirmation.getEvidence());
			}
		}

	}

	@Override
	public Message[] requestConfirmations() throws DomibusConnectorNationalBackendClientException,
	ImplementationMissingException {
		List<Message> unconfirmed = util.findUnconfirmedMessages();
		if (!CollectionUtils.isEmpty(unconfirmed)) {
			LOGGER.info("Found {} unconfirmed incoming messages.", unconfirmed.size());
			List<Message> confirmationMessages = new ArrayList<Message>();
			for (Message originalMessage : unconfirmed) {
				Message deliveryMessage = DomibusConnectorRunnableUtil.createConfirmationMessage(EvidenceType.DELIVERY,
						originalMessage);
				confirmationMessages.add(deliveryMessage);

				Message retrievalMessage = DomibusConnectorRunnableUtil.createConfirmationMessage(
						EvidenceType.RETRIEVAL, originalMessage);
				confirmationMessages.add(retrievalMessage);

			}

			return confirmationMessages.toArray(new Message[confirmationMessages.size()]);
		}
		return null;
	}

	@Override
	public void deliverLastEvidenceForMessage(Message confirmationMessage)
			throws DomibusConnectorNationalBackendClientException, ImplementationMissingException {
		Message originalMessage = util.findOriginalMessage(confirmationMessage.getMessageDetails().getRefToMessageId());

		if (originalMessage == null) {
			throw new DomibusConnectorNationalBackendClientException(
					"Cannot find the original message for ID in database: "
							+ confirmationMessage.getMessageDetails().getRefToMessageId());
		}

		MessageConfirmation confirmation = confirmationMessage.getConfirmations().get(0);
		if (confirmation.getEvidence() != null && confirmation.getEvidence().length > 0) {
			String type = confirmation.getEvidenceType().name();

			File messageFolder = null;
			if (isOutgoingMessage(originalMessage)) {
				messageFolder = new File(outgoingMessagesDir + File.separator
						+ originalMessage.getMessageDetails().getNationalMessageId()
						+ DomibusConnectorRunnableConstants.MESSAGE_SENT_FOLDER_POSTFIX);
				if (!messageFolder.exists() || !messageFolder.isDirectory()) {
					LOGGER.error("Message folder {} for outgoing message does not exist anymore. Create again!",
							messageFolder.getAbsolutePath());
					if (!messageFolder.mkdir()) {
						throw new DomibusConnectorNationalBackendClientException(
								"Outgoing message folder cannot be created!");
					}
				}
			} else {

				String natMessageId = originalMessage.getMessageDetails().getNationalMessageId();
				if (!StringUtils.hasText(natMessageId)) {
					natMessageId = DomibusConnectorRunnableUtil.generateNationalMessageId(originalMessage
							.getMessageDetails().getFromParty().getPartyId(), new Date());
					LOGGER.debug("Generated national message ID for incoming message {}", natMessageId);
					originalMessage.getMessageDetails().setNationalMessageId(natMessageId);
					util.mergeMessage(originalMessage);
				}
				String pathname = incomingMessagesDir.getAbsolutePath() + File.separator + natMessageId;
				messageFolder = new File(pathname);
				if (!messageFolder.exists() || !messageFolder.isDirectory()) {
					LOGGER.error("Message folder {} for incoming message does not exist. Create folder!",
							messageFolder.getAbsolutePath());
					if (!messageFolder.mkdir()) {
						throw new DomibusConnectorNationalBackendClientException(
								"Incoming message folder cannot be created!");
					}
				}
			}

			String path = messageFolder.getAbsolutePath() + File.separator + type
					+ DomibusConnectorRunnableConstants.XML_FILE_EXTENSION;
			LOGGER.debug("Create evidence xml file {}", path);
			File evidenceXml = new File(path);
			try {
				DomibusConnectorRunnableUtil.byteArrayToFile(confirmation.getEvidence(), evidenceXml);
			} catch (IOException e) {
				throw new DomibusConnectorNationalBackendClientException("Could not create file "
						+ evidenceXml.getAbsolutePath(), e);
			}
		}

	}

	private boolean isOutgoingMessage(Message originalMessage) {
		return originalMessage.getDbMessage() != null && originalMessage.getDbMessage().getDirection() != null
				&& originalMessage.getDbMessage().getDirection().equals(MessageDirection.NAT_TO_GW);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String path = System.getProperty("user.dir");

		if (!StringUtils.hasText(incomingMessagesDirectory)) {
			incomingMessagesDirectory = path + File.separator
					+ DomibusConnectorRunnableConstants.INCOMING_MESSAGES_DEFAULT_DIR;
			LOGGER.debug("The property 'incoming.messages.directory' is not set properly as it is null or empty! Default value set!");
		}
		LOGGER.debug("Initializing set incoming messages directory {}", incomingMessagesDirectory);
		incomingMessagesDir = new File(incomingMessagesDirectory);

		if (!incomingMessagesDir.exists()) {
			throw new DomibusConnectorRunnableException("Directory '" + incomingMessagesDirectory + "' does not exist!");
		}

		if (!incomingMessagesDir.isDirectory()) {
			throw new DomibusConnectorRunnableException("'" + incomingMessagesDirectory + "' is not a directory!");
		}

		if (!StringUtils.hasText(outgoingMessagesDirectory)) {
			outgoingMessagesDirectory = path + File.separator
					+ DomibusConnectorRunnableConstants.OUTGOING_MESSAGES_DEFAULT_DIR;
			LOGGER.debug("The property 'outgoing.messages.directory' is not set properly as it is null or empty! Default value set!");
		}
		LOGGER.debug("Initializing set outgoing messages directory {}", outgoingMessagesDirectory);
		outgoingMessagesDir = new File(outgoingMessagesDirectory);

		if (!outgoingMessagesDir.exists()) {
			throw new DomibusConnectorRunnableException("Directory '" + outgoingMessagesDirectory + "' does not exist!");
		}

		if (!outgoingMessagesDir.isDirectory()) {
			throw new DomibusConnectorRunnableException("'" + outgoingMessagesDirectory + "' is not a directory!");
		}

		if (!StringUtils.hasText(messagePropertiesFileName))
			messagePropertiesFileName = DomibusConnectorRunnableConstants.MESSAGE_PROPERTIES_DEFAULT_FILE_NAME;

	}

	private void processMessageFolderFiles(Message message, File workMessageFolder, DomibusConnectorMessageProperties messageProperties)
			throws DomibusConnectorNationalBackendClientException {
		try {
			util.convertMessagePropertiesToMessageDetails(messageProperties, message.getMessageDetails());
		} catch (DomibusConnectorRunnableException e) {
			throw new DomibusConnectorNationalBackendClientException(
					"Error converting message details from message properties!", e);
		}

		String contentXmlFileName = messageProperties.getContentXmlFileName();
		if (!StringUtils.hasText(contentXmlFileName)) {
			throw new DomibusConnectorNationalBackendClientException(
					"Property for contentXmlFileName not set properly! Message cannot be processed!");
		}

		String contentPdfFileName = messageProperties.getContentPdfFileName();
		if (!StringUtils.hasText(contentPdfFileName)) {
			throw new DomibusConnectorNationalBackendClientException(
					"Property for contentPdfFileName not set properly! Message cannot be processed!");
		}

		boolean detachedSignatureExists = false;
		String detachedSignatureFileName = messageProperties.getDetachedSignatureFileName();
		if (StringUtils.hasText(detachedSignatureFileName)) {
			detachedSignatureExists = true;
		}

		int attachmentCount = 1;

		for (File sub : workMessageFolder.listFiles()) {
			if (sub.getName().equals(messagePropertiesFileName)) {
				continue;
			} else {
				MessageContent messageContent = message.getMessageContent();
				if (sub.getName().equals(contentXmlFileName)) {
					LOGGER.debug("Found content xml file with name {}", contentXmlFileName);
					try {
						messageContent.setNationalXmlContent(DomibusConnectorRunnableUtil.fileToByteArray(sub));
					} catch (IOException e) {
						throw new DomibusConnectorNationalBackendClientException(
								"Exception loading content xml into byte array from file " + sub.getName());
					}
					continue;
				} else if (sub.getName().equals(contentPdfFileName)) {
					LOGGER.debug("Found content pdf file with name {}", contentPdfFileName);
					try {
						messageContent.setPdfDocument(DomibusConnectorRunnableUtil.fileToByteArray(sub));
						messageContent.setPdfDocumentName(sub.getName());
					} catch (IOException e) {
						throw new DomibusConnectorNationalBackendClientException(
								"Exception loading content pdf into byte array from file " + sub.getName());
					}
					continue;
				} else if (detachedSignatureExists && sub.getName().equals(detachedSignatureFileName)) {
					LOGGER.debug("Found detached signature file with name {}", detachedSignatureFileName);
					try {
						messageContent.setDetachedSignature(DomibusConnectorRunnableUtil.fileToByteArray(sub));
						messageContent.setDetachedSignatureName(sub.getName());
					} catch (IOException e) {
						throw new DomibusConnectorNationalBackendClientException(
								"Exception loading detached signature into byte array from file " + sub.getName());
					}
					if (detachedSignatureFileName.endsWith(DomibusConnectorRunnableConstants.XML_FILE_EXTENSION)) {
						messageContent.setDetachedSignatureMimeType(DetachedSignatureMimeType.XML);
					} else if (detachedSignatureFileName
							.endsWith(DomibusConnectorRunnableConstants.PKCS7_FILE_EXTENSION)) {
						messageContent.setDetachedSignatureMimeType(DetachedSignatureMimeType.PKCS7);
					} else {
						throw new DomibusConnectorNationalBackendClientException(
								"The detached signature file has an invalid extension! " + sub.getName());
					}
					continue;
				} else {
					LOGGER.debug("Processing attachment File {}", sub.getName());

					byte[] attachmentData = null;
					try {
						attachmentData = DomibusConnectorRunnableUtil.fileToByteArray(sub);
					} catch (IOException e) {
						throw new DomibusConnectorNationalBackendClientException(
								"Exception loading attachment into byte array from file " + sub.getName());
					}
					String attachmentId = DomibusConnectorRunnableConstants.ATTACHMENT_ID_PREFIX + attachmentCount;

					if(!ArrayUtils.isEmpty(attachmentData)){
						MessageAttachment attachment = new MessageAttachment(attachmentData, attachmentId);

						attachment.setName(sub.getName());
						attachmentCount++;
						attachment.setMimeType(DomibusConnectorRunnableUtil.getMimeTypeFromFileName(sub));
						
						LOGGER.debug("Add attachment {}", attachment.toString());
						message.addAttachment(attachment);
					}
				}
			}
		}
	}

	@Override
	public String requestMessageStatusFromGateway(Message message)
			throws DomibusConnectorNationalBackendClientException, ImplementationMissingException {
		return null;
	}

	@Override
	public List<MessageError> requestMessageErrors(Message message)
			throws DomibusConnectorNationalBackendClientException, ImplementationMissingException {
		return null;
	}

}
