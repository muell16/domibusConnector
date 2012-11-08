package eu.ecodex.connector.common.message;

public class MessageAttachment {

    private byte[] attachment;
    private String name;
    private String mimeType;

    public MessageAttachment(byte[] attachment, String name, String mimeType) {
        super();
        this.attachment = attachment;
        this.name = name;
        this.mimeType = mimeType;
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

}
