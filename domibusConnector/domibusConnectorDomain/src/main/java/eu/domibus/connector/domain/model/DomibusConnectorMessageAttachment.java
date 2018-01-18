package eu.domibus.connector.domain.model;

import java.io.Serializable;


/**
 * This object contains an attachment for a message. With every message there can
 * be some documents (mostly PDF's) sent along with. Therefore those documents are
 * attached to the message over this type.  Attributes:  attachment: The data
 * itself in byte[]  name: The name of the attachment. Most usefull usage is the
 * file name of the attachment.  mimeType: The type of the attachment. Example:
 * "text/xml", "application/pdf"  description:
 * @author riederb
 * @version 1.0
 * @updated 29-Dez-2017 10:12:48
 */
public class DomibusConnectorMessageAttachment implements Serializable {

	private final String identifier;
	private DomibusConnectorBigDataReference attachment;
	private String name;
	private String mimeType;
	private String description;

	/**
	 * Constructor filling the two mandatory attributes
	 * 
	 * @param attachment    The data itself in byte[]
	 * @param identifier    Identifies the attachment for transformation and
	 * transportation
	 */
	public DomibusConnectorMessageAttachment(final DomibusConnectorBigDataReference attachment, final String identifier){
	   this.attachment = attachment;
	   this.identifier = identifier;
	}

	public String getIdentifier(){
		return this.identifier;
	}

	public DomibusConnectorBigDataReference getAttachment(){
		return this.attachment;
	}
    
    public void setAttachment(DomibusConnectorBigDataReference attachment) {
       this.attachment = attachment;
    }

	public String getName(){
		return this.name;
	}

	/**
	 * 
	 * @param name    name
	 */
	public void setName(String name){
		this.name = name;
	}

	public String getMimeType(){
		return this.mimeType;
	}

	/**
	 * 
	 * @param mimeType    mimeType
	 */
	public void setMimeType(String mimeType){
		this.mimeType = mimeType;
	}

	public String getDescription(){
		return this.description;
	}

	/**
	 * 
	 * @param description    description
	 */
	public void setDescription(String description){
		this.description = description;
	}

}