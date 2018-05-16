package eu.domibus.connector.domain.model;

import java.io.Serializable;
import org.springframework.core.style.ToStringCreator;


/**
 * @author riederb
 * @version 1.0
 */
public class DomibusConnectorParty implements Serializable {

	private String partyId;
	private String partyIdType;
	private String role;

	/**
	 * 
	 * @param partyId partyId
	 * @param partyIdType partyIdType
	 * @param role    role
	 */
	public DomibusConnectorParty(final String partyId, final String partyIdType, final String role){
	   this.partyId = partyId;
	   this.partyIdType = partyIdType;
	   this.role = role;
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
        builder.append("role", this.role);
        return builder.toString();        
    }

}