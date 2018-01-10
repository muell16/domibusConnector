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
interface MapMessageContentFromPersistenceToDomainHandler {

    default int getLoadPriority() { return 100; }
    
    Object convertFrom(PDomibusConnectorMsgCont content);
    
    boolean canConvertFrom(PDomibusConnectorMsgCont content);
        
}
