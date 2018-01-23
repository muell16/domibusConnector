/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import java.util.List;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorPartyPersistenceService {

    public DomibusConnectorParty persistNewParty(DomibusConnectorParty newParty);
    
    public List<DomibusConnectorParty> getPartyList();
    
    public void deleteParty(DomibusConnectorParty party);
    
    public DomibusConnectorParty updateParty(DomibusConnectorParty oldParty, DomibusConnectorParty newParty);
    
    public DomibusConnectorParty getParty(String partyId, String role);

    public DomibusConnectorParty getPartyByPartyId(String partyId);

}
