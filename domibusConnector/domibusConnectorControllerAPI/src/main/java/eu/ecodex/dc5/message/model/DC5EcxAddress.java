package eu.ecodex.dc5.message.model;


import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class DC5EcxAddress {

    private String ecxAddress;

    @Embedded
    private DC5Party party;
    @Embedded
    private DC5Role role;

    public DC5EcxAddress() {
    }
    public DC5EcxAddress(String ecxAddress, DC5Party party, DC5Role role) {
        this.ecxAddress = ecxAddress;
        this.party = party;
        this.role = role;
    }

    public String getEcxAddress() {
        return ecxAddress;
    }

    public void setEcxAddress(String ecxAddress) {
        this.ecxAddress = ecxAddress;
    }

    public DC5Party getParty() {
        return party;
    }

    public void setParty(DC5Party party) {
        this.party = party;
    }

    public DC5Role getRole() {
        return role;
    }

    public void setRole(DC5Role role) {
        this.role = role;
    }
}
