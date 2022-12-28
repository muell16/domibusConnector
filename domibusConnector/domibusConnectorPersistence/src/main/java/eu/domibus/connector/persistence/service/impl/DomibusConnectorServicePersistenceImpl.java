package eu.domibus.connector.persistence.service.impl;

import eu.ecodex.dc5.message.model.DC5Service;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import eu.domibus.connector.persistence.service.DomibusConnectorServicePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
@Deprecated
public class DomibusConnectorServicePersistenceImpl implements DomibusConnectorServicePersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorServicePersistenceImpl.class);

    DomibusConnectorServiceDao serviceDao;

    @Autowired
    public void setServiceDao(DomibusConnectorServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @Override
    public DC5Service persistNewService(DC5Service newService) {
        PDomibusConnectorService dbService = ServiceMapper.mapServiceToPersistence(newService);
        dbService = this.serviceDao.save(dbService);
        return ServiceMapper.mapServiceToDomain(dbService);
    }

    @Override
    public List<DC5Service> getServiceList() {
        List<DC5Service> services = new ArrayList<>();
        for (PDomibusConnectorService dbService : this.serviceDao.findAll()) {
            DC5Service srv = ServiceMapper.mapServiceToDomain(dbService);
            services.add(srv);
        }
        return services;
    }
    
    @Override
    public List<String> getServiceListString() {
        List<String> services = new ArrayList<>();
        for (PDomibusConnectorService dbService : this.serviceDao.findAll()) {
            services.add(dbService.getService());
        }
        return services;
    }

    @Override
    public DC5Service updateService(DC5Service oldService, DC5Service newService) {
        PDomibusConnectorService dbService = ServiceMapper.mapServiceToPersistence(newService);
        dbService = this.serviceDao.save(dbService);
        return ServiceMapper.mapServiceToDomain(dbService);
    }

    @Override
    public void deleteService(DC5Service service) {
        PDomibusConnectorService dbService = ServiceMapper.mapServiceToPersistence(service);
        this.serviceDao.delete(dbService);
    }

    @Override
    public DC5Service getService(String service) {
//        PDomibusConnectorService srv = serviceDao.findById(service).get();
//        return ServiceMapper.mapServiceToDomain(srv);
        return null;
    }
}
