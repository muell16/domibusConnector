package eu.domibus.connector.domain.model;


/**
 * @author riederb
 * @version 1.0
 * @created 29-Dez-2017 10:05:58
 */
public class DomibusConnectorParty {

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

}