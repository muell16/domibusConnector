package eu.ecodex.dc5.domain.service;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
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
    public Optional<DC5BusinessDomain> findById(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        Optional<DC5BusinessDomainJpaEntity> byName = businessDomainDao.findByName(businessDomainId);
        return byName.map(this::mapToDomain);
    }

    @Override
    public List<DC5BusinessDomain> findAll() {
        return businessDomainDao.findAll().stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public DC5BusinessDomain update(DC5BusinessDomain DC5BusinessDomain) {
        if (DC5BusinessDomain == null) {
            throw new IllegalArgumentException("domibusConnectorBusinessDomain is not allowed to be null!");
        }
        Optional<DC5BusinessDomainJpaEntity> lane = businessDomainDao.findByName(DC5BusinessDomain.getId());
        if (lane.isPresent()) {
            DC5BusinessDomainJpaEntity dbBusinessDomain = this.mapToDb(DC5BusinessDomain, lane.get());
            DC5BusinessDomainJpaEntity save = businessDomainDao.save(dbBusinessDomain);
            return this.mapToDomain(save);
        } else {
            throw new IllegalArgumentException(String.format("No BusinessDomain configured with id [%s]", DC5BusinessDomain.getId()));
        }
    }

    @Override
    public DC5BusinessDomain create(DC5BusinessDomain businessDomain) {
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

    private DC5BusinessDomainJpaEntity mapToDb(DC5BusinessDomain businessDomain, DC5BusinessDomainJpaEntity dbDomain) {
        Map<String, String> map = new HashMap<>(businessDomain.getProperties());
        dbDomain.setProperties(map);
        dbDomain.setDescription(businessDomain.getDescription());
        dbDomain.setEnabled(businessDomain.isEnabled());
        return dbDomain;
    }

    private DC5BusinessDomain mapToDomain(DC5BusinessDomainJpaEntity jpaEntity) {
        DC5BusinessDomain lane = new DC5BusinessDomain();
        lane.setDescription(jpaEntity.getDescription());
        lane.setId(jpaEntity.getName());
        lane.setConfigurationSource(ConfigurationSource.DB);
        lane.setEnabled(jpaEntity.isEnabled());
        Map<String, String> p = new HashMap<>(jpaEntity.getProperties());
        lane.setProperties(p);
        return lane;
    }

}
