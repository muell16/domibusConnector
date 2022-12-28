package eu.ecodex.dc5.domain.service;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.repo.DC5BusinessDomainJpaEntity;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import eu.ecodex.dc5.domain.repo.DomibusConnectorBusinessDomainDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DCBusinessDomainPersistenceServiceImpl implements DCBusinessDomainPersistenceService {

    private final DomibusConnectorBusinessDomainDao businessDomainDao;

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
        Map<String, String> map = new HashMap<>(businessDomain.getProperties());
        dbDomain.setProperties(map);
        dbDomain.setDescription(businessDomain.getDescription());
        dbDomain.setEnabled(businessDomain.isEnabled());
        return dbDomain;
    }

    private DomibusConnectorBusinessDomain mapToDomain(DC5BusinessDomainJpaEntity jpaEntity) {
        DomibusConnectorBusinessDomain lane = new DomibusConnectorBusinessDomain();
        lane.setDescription(jpaEntity.getDescription());
        lane.setId(jpaEntity.getName());
        lane.setConfigurationSource(ConfigurationSource.DB);
        lane.setEnabled(jpaEntity.isEnabled());
        Map<String, String> p = new HashMap<>(jpaEntity.getProperties());
        lane.setProperties(p);
        return lane;
    }

}
