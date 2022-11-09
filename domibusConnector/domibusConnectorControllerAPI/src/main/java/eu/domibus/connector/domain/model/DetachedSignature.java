package eu.domibus.connector.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;


/**
 * @author riederb
 * @version 1.0
 */
@Entity
public class DetachedSignature implements Serializable {

	@GeneratedValue
	@Id
	private long id;

	@Lob
	private byte detachedSignature[];
	private String detachedSignatureName;
	private DetachedSignatureMimeType mimeType;

	public DetachedSignature() {}

	/**
	 * 
	 * @param detachedSignature detachedSignature
	 * @param detachedSignatureName detachedSignatureName
	 * @param mimeType    mimeType
	 */
	public DetachedSignature(final byte[] detachedSignature, final String detachedSignatureName, final DetachedSignatureMimeType mimeType){
	   this.detachedSignature = detachedSignature;
	   this.detachedSignatureName = detachedSignatureName;
	   this.mimeType = mimeType;
	}

	public byte[] getDetachedSignature(){
		return this.detachedSignature;
	}

	public String getDetachedSignatureName(){
		return this.detachedSignatureName;
	}

	public DetachedSignatureMimeType getMimeType(){
		return this.mimeType;
	}

}