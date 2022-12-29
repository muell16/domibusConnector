package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DC5BusinessDomain;

import java.util.List;
import java.util.Optional;

public interface DCBusinessDomainPersistenceService {

    Optional<DC5BusinessDomain> findById(DC5BusinessDomain.BusinessDomainId businessDomainId);

    List<DC5BusinessDomain> findAll();

    DC5BusinessDomain update(DC5BusinessDomain DC5BusinessDomain);

    DC5BusinessDomain create(DC5BusinessDomain businessDomain);
}
