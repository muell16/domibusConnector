package eu.domibus.connector.persistence.model.test.util;

import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.connector.persistence.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.model.DomibusConnectorPartyPK;
import eu.domibus.connector.persistence.model.DomibusConnectorService;

/**
 * creates other persistence entities
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class PersistenceEntityCreator {

    public static DomibusConnectorAction createAction() {
        DomibusConnectorAction domibusConnectorAction = new DomibusConnectorAction();
        domibusConnectorAction.setAction("action1");
        domibusConnectorAction.setPdfRequired(true);
        return domibusConnectorAction;
    }
    
    public static DomibusConnectorAction createRelayREMMDAcceptanceRejectionAction() {
        DomibusConnectorAction domibusConnectorAction = new DomibusConnectorAction();
        domibusConnectorAction.setAction("RelayREMMDAcceptanceRejection");
        domibusConnectorAction.setPdfRequired(false);
        return domibusConnectorAction;
    }
    
    public static DomibusConnectorAction createDeliveryNonDeliveryToRecipientAction() {
        DomibusConnectorAction domibusConnectorAction = new DomibusConnectorAction();
        domibusConnectorAction.setAction("DeliveryNonDeliveryToRecipient");
        domibusConnectorAction.setPdfRequired(false);
        return domibusConnectorAction;
    }
    
    public static DomibusConnectorAction createRetrievalNonRetrievalToRecipientAction() {        
        DomibusConnectorAction domibusConnectorAction = new DomibusConnectorAction();
        domibusConnectorAction.setAction("RetrievalNonRetrievalToRecipient");
        domibusConnectorAction.setPdfRequired(false);
        return domibusConnectorAction;
    }
    
    public static DomibusConnectorAction createRelayREMMDFailureAction() {        
        DomibusConnectorAction domibusConnectorAction = new DomibusConnectorAction();
        domibusConnectorAction.setAction("RelayREMMDFailure");
        domibusConnectorAction.setPdfRequired(false);
        return domibusConnectorAction;
    }
    
    public static DomibusConnectorService createServiceEPO() {
        DomibusConnectorService service = new DomibusConnectorService();
        service.setService("EPO");
        service.setServiceType("urn:e-codex:services:");
        return service;
    }
    
    public static DomibusConnectorParty createPartyAT() {
        DomibusConnectorParty at = new DomibusConnectorParty();
        at.setPartyId("AT");
        at.setRole("GW");
        at.setPartyIdType("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");
        return at;
    }
    
    public static DomibusConnectorPartyPK createPartyPKforPartyAT() {
        return new DomibusConnectorPartyPK("AT", "GW");
    }
}
