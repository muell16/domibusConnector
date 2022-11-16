
package eu.domibus.connector.persistence.service;

import eu.ecodex.dc5.message.model.DC5Action;
import java.util.List;


/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 * @deprecated use DomibusConnectorPModeService instead and also make use of default DomibusConnectorMessageLane
 */
@Deprecated
public interface DomibusConnectorActionPersistenceService {

    public DC5Action persistNewAction(DC5Action service);
    
    public List<DC5Action> getActionList();
    
    public DC5Action updateAction(DC5Action oldAction, DC5Action newAction);
    
    public void deleteAction(DC5Action deleteAction);
    
    public DC5Action getAction(String action);

    public DC5Action getRelayREMMDAcceptanceRejectionAction();

    public DC5Action getRelayREMMDFailure();

    public DC5Action getDeliveryNonDeliveryToRecipientAction();

    public DC5Action getRetrievalNonRetrievalToRecipientAction();

	List<String> getActionListString();

}
