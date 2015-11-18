package eu.domibus.connector.common.db.model;

import java.io.Serializable;

import javax.persistence.Column;

public class DomibusConnectorPartyPK implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5954185507117364904L;

    @Column(name = "PARTY_ID")
    private String partyId;
    @Column(name = "ROLE")
    private String role;

    public DomibusConnectorPartyPK() {
    }

    public DomibusConnectorPartyPK(String partyId, String role) {
        super();
        this.partyId = partyId;
        this.role = role;
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((partyId == null) ? 0 : partyId.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DomibusConnectorPartyPK other = (DomibusConnectorPartyPK) obj;
        if (partyId == null) {
            if (other.partyId != null)
                return false;
        } else if (!partyId.equals(other.partyId))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }

}
