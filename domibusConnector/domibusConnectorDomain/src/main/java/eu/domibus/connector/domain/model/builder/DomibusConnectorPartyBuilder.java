package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorParty;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorPartyBuilder {

    private String partyId = null;
	private String partyIdType = null;
	private String role = null;
    
    public static DomibusConnectorPartyBuilder createBuilder() {
        return new DomibusConnectorPartyBuilder();
    }
    
    private DomibusConnectorPartyBuilder() {}

    public DomibusConnectorPartyBuilder setPartyId(String partyId) {
        this.partyId = partyId;
        return this;
    }

    public DomibusConnectorPartyBuilder withPartyIdType(String partyIdType) {
        this.partyIdType = partyIdType;
        return this;
    }

    public DomibusConnectorPartyBuilder setRole(String role) {
        this.role = role;
        return this;
    }
    
    public DomibusConnectorParty build() {
        if (partyId == null) {
            throw new RuntimeException("PartyId is not allowed to be null!");
        }
        if (role == null) {
            throw new RuntimeException("Role is not allowed to be null!");
        }
        return new DomibusConnectorParty(partyId, partyIdType, role);
    }
    
    
}