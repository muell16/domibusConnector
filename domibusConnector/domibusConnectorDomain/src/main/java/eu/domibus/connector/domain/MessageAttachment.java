package eu.domibus.connector.domain;

/**
 * This object contains an attachment for a message. With every message there
 * can be some documents (mostly PDF's) sent along with. Therefore those
 * documents are attached to the message over this type. 
 * 
 * Attributes:
 * 
 * attachment: The data itself in byte[]
 * 
 * name: The name of the attachment. Most usefull usage is the file name of the attachment.
 * 
 * mimeType: The type of the attachment. Example: "text/xml", "application/pdf"
 * 
 * description: 
 * 
 * 
 * @author riederb
 * 
 */
public class MessageAttachment {

	private final String identifier;
    private final byte[] attachment;
    private String name;
    private String mimeType;
    private String description;

    
    /**
     * Constructor filling the two mandatory attributes
     * 
     * @param attachment The data itself in byte[]
     * @param identifier Identifies the attachment for transformation and transportation
     */
    public MessageAttachment(byte[] attachment, String identifier) {
    	super();
    	this.attachment = attachment;
    	this.identifier = identifier;
    }
    
    public String getIdentifier() {
    	return identifier;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name The name of the attachment. Most usefull usage is the file name of the attachment.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType The type of the attachment. Example: "text/xml", "application/pdf"
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @param description A description of the attachment.
     */
    public void setDescription(String description) {
        this.description = description;
    }

	@Override
	public String toString() {
		return "MessageAttachment [identifier=" + identifier + ", name=" + name + ", mimeType=" + mimeType
				+ ", description=" + description + "]";
	}


}
