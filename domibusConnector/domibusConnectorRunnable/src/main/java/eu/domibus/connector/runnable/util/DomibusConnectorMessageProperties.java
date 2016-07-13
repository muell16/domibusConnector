package eu.domibus.connector.runnable.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class DomibusConnectorMessageProperties {

    private static final String SERVICE_PROPERTY_NAME = "service";
    private static final String ACTION_PROPERTY_NAME = "action";
    private static final String TO_PARTY_ROLE_PROPERTY_NAME = "to.party.role";
    private static final String TO_PARTY_ID_PROPERTY_NAME = "to.party.id";
    private static final String FROM_PARTY_ROLE_PROPERTY_NAME = "from.party.role";
    private static final String FROM_PARTY_ID_PROPERTY_NAME = "from.party.id";
    private static final String ORIGINAL_SENDER_PROPERTY_NAME = "original.sender";
    private static final String FINAL_RECIPIENT_PROPERTY_NAME = "final.recipient";
    private static final String NATIONAL_MESSAGE_ID_PROPERTY_NAME = "national.message.id";
    private static final String EBMS_MESSAGE_ID_PROPERTY_NAME = "ebms.message.id";
    private static final String CONVERSATION_ID_PROPERTY_NAME = "conversation.id";
    private static final String CONTENT_PDF_FILE_PROPERTY_NAME = "content.pdf.file.name";
    private static final String CONTENT_XML_FILE_PROPERTY_NAME = "content.xml.file.name";
    private static final String DETACHED_SIGNATURE_PROPERTY_NAME = "detached.signature.file.name";
    private static final String MESSAGE_RECEIVED_PROPERTY_NAME = "message.received.datetime";
    private static final String MESSAGE_SENT_PROPERTY_NAME = "message.sent.datetime";

    private final Properties properties = new Properties();

    public void loadPropertiesFromFile(File messagePropertiesFile) {
        // properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(messagePropertiesFile);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            properties.load(fileInputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void storePropertiesToFile(File messagePropertiesFile) {
        if (!messagePropertiesFile.exists()) {
            try {
                messagePropertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(messagePropertiesFile);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }

        try {
            properties.store(fos, null);
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

    public String getService() {
        return properties.getProperty(SERVICE_PROPERTY_NAME);
    }

    public void setService(String service) {
        properties.setProperty(SERVICE_PROPERTY_NAME, service);
    }

    public String getAction() {
        return properties.getProperty(ACTION_PROPERTY_NAME);
    }

    public void setAction(String action) {
        properties.setProperty(ACTION_PROPERTY_NAME, action);
    }

    public String getFromPartyId() {
        return properties.getProperty(FROM_PARTY_ID_PROPERTY_NAME);
    }

    public void setFromPartyId(String fromPartyId) {
        properties.setProperty(FROM_PARTY_ID_PROPERTY_NAME, fromPartyId);
    }

    public String getFromPartyRole() {
        return properties.getProperty(FROM_PARTY_ROLE_PROPERTY_NAME);
    }

    public void setFromPartyRole(String fromPartyRole) {
        properties.setProperty(FROM_PARTY_ROLE_PROPERTY_NAME, fromPartyRole);
    }

    public String getToPartyId() {
        return properties.getProperty(TO_PARTY_ID_PROPERTY_NAME);
    }

    public void setToPartyId(String toPartyId) {
        properties.setProperty(TO_PARTY_ID_PROPERTY_NAME, toPartyId);
    }

    public String getToPartyRole() {
        return properties.getProperty(TO_PARTY_ROLE_PROPERTY_NAME);
    }

    public void setToPartyRole(String toPartyRole) {
        properties.setProperty(TO_PARTY_ROLE_PROPERTY_NAME, toPartyRole);
    }

    public String getOriginalSender() {
        return properties.getProperty(ORIGINAL_SENDER_PROPERTY_NAME);
    }

    public void setOriginalSender(String originalSender) {
        properties.setProperty(ORIGINAL_SENDER_PROPERTY_NAME, originalSender);
    }

    public String getFinalRecipient() {
        return properties.getProperty(FINAL_RECIPIENT_PROPERTY_NAME);
    }

    public void setFinalRecipient(String finalRecipient) {
        properties.setProperty(FINAL_RECIPIENT_PROPERTY_NAME, finalRecipient);
    }

    public String getNationalMessageId() {
        return properties.getProperty(NATIONAL_MESSAGE_ID_PROPERTY_NAME);
    }

    public void setNationalMessageId(String nationalMessageId) {
        properties.setProperty(NATIONAL_MESSAGE_ID_PROPERTY_NAME, nationalMessageId);
    }

    public String getEbmsMessageId() {
        return properties.getProperty(EBMS_MESSAGE_ID_PROPERTY_NAME);
    }

    public void setEbmsMessageId(String ebmsMessageId) {
        properties.setProperty(EBMS_MESSAGE_ID_PROPERTY_NAME, ebmsMessageId);
    }
    
    public String getConversationId(){
    	return properties.getProperty(CONVERSATION_ID_PROPERTY_NAME);
    }
    
    public void setConversationId(String conversationId){
    	properties.setProperty(CONVERSATION_ID_PROPERTY_NAME, conversationId);
    }

    public String getContentPdfFileName() {
        return properties.getProperty(CONTENT_PDF_FILE_PROPERTY_NAME);
    }

    public void setContentPdfFileName(String contentPdfFileName) {
        properties.setProperty(CONTENT_PDF_FILE_PROPERTY_NAME, contentPdfFileName);
    }

    public String getContentXmlFileName() {
        return properties.getProperty(CONTENT_XML_FILE_PROPERTY_NAME);
    }

    public void setContentXmlFileName(String contentXmlFileName) {
        properties.setProperty(CONTENT_XML_FILE_PROPERTY_NAME, contentXmlFileName);
    }

    public String getDetachedSignatureFileName() {
        return properties.getProperty(DETACHED_SIGNATURE_PROPERTY_NAME);
    }

    public void setDetachedSignatureFileName(String detachedSignatureFileName) {
        properties.setProperty(DETACHED_SIGNATURE_PROPERTY_NAME, detachedSignatureFileName);
    }
    
    public String getMessageReceivedDatetime(){
    	return properties.getProperty(MESSAGE_RECEIVED_PROPERTY_NAME);
    }
    
    public void setMessageReceivedDatetime(String messageReceivedDatetime){
    	properties.setProperty(MESSAGE_RECEIVED_PROPERTY_NAME, messageReceivedDatetime);
    }

	public String getMessageSentDatetime() {
		return properties.getProperty(MESSAGE_SENT_PROPERTY_NAME);
	}
	
	public void setMessageSentDatetime(String messageSentDatetime){
    	properties.setProperty(MESSAGE_SENT_PROPERTY_NAME, messageSentDatetime);
    }

}
