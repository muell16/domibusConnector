/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import java.util.List;


/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorActionPersistenceService {

    public DomibusConnectorAction persistNewAction(DomibusConnectorAction service);
    
    public List<DomibusConnectorAction> getActionList();
    
    public DomibusConnectorAction updateAction(DomibusConnectorAction oldAction, DomibusConnectorAction newAction);
    
    public void deleteAction(DomibusConnectorAction deleteAction);
    
    public DomibusConnectorAction getAction(String action);

    public DomibusConnectorAction getRelayREMMDAcceptanceRejectionAction();

    public DomibusConnectorAction getRelayREMMDFailure();

    public DomibusConnectorAction getDeliveryNonDeliveryToRecipientAction();

    public DomibusConnectorAction getRetrievalNonRetrievalToRecipientAction();

}
