package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import java.io.Serializable;
import org.springframework.core.style.ToStringCreator;

/**
 * This is an object that internally represents the evidences for a message. It
 * contains the evidence itself as a byte[] containing a structured document, and
 * an enum type {@link EvidenceType} which describes the evidence type. To be able
 * to connect the confirmation to a message it should be instantiated and added to
 * the {@link DomibusConnectorMessage} object.
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 10:15:46
 */
public class DomibusConnectorMessageConfirmation implements Serializable {

	private DomibusConnectorEvidenceType evidenceType;
	private byte evidence[];

	/**
	 * 
	 * @param evidenceType
	 * @param evidence    evidence
	 */
	public DomibusConnectorMessageConfirmation(DomibusConnectorEvidenceType evidenceType, byte[] evidence){
	   this.evidenceType = evidenceType;
	   this.evidence = evidence;
	}

	/**
	 * 
	 * @param evidenceType    evidenceType
	 */
	public DomibusConnectorMessageConfirmation(DomibusConnectorEvidenceType evidenceType){
	   this.evidenceType = evidenceType;
	}

	public DomibusConnectorMessageConfirmation(){

	}

	public DomibusConnectorEvidenceType getEvidenceType(){
		return this.evidenceType;
	}

	/**
	 * 
	 * @param evidenceType    evidenceType
	 */
	public void setEvidenceType(DomibusConnectorEvidenceType evidenceType){
		this.evidenceType = evidenceType;
	}

	public byte[] getEvidence(){
		return this.evidence;
	}

	/**
	 * 
	 * @param evidence    evidence
	 */
	public void setEvidence(byte[] evidence){
		this.evidence = evidence;
	}

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("evidenceType", this.evidenceType);
        return builder.toString();        
    }
    
}