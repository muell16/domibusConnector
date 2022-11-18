package eu.ecodex.dc5.domain.service;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.repo.DC5BusinessDomainJpaEntity;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import eu.ecodex.dc5.domain.repo.DomibusConnectorBusinessDomainDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DCBusinessDomainPersistenceServiceImpl implements DCBusinessDomainPersistenceService {

    private final DomibusConnectorBusinessDomainDao businessDomainDao;

    public DCBusinessDomainPersistenceServiceImpl(DomibusConnectorBusinessDomainDao businessDomainDao) {
        this.businessDomainDao = businessDomainDao;
    }

    @Override
    public Optional<DomibusConnectorBusinessDomain> findById(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        Optional<DC5BusinessDomainJpaEntity> byName = businessDomainDao.findByName(businessDomainId);
        return byName.map(this::mapToDomain);
    }

    @Override
    public List<DomibusConnectorBusinessDomain> findAll() {
        return businessDomainDao.findAll().stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public DomibusConnectorBusinessDomain update(DomibusConnectorBusinessDomain domibusConnectorBusinessDomain) {
        if (domibusConnectorBusinessDomain == null) {
            throw new IllegalArgumentException("domibusConnectorBusinessDomain is not allowed to be null!");
        }
        Optional<DC5BusinessDomainJpaEntity> lane = businessDomainDao.findByName(domibusConnectorBusinessDomain.getId());
        if (lane.isPresent()) {
            DC5BusinessDomainJpaEntity dbBusinessDomain = this.mapToDb(domibusConnectorBusinessDomain, lane.get());
            DC5BusinessDomainJpaEntity save = businessDomainDao.save(dbBusinessDomain);
            return this.mapToDomain(save);
        } else {
            throw new IllegalArgumentException(String.format("No BusinessDomain configured with id [%s]", domibusConnectorBusinessDomain.getId()));
        }
    }

    @Override
    public DomibusConnectorBusinessDomain create(DomibusConnectorBusinessDomain businessDomain) {
        if (businessDomain == null) {
            throw new IllegalArgumentException("Null is not allowed for businessDomain");
        }
        if (businessDomain.getId() == null) {
            throw new IllegalArgumentException("Null is not allowed for businessDomainId!");
        }
        DC5BusinessDomainJpaEntity dbBusinessDomain = this.mapToDb(businessDomain, new DC5BusinessDomainJpaEntity());
        dbBusinessDomain.setName(businessDomain.getId());
        DC5BusinessDomainJpaEntity save = businessDomainDao.save(dbBusinessDomain);
        return this.mapToDomain(save);
    }

    private DC5BusinessDomainJpaEntity mapToDb(DomibusConnectorBusinessDomain businessDomain, DC5BusinessDomainJpaEntity dbDomain) {
        Map<String, String> map = new HashMap<>(businessDomain.getMessageLaneProperties());
        dbDomain.setProperties(map);
        dbDomain.setDescription(businessDomain.getDescription());
        return dbDomain;
    }

    private DomibusConnectorBusinessDomain mapToDomain(DC5BusinessDomainJpaEntity DC5BusinessDomainJpaEntity) {
        DomibusConnectorBusinessDomain lane = new DomibusConnectorBusinessDomain();
        lane.setDescription(DC5BusinessDomainJpaEntity.getDescription());
        lane.setId(DC5BusinessDomainJpaEntity.getName());
        lane.setConfigurationSource(ConfigurationSource.DB);
        lane.setEnabled(true);
        Map<String, String> p = new HashMap<>(DC5BusinessDomainJpaEntity.getProperties());
        lane.setMessageLaneProperties(p);
        return lane;
    }

}
