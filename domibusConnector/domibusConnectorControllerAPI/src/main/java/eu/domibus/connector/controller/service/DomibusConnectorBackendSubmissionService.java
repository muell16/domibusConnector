/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.controller.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * should be implemented by the Service which is delivering the messages
 * to the backend
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorBackendSubmissionService {

    public void submitToBackend(DomibusConnectorMessage message);
    
}
