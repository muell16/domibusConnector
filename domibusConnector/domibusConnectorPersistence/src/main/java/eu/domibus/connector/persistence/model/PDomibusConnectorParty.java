package eu.domibus.connector.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(PDomibusConnectorPartyPK.class)
@Table(name = "DOMIBUS_CONNECTOR_PARTY")
public class PDomibusConnectorParty {

    @Id
    @Column(name = "PARTY_ID")
    private String partyId;

    @Id
    @Column(name = "ROLE")
    private String role;

    @Column(name = "PARTY_ID_TYPE")
    private String partyIdType;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPartyIdType() {
        return partyIdType;
    }

    public void setPartyIdType(String partyIdType) {
        this.partyIdType = partyIdType;
    }

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", partyId);
        toString.append("role", role);
        toString.append("idType", partyIdType);
        return toString.build();
    }
}
