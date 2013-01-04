package eu.ecodex.connector.nbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.enums.ServiceEnum;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorService implements ECodexConnectorNationalBackendClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorService.class);

    private final Map<String, File> messageDirs = new HashMap<String, File>();

    @Override
    public void deliverMessage(Message message) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        System.out
                .println("This is the national implementation of the deliverMessage method of the NationalBackendClient");

    }

    @Override
    public String[] requestMessagesUnsent() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        InputStream is = this.getClass().getResourceAsStream("/example.properties");
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String messagesDir = props.getProperty("listener.dir");

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
            if (!messageFolder.renameTo(workMessageFolder)) {
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
            if (!workMessageFolder.renameTo(doneMessageFolder)) {
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
        System.out
                .println("This is the national implementation of the deliverLastEvidenceForMessage method of the NationalBackendClient");

    }

    private byte[] fileToByteArray(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();

        return data;
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

    private Properties loadDetailsFileAsProperties(File detailsFile) {
        Properties details = new Properties();
        try {
            details.load(new FileInputStream(detailsFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return details;
    }

}
