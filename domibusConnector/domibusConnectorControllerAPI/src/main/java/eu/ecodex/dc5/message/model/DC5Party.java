package eu.ecodex.dc5.message.model;


import javax.persistence.Embeddable;

@Embeddable
public class DC5Party {
    private String partyId;
    private String partyIdType;

    public DC5Party() {
    }

    public DC5Party(String partyId, String partyIdType) {
        this.partyId = partyId;
        this.partyIdType = partyIdType;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyIdType() {
        return partyIdType;
    }

    public void setPartyIdType(String partyIdType) {
        this.partyIdType = partyIdType;
    }
}
