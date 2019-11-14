package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.builder.DomibusConnectorPartyBuilder;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class PartyMapperTest {


    @Test
    public void mapPartyToDomain() throws Exception {
        PDomibusConnectorParty dbParty = new PDomibusConnectorParty();
        dbParty.setPartyId("partyId");
        dbParty.setPartyIdType("partyIdType");
        dbParty.setRole("role");

        DomibusConnectorParty domibusConnectorParty = PartyMapper.mapPartyToDomain(dbParty);

        assertThat(domibusConnectorParty.getPartyId()).isEqualTo("partyId");
        assertThat(domibusConnectorParty.getPartyIdType()).isEqualTo("partyIdType");
        assertThat(domibusConnectorParty.getRole()).isEqualTo("role");
    }

    @Test
    public void mapPartyToDomain_mapNull_shouldRetNull() throws Exception {
        assertThat(PartyMapper.mapPartyToDomain(null)).isNull();
    }


    @Test
    public void mapPartyToPersistence() throws Exception {
        DomibusConnectorParty domainParty = DomibusConnectorPartyBuilder.createBuilder()
                .setPartyId("partyId")
                .setRole("role")
                .withPartyIdType("partyIdType")
                .build();

        PDomibusConnectorParty dbParty = PartyMapper.mapPartyToPersistence(domainParty);

        assertThat(dbParty.getPartyId()).isEqualTo("partyId");
        assertThat(dbParty.getRole()).isEqualTo("role");
        assertThat(dbParty.getPartyIdType()).isEqualTo("partyIdType");
    }

    @Test
    public void mapPartyToPersistence_mapNull_shouldRetNull() {
        assertThat(PartyMapper.mapPartyToPersistence(null)).isNull();
    }

}