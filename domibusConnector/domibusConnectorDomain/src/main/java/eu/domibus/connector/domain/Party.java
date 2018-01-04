/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Deprecated
public class Party {

    private String partyId;

    private String role;

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
    
    
    
}
