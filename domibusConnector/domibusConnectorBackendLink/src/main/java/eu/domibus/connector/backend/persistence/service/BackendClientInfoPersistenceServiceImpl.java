
package eu.domibus.connector.backend.persistence.service;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorService;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
@Transactional
public class BackendClientInfoPersistenceServiceImpl implements BackendClientInfoPersistenceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BackendClientInfoPersistenceServiceImpl.class); 
    
    @Autowired
    BackendClientDao backendClientDao;

    public void setBackendClientDao(BackendClientDao backendClientDao) {
        this.backendClientDao = backendClientDao;
    }


    @Override
    public @Nullable DomibusConnectorBackendClientInfo getBackendClientInfoByName(String backendName) {
        if (backendName == null) {
            throw new IllegalArgumentException("backendName is not allowed to be null!");
        }
        BackendClientInfo dbBackendInfo = backendClientDao.findOneBackendByBackendName(backendName);
        DomibusConnectorBackendClientInfo clientInfo = mapDbEntityToDomainEntity(dbBackendInfo);
        LOGGER.debug("#getBackendClientInfoByName: returning backendClientInfo: [{}]", clientInfo);
        return clientInfo;
    }

    @Override
    public @Nullable DomibusConnectorBackendClientInfo getEnabledBackendClientInfoByName(String backendName) {
        if (backendName == null) {
            throw new IllegalArgumentException("backendName is not allowed to be null!");
        }
        BackendClientInfo dbBackendInfo = backendClientDao.findOneBackendByBackendNameAndEnabledIsTrue(backendName);
        DomibusConnectorBackendClientInfo clientInfo = mapDbEntityToDomainEntity(dbBackendInfo);
        LOGGER.debug("#getEnabledBackendClientInfoByName: returning backendClientInfo: [{}]", clientInfo);
        return clientInfo;
    }

    @Nullable
    @Override
    public DomibusConnectorBackendClientInfo getEnabledBackendClientInfoByService(DomibusConnectorService service) {
        if (service == null) {
            return null;
        }
        List<BackendClientInfo>  backendInfos = backendClientDao.findByServices_serviceAndEnabledIsTrue(service.getService());
        if (backendInfos.size() == 0) {
            LOGGER.debug("#getEnabledBackendClientInfoByService: Found no backend to handle service [{}]", service.getService());
            return null;
        } else if (backendInfos.size() == 1){
            BackendClientInfo dbBackendInfo = backendInfos.get(0);
            return mapDbEntityToDomainEntity(dbBackendInfo);
        } else {
            throw new IllegalStateException(String.format("#getEnabledBackendClientInfoByService: Found more than one Backend wich can handle service [%s]", service.getService()));
        }
    }

    @Override
    public DomibusConnectorBackendClientInfo save(@Nonnull DomibusConnectorBackendClientInfo backendClientInfo) {
        if (backendClientInfo == null) {
            throw new IllegalArgumentException("backendClientInfo is not allowed to be null!");
        }
        BackendClientInfo dbBackendClientInfo = this.mapDomainEntityToDbEntity(backendClientInfo);

        BackendClientInfo oldDbBackendClientInfo = this.backendClientDao.findOneByBackendName(backendClientInfo.getBackendName());
        if (oldDbBackendClientInfo != null) { //update
            BeanUtils.copyProperties(dbBackendClientInfo, oldDbBackendClientInfo, "services", "id"); //ignore services and id on update, TODO: implement service mapping!
            dbBackendClientInfo = this.backendClientDao.save(oldDbBackendClientInfo);
        } else { //create
            dbBackendClientInfo = this.backendClientDao.save(dbBackendClientInfo);
        }
        return this.mapDbEntityToDomainEntity(dbBackendClientInfo);
    }

    @Nonnull
    @Override
    public DomibusConnectorBackendClientInfo getDefaultBackendClientInfo() {
        BackendClientInfo defaultBackend = backendClientDao.findOneByDefaultBackendIsTrue();
        return mapDbEntityToDomainEntity(defaultBackend);
    }

    @Nullable
    DomibusConnectorBackendClientInfo mapDbEntityToDomainEntity(@Nullable BackendClientInfo dbBackendInfo) {
        if (dbBackendInfo == null) {            
            return null;
        }
        DomibusConnectorBackendClientInfo clientInfo = new DomibusConnectorBackendClientInfo();
        BeanUtils.copyProperties(dbBackendInfo, clientInfo);
        return clientInfo;
    }

    @Nullable
    BackendClientInfo mapDomainEntityToDbEntity(@Nullable DomibusConnectorBackendClientInfo clientInfo) {
        if (clientInfo == null) {
            return null;
        }
        BackendClientInfo dbClientInfo = new BackendClientInfo();
        BeanUtils.copyProperties(clientInfo, dbClientInfo);
        return dbClientInfo;
    }

}
