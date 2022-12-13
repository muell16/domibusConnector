package eu.ecodex.dc5.message.model;

import eu.domibus.connector.domain.model.LargeFileReference;
import lombok.*;
import org.springframework.core.style.ToStringCreator;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;


/**
 * This object contains an attachment for a message. With every message there can
 * be some documents (mostly PDF's) sent along with. Therefore those documents are
 * attached to the message over this type.  Attributes:  attachment: The data
 * itself in byte[]  name: The name of the attachment. Most usefull usage is the
 * file name of the attachment.  mimeType: The type of the attachment. Example:
 * "text/xml", "application/pdf"  description:
 * @author riederb
 * @version 1.0
 */
@Entity


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DomibusConnectorMessageAttachment {

	@GeneratedValue
	@Id
	private long id;

	@NotBlank
	@NonNull
	private String identifier;

	@Convert(converter = StorageReferenceConverter.class)
	private LargeFileReference attachment;
	private String name;
	private String mimeType;
	private String description;

	@NonNull
	@Convert(converter = DigestConverter.class)
	private Digest digest;

	private long size = -1;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private DetachedSignature detachedSignature;

	public static class StorageReferenceConverter implements AttributeConverter<LargeFileReference, String> {

		@Override
		public String convertToDatabaseColumn(LargeFileReference attribute) {
			if (attribute == null) {
				return null;
			}
			return LargeFileReference.mapToString(attribute);
		}

		@Override
		public LargeFileReference convertToEntityAttribute(String dbData) {
			if (dbData == null) {
				return null;
			}
			return LargeFileReference.mapFromString(dbData);
		}
	}


	public String getIdentifier(){
		return this.identifier;
	}

	public LargeFileReference getAttachment(){
		return this.attachment;
	}
    
    public void setAttachment(LargeFileReference attachment) {
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

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("identifier", this.identifier);
        builder.append("name", this.name);
        builder.append("mimeType", this.mimeType);     
        builder.append("dataReference", this.attachment);
        return builder.toString();        
    }
    
}