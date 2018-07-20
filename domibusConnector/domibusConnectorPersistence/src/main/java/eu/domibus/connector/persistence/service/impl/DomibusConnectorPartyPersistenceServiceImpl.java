package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorPartyPK;
import eu.domibus.connector.persistence.service.DomibusConnectorPartyPersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomibusConnectorPartyPersistenceServiceImpl implements DomibusConnectorPartyPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorPartyPersistenceServiceImpl.class);

    DomibusConnectorPartyDao partyDao;

    @Autowired
    public void setPartyDao(DomibusConnectorPartyDao partyDao) {
        this.partyDao = partyDao;
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorParty getParty(String partyId, String role) {
        PDomibusConnectorPartyPK pk = new PDomibusConnectorPartyPK(partyId, role);
        PDomibusConnectorParty party = partyDao.findById(pk).get();
        return PartyMapper.mapPartyToDomain(party);
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorParty getPartyByPartyId(String partyId) {
        PDomibusConnectorParty party = partyDao.findOneByPartyId(partyId);
        return PartyMapper.mapPartyToDomain(party);
    }



    @Override
    public DomibusConnectorParty persistNewParty(DomibusConnectorParty newParty) {
        PDomibusConnectorParty dbParty = PartyMapper.mapPartyToPersistence(newParty);
        dbParty = this.partyDao.save(dbParty);
        return PartyMapper.mapPartyToDomain(dbParty);
    }

    @Override
    public List<DomibusConnectorParty> getPartyList() {
        final List<DomibusConnectorParty> parties = new ArrayList<>();
        this.partyDao.findAll().forEach((dbParty) -> {
            DomibusConnectorParty p = PartyMapper.mapPartyToDomain(dbParty);
            parties.add(p);
        });
        return parties;
    }

    @Override
    public void deleteParty(DomibusConnectorParty party) {
        PDomibusConnectorParty dbParty = PartyMapper.mapPartyToPersistence(party);
        this.partyDao.delete(dbParty);
    }

    @Override
    public DomibusConnectorParty updateParty(DomibusConnectorParty oldParty, DomibusConnectorParty newParty) {
        PDomibusConnectorParty newDbParty = PartyMapper.mapPartyToPersistence(newParty);
        PDomibusConnectorParty updatedParty = this.partyDao.save(newDbParty);
        return PartyMapper.mapPartyToDomain(updatedParty);
    }

}
