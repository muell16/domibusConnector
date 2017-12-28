/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.test.util;

import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Party;
import eu.domibus.connector.domain.Service;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomainCreator {

    public static Party createPartyAT() {
        Party p = new Party();
        p.setPartyId("AT");
        p.setRole("GW");
        p.setPartyIdType("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");
        return p;
    }
    
    public static Action createActionForm_A() {
        Action a = new Action();
        a.setAction("Form_A");
        a.setPdfRequired(true);
        return a;                
    }
    
    public static Action createActionRelayREMMDAcceptanceRejection() {
        Action a = new Action();
        a.setAction("RelayREMMDAcceptanceRejection");
        a.setPdfRequired(true);
        return a;
    }
    
    public static Service createServiceEPO() {
        Service s = new Service();
        s.setService("EPO");
        s.setServiceType("urn:e-codex:services:");
        return s;
    }
}
