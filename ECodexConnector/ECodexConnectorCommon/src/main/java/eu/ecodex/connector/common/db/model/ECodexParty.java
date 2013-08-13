package eu.ecodex.connector.common.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ECodexPartyPK.class)
@Table(name = "ECODEX_PARTY")
public class ECodexParty {

    @Id
    @Column(name = "PARTY_ID")
    private String partyId;

    @Id
    @Column(name = "ROLE")
    private String role;

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

}
