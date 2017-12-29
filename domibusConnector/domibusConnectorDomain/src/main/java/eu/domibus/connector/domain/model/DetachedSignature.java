package eu.domibus.connector.domain.model;


/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 10:05:58
 */
public class DetachedSignature {

	private final byte detachedSignature[];
	private final String detachedSignatureName;
	private final DetachedSignatureMimeType mimeType;

	/**
	 * 
	 * @param detachedSignature
	 * @param detachedSignatureName
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