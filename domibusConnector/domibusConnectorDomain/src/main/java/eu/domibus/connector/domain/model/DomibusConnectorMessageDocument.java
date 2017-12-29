package eu.domibus.connector.domain.model;


/**
 * Holds the printable document to a message. The document itself is a byte[]. A
 * documentName that the document is identified with and optionally a {@link
 * DetachedSignature} that the document is signed with are also content of this
 * object.
 * @author riederb
 * @version 1.0
 * @updated 29-Dez-2017 10:12:49
 */
public class DomibusConnectorMessageDocument {

	private final byte document[];
	private final String documentName;
	private final DetachedSignature detachedSignature;
	private String hashValue;

	/**
	 * Constructor for DomibusConnectorMessageDocument with all attributes required
	 * and one optional attribute.
	 * 
	 * @param document    the printable document as a byte[]
	 * @param documentName    the name of the printable document the document is
	 * identified with.
	 * @param detachedSignature    may be null. If the document is signed with a
	 * detached signature, the signature parameters are given here.
	 */
	public DomibusConnectorMessageDocument(final byte[] document, final String documentName, final DetachedSignature detachedSignature){
	   this.document = document;
	   this.documentName = documentName;
	   this.detachedSignature = detachedSignature;
	}

	public byte[] getDocument(){
		return this.document;
	}

	public String getDocumentName(){
		return this.documentName;
	}

	public DetachedSignature getDetachedSignature(){
		return this.detachedSignature;
	}

	public String getHashValue(){
		return this.hashValue;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setHashValue(String newVal){
		this.hashValue = hashValue;
	}

}