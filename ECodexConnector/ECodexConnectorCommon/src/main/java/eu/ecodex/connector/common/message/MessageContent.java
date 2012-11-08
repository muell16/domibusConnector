package eu.ecodex.connector.common.message;

/**
 * This is a container object that contains the XML content of the message. It
 * is mandatory to build a {@link Message} object.
 * 
 * @author riederb
 * 
 */
public class MessageContent {

    private byte[] xmlContent;

    public byte[] getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(byte[] xmlContent) {
        this.xmlContent = xmlContent;
    }

}
