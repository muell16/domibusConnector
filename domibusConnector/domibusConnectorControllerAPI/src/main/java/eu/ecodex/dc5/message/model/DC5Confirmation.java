package eu.ecodex.dc5.message.model;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;

import java.util.Arrays;

import lombok.*;
import org.springframework.core.style.ToStringCreator;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This is an object that internally represents the evidences for a message. It
 * contains the evidence itself as a byte[] containing a structured document, and
 * an enum type {@link DomibusConnectorEvidenceType} which describes the evidence type. To be able
 * to connect the confirmation to a message it should be instantiated and added to
 * the {@link DC5Message} object.
 * @author riederb
 * @version 1.0
 *
 */
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DC5Confirmation {

	@GeneratedValue
	@Id
	private long id;

	private DomibusConnectorEvidenceType evidenceType;
	private @Nullable byte evidence[];



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

	public @Nullable  byte[] getEvidence(){
		return this.evidence;
	}

	/**
	 * 
	 * @param evidence    evidence
	 */
	public void setEvidence(@Nullable byte[] evidence){
		this.evidence = evidence;
	}

	@Nullable
	public Long getEvidenceDbId() {
		return id;
	}

	public void setEvidenceDbId(@Nullable Long evidenceDbId) {
		this.id = evidenceDbId;
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("evidenceType", this.evidenceType);
        return builder.toString();        
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DC5Confirmation)) return false;

		DC5Confirmation that = (DC5Confirmation) o;

		return Arrays.equals(evidence, that.evidence);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(evidence);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}