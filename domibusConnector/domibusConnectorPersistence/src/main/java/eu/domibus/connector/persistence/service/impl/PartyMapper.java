package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import javassist.tools.rmi.AppletServer;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nullable;

public class PartyMapper {

    static @Nullable
    DomibusConnectorParty mapPartyToDomain(@Nullable PDomibusConnectorParty persistenceParty) {
        if (persistenceParty != null) {
            eu.domibus.connector.domain.model.DomibusConnectorParty p
                    = new eu.domibus.connector.domain.model.DomibusConnectorParty(
                    persistenceParty.getPartyId(),
                    persistenceParty.getPartyIdType(),
                    persistenceParty.getRole()
            );
            p.setIdentifier(persistenceParty.getPmodePartyIdentifier());
            p.setDbKey(persistenceParty.getId());
            return p;
        }
        return null;
    }

    static @Nullable
    PDomibusConnectorParty mapPartyToPersistence(@Nullable DomibusConnectorParty party) {
        if (party != null) {
            PDomibusConnectorParty persistenceParty = new PDomibusConnectorParty();
            persistenceParty.setPartyId(party.getPartyId());
            persistenceParty.setPartyIdType(party.getPartyIdType());
            persistenceParty.setRole(party.getRole());
            persistenceParty.setPmodePartyIdentifier(party.getIdentifier());
            persistenceParty.setId(party.getDbKey());
            return persistenceParty;
        }
        return null;
    }

}
