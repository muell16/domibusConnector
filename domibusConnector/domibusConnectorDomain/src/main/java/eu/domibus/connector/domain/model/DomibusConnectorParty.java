package eu.domibus.connector.domain.model;

import java.io.Serializable;
import org.springframework.core.style.ToStringCreator;


/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 10:05:58
 */
public class DomibusConnectorParty implements Serializable {

	private final String partyId;
	private final String partyIdType;
	private final String role;

	/**
	 * 
	 * @param partyId
	 * @param partyIdType
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
    
    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("partyId", this.partyId);
        builder.append("role", this.role);
        return builder.toString();        
    }

}