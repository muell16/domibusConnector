/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.model.builder.DomibusConnectorActionBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorPartyBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorServiceBuilder;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class CopyHelper {

    public static DomibusConnectorAction copyAction(DomibusConnectorAction action) {
        return DomibusConnectorActionBuilder.createBuilder()
                .copyPropertiesFrom(action)
                .build();                
    }
    
    public static DomibusConnectorParty copyParty(DomibusConnectorParty party) {
        return DomibusConnectorPartyBuilder.createBuilder()
                .copyPropertiesFrom(party)
                .build();
    }
    
    public static DomibusConnectorService copyService(DomibusConnectorService service) {
        return DomibusConnectorServiceBuilder.createBuilder()
                .copyPropertiesFrom(service)
                .build();
    }
    
    
}
