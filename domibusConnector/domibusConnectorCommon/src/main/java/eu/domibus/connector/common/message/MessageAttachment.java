package eu.domibus.connector.common.message;

/**
 * This object contains an attachment for a message. With every message there
 * can be some documents (mostly PDF's) sent along with. Therefore those
 * documents are attached to the message over this type. For every attachment
 * there is the document itself as a byte[], a name for the attachment and a
 * mimeType.
 * 
 * Since 2.0: Additional attributes description and identifier for additional
 * data transmitted for attachments.
 * 
 * @author riederb
 * 
 */
public class MessageAttachment {

    private byte[] attachment;
    private String name;
    private String mimeType;
    private String description;
    private String identifier;

    public MessageAttachment(byte[] attachment, String name, String mimeType) {
        super();
        this.attachment = attachment;
        this.name = name;
        this.mimeType = mimeType;
    }

    public MessageAttachment(byte[] attachment, String name, String mimeType, String description, String identifier) {
        this(attachment, name, mimeType);
        this.description = description;
        this.identifier = identifier;
    }

    public MessageAttachment() {
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "MessageAttachment [name=" + name + ", mimeType=" + mimeType + ", description=" + description
                + ", identifier=" + identifier + "]";
    }

}
