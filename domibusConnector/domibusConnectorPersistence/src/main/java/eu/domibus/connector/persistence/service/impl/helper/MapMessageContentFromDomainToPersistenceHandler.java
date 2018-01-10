/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
interface MapMessageContentFromDomainToPersistenceHandler {

    default int getStorePriority() { return 100; }
    
    PDomibusConnectorMsgCont convertToConnectorMsgCont(Object obj);
        
}
