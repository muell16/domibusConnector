package eu.domibus.connector.persistence.model.test.util;

import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.connector.persistence.model.DomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.DomibusConnectorMessageError;
import eu.domibus.connector.persistence.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.model.DomibusConnectorPartyPK;
import eu.domibus.connector.persistence.model.DomibusConnectorService;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

/**
 * creates other persistence entities
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class PersistenceEntityCreator {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    
    public static DomibusConnectorEvidence createDeliveryEvidence() {
        DomibusConnectorEvidence evidence = new DomibusConnectorEvidence();
        evidence.setMessage(createSimpleDomibusConnectorMessage());
        evidence.setType(EvidenceType.DELIVERY);
        evidence.setId(13L);
        return evidence;
    }
    
    
    public static DomibusConnectorEvidence createNonDeliveryEvidence() {
        DomibusConnectorEvidence evidence = new DomibusConnectorEvidence();
        evidence.setMessage(createSimpleDomibusConnectorMessage());
        evidence.setType(EvidenceType.NON_DELIVERY);
        evidence.setId(14L);
        return evidence;
    }
    
    /**
     * creates a error message with
     * #createSimpleDomibusConnectorMessage as message
     * "error detail message" as detailed text
     * "error message" as error message
     * "error source" as error source
     * @return the MessageError
     */
    public static DomibusConnectorMessageError createMessageError() {
        DomibusConnectorMessageError error = new DomibusConnectorMessageError();
        error.setDetailedText("error detail message");
        error.setErrorMessage("error message");
        error.setErrorSource("error source");
        error.setMessage(createSimpleDomibusConnectorMessage());
        return error;
    }

    /**
     * Creates a default DomibusConnectorMessage, for testing purposes
     * @return - the message
     */
    public static DomibusConnectorMessage createSimpleDomibusConnectorMessage() {
        try {
            DomibusConnectorMessage msg = new DomibusConnectorMessage();
            msg.setNationalMessageId("national1");
            msg.setConfirmed(dateFormat.parse("2017-12-23 23:45:23"));
            msg.setConversationId("conversation1");
            msg.setHashValue("hashvalue");
            msg.setId(47L);
            msg.setEvidences(new HashSet<>());
            return msg;
        } catch (ParseException ex) {
            throw new RuntimeException("should not happen!");
        }
    }
}
