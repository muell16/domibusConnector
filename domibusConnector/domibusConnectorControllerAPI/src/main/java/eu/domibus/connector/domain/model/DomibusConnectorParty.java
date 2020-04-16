package eu.domibus.connector.domain.model;

import java.io.Serializable;
import org.springframework.core.style.ToStringCreator;


/**
 * @author riederb
 * @version 1.0
 */
public class DomibusConnectorParty implements Serializable {

	private Long dbKey;
	private String partyName;

	private String partyId;
	private String partyIdType;
	private String role;


	/**
	 * Default constructor, needed for frameworks
	 * to serialize and deserialize objects of this class
	 */
	public DomibusConnectorParty() {}

	/**
	 * DomibusConnectorMessageDocument
	 * @param partyId partyId
	 * @param partyIdType partyIdType
	 * @param role    role
	 */
	public DomibusConnectorParty(final String partyId, final String partyIdType, final String role){
	   this.partyId = partyId;
	   this.partyIdType = partyIdType;
	   this.role = role;
	}

	public Long getDbKey() {
		return dbKey;
	}

	public void setDbKey(Long dbKey) {
		this.dbKey = dbKey;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getPartyId(){
		return this.partyId;
	}

	public String getPartyIdType(){
		return this.partyIdType;
	}

	public String getRole(){
		return this.role;
	}

	public void setPartyIdType(String partyIdType) {
		this.partyIdType = partyIdType;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("partyId", this.partyId);
		builder.append("partyIdType", this.partyIdType);
        builder.append("role", this.role);
        return builder.toString();        
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DomibusConnectorParty)) return false;

		DomibusConnectorParty that = (DomibusConnectorParty) o;

		if (partyId != null ? !partyId.equals(that.partyId) : that.partyId != null) return false;
		if (partyIdType != null ? !partyIdType.equals(that.partyIdType) : that.partyIdType != null) return false;
		return role != null ? role.equals(that.role) : that.role == null;
	}

	@Override
	public int hashCode() {
		int result = partyId != null ? partyId.hashCode() : 0;
		result = 31 * result + (partyIdType != null ? partyIdType.hashCode() : 0);
		result = 31 * result + (role != null ? role.hashCode() : 0);
		return result;
	}
}