package eu.domibus.connector.persistence.service.impl;

import eu.ecodex.dc5.message.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;

import javax.annotation.Nullable;

public class PartyMapper {

    static @Nullable
    DomibusConnectorParty mapPartyToDomain(@Nullable PDomibusConnectorParty persistenceParty) {
        if (persistenceParty != null) {
            DomibusConnectorParty p
                    = new DomibusConnectorParty(
                    persistenceParty.getPartyId(),
                    persistenceParty.getPartyIdType(),
                    persistenceParty.getRole()
            );
            p.setPartyName(persistenceParty.getPmodePartyIdentifier());
            p.setRoleType(persistenceParty.getRoleType());
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
            persistenceParty.setPmodePartyIdentifier(party.getPartyName());
            persistenceParty.setRoleType(party.getRoleType());
            persistenceParty.setId(party.getDbKey());
            return persistenceParty;
        }
        return null;
    }

}
