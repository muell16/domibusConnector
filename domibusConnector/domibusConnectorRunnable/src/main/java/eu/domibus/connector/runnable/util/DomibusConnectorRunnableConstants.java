package eu.domibus.connector.runnable.util;

public class DomibusConnectorRunnableConstants {

    public static final String XML_FILE_EXTENSION = ".xml";
    public static final String PDF_FILE_EXTENSION = ".pdf";
    public static final String PKCS7_FILE_EXTENSION = ".p7b";
    public static final String DEFAULT_XML_FILE_NAME = "content.xml";
    public static final String DEFAULT_PDF_FILE_NAME = "content.pdf";
    public static final String DETACHED_SIGNATURE_FILE_NAME = "detachedSignature";
    public static final String ATTACHMENT_ID_PREFIX = "attachment_";
    
    public static final String MESSAGE_NEW_FOLDER_POSTFIX = "_new";
    public static final String MESSAGE_READY_FOLDER_POSTFIX = "_message";
    public static final String MESSAGE_PROCESSING_FOLDER_POSTFIX = "_processing";
    public static final String MESSAGE_SENT_FOLDER_POSTFIX = "_sent";
    public static final String MESSAGE_FAILED_FOLDER_POSTFIX = "_failed";

    public static final String MESSAGE_PROPERTIES_DEFAULT_FILE_NAME = "message.properties";
    public static final String INCOMING_MESSAGES_DEFAULT_DIR = "messages/incoming";
    public static final String OUTGOING_MESSAGES_DEFAULT_DIR = "messages/outgoing";

    public static final int MESSAGE_TYPE_OUTGOING = 1;
    public static final int MESSAGE_TYPE_INCOMING = 2;
}
