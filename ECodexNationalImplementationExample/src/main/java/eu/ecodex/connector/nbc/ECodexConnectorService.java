package eu.ecodex.connector.nbc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;

import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.enums.ServiceEnum;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorService implements ECodexConnectorNationalBackendClient, InitializingBean {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorService.class);

    private Properties properties;

    private final Map<String, File> messageDirs = new HashMap<String, File>();

    @Override
    public void deliverMessage(Message message) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        String messagesDir = properties.getProperty("listener.dir");

        String messageID = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "_message_received";
        File messageFolder = new File(messagesDir + File.separator + messageID);

        if (message.getMessageContent() != null) {
            if (message.getMessageContent().getPdfDocument() != null
                    && message.getMessageContent().getPdfDocument().length > 0) {
                File contentPdf = new File(messageFolder.getAbsolutePath() + File.separator + "content.pdf");
                try {
                    byteArrayToFile(message.getMessageContent().getPdfDocument(), contentPdf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (message.getMessageContent().getNationalXmlContent() != null
                    && message.getMessageContent().getNationalXmlContent().length > 0) {
                File contentXml = new File(messageFolder.getAbsolutePath() + File.separator + "content.xml");
                try {
                    byteArrayToFile(message.getMessageContent().getNationalXmlContent(), contentXml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (message.getMessageDetails() != null) {
                File messageDetails = new File(messageFolder.getAbsolutePath() + File.separator + "message.details");
                convertMessageDetailsToDetailsFile(messageDetails, message.getMessageDetails());
            }
        }

    }

    @Override
    public String[] requestMessagesUnsent() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {

        String messagesDir = properties.getProperty("listener.dir");

        File messagesFolder = new File(messagesDir);

        List<File> messagesUnsent = new ArrayList<File>();
        if (messagesFolder.exists() && messagesFolder.isDirectory() && messagesFolder.listFiles().length > 0) {
            for (File sub : messagesFolder.listFiles()) {
                if (sub.isDirectory() && sub.getName().endsWith("_message")) {
                    messagesUnsent.add(sub);
                }
            }
        }

        List<String> messageIds = new ArrayList<String>();

        for (File message : messagesUnsent) {
            if (message.listFiles().length > 0) {
                File detailFile = new File(message.getAbsolutePath() + File.separator + "message.details");
                Properties details = loadDetailsFileAsProperties(detailFile);
                String nationalMessageId = details.getProperty("national.message.id");
                if (nationalMessageId != null) {
                    messageIds.add(nationalMessageId);
                    messageDirs.put(nationalMessageId, message);
                }
            }
        }

        return messageIds.toArray(new String[messageIds.size()]);
    }

    @Override
    public void requestMessage(Message message) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        File messageFolder = messageDirs.get(message.getMessageDetails().getNationalMessageId());

        if (messageFolder.exists() && messageFolder.isDirectory() && messageFolder.listFiles().length > 0) {
            String oldMessageFolderName = messageFolder.getAbsolutePath();

            File workMessageFolder = new File(oldMessageFolderName + "_work");
            try {
                FileUtils.moveDirectory(messageFolder, workMessageFolder);
            } catch (IOException e1) {
                throw new ECodexConnectorNationalBackendClientException("Could not rename message folder "
                        + messageFolder.getAbsolutePath() + " to " + workMessageFolder.getAbsolutePath());
            }

            for (File sub : workMessageFolder.listFiles()) {
                if (sub.getName().equals("message.details")) {
                    convertDetailsToMessageDetails(sub, message.getMessageDetails());
                } else if (sub.getName().equals("content.xml")) {
                    try {
                        message.getMessageContent().setNationalXmlContent(fileToByteArray(sub));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (sub.getName().equals("content.pdf")) {
                    try {
                        message.getMessageContent().setPdfDocument(fileToByteArray(sub));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            messageDirs.remove(message.getMessageDetails().getNationalMessageId());

            File doneMessageFolder = new File(oldMessageFolderName + "_done");
            try {
                FileUtils.moveDirectory(workMessageFolder, doneMessageFolder);
            } catch (IOException e1) {
                LOGGER.error("Could not rename message folder {} to {}", workMessageFolder.getAbsolutePath(),
                        doneMessageFolder.getAbsolutePath());
            }

        }
    }

    @Override
    public Message[] requestConfirmations() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        System.out
                .println("This is the national implementation of the requestConfirmations method of the NationalBackendClient");
        return null;
    }

    @Override
    public void deliverLastEvidenceForMessage(Message confirmationMessage)
            throws ECodexConnectorNationalBackendClientException, ImplementationMissingException {
        if (confirmationMessage.getConfirmations() == null || confirmationMessage.getConfirmations().isEmpty()) {
            throw new ECodexConnectorNationalBackendClientException(
                    "method 'deliverLastEvidenceForMessage' called, but no confirmation enclosed!");
        }
        MessageConfirmation messageConfirmation = confirmationMessage.getConfirmations().get(0);

        LOGGER.info("Received evidence of type {} for message {}", messageConfirmation.getEvidenceType().name(),
                confirmationMessage.getMessageDetails().getRefToMessageId());
        try {
            LOGGER.debug(prettyPrint(messageConfirmation.getEvidence()));
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String prettyPrint(byte[] input) throws TransformerFactoryConfigurationError, TransformerException {
        // Instantiate transformer input
        Source xmlInput = new StreamSource(new ByteArrayInputStream(input));
        StreamResult xmlOutput = new StreamResult(new StringWriter());

        // Configure transformer
        Transformer transformer = TransformerFactory.newInstance().newTransformer(); // An
                                                                                     // identity
                                                                                     // transformer
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "testing.dtd");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(xmlInput, xmlOutput);

        return xmlOutput.getWriter().toString();
    }

    private byte[] fileToByteArray(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();

        return data;
    }

    private void byteArrayToFile(byte[] data, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
    }

    private void convertDetailsToMessageDetails(File detailsFile, MessageDetails messageDetails) {
        Properties details = loadDetailsFileAsProperties(detailsFile);

        messageDetails.setAction(ActionEnum.Form_A);
        messageDetails.setService(ServiceEnum.EPO);

        messageDetails.setFinalRecipientAddress(details.getProperty("final.recipient.address"));
        messageDetails.setOriginalSenderAddress(details.getProperty("original.sender.address"));

        String toName = details.getProperty("recipient.name");
        String toRole = details.getProperty("recipient.role");
        messageDetails.setToPartner(PartnerEnum.findValue(toName, toRole));
    }

    private void convertMessageDetailsToDetailsFile(File detailsFile, MessageDetails messageDetails) {
        if (!detailsFile.exists()) {
            try {
                detailsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Properties details = new Properties();
        details.setProperty("recipient.name", messageDetails.getToPartner().getName());
        details.setProperty("recipient.role", messageDetails.getToPartner().getRole());
        details.setProperty("sender.name", messageDetails.getFromPartner().getName());
        details.setProperty("sender.role", messageDetails.getFromPartner().getRole());
        // details.setProperty("final.recipient.address", "");
        // details.setProperty("original.sender.address", "");
        details.setProperty("action", messageDetails.getAction().toString());
        details.setProperty("service", messageDetails.getService().toString());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(detailsFile);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }

        try {
            details.store(fos, null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Properties loadDetailsFileAsProperties(File detailsFile) {

        Properties details = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(detailsFile);
            details.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return details;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/example.properties");
        properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
