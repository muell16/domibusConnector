package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.service.DomibusConnectorPartyPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createPartyAT;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createPartyPKforPartyAT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

public class DomibusConnectorPartyPersistenceServiceImplTest {

    @Mock
    DomibusConnectorPartyDao domibusConnectorPartyDao;

    DomibusConnectorPartyPersistenceService partyPersistenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DomibusConnectorPartyPersistenceServiceImpl partyPersistenceServiceImpl = new DomibusConnectorPartyPersistenceServiceImpl();
        partyPersistenceServiceImpl.setPartyDao(domibusConnectorPartyDao);
        partyPersistenceService = partyPersistenceServiceImpl;
    }

    @Test
    public void testGetParty() {

        Mockito.when(this.domibusConnectorPartyDao.findById(eq(createPartyPKforPartyAT())))
                .thenReturn(Optional.of(createPartyAT()));

        eu.domibus.connector.domain.model.DomibusConnectorParty party = partyPersistenceService.getParty("AT", "GW");

        assertThat(party).isNotNull();
        assertThat(party.getPartyId()).isEqualTo("AT");
        assertThat(party.getRole()).isEqualTo("GW");
        assertThat(party.getPartyIdType()).isEqualTo("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");
    }

    @Test
    public void testGetPartyByPartyId() {

        Mockito.when(this.domibusConnectorPartyDao.findOneByPartyId(eq("AT")))
                .thenReturn(createPartyAT());

        eu.domibus.connector.domain.model.DomibusConnectorParty party = partyPersistenceService.getPartyByPartyId("AT");

        assertThat(party).isNotNull();
        assertThat(party.getPartyId()).isEqualTo("AT");
        assertThat(party.getRole()).isEqualTo("GW");
        assertThat(party.getPartyIdType()).isEqualTo("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");

    }

}