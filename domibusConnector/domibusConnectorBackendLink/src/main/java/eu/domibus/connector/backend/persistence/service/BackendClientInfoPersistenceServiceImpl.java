
package eu.domibus.connector.backend.persistence.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
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
    
    @Nullable DomibusConnectorBackendClientInfo mapDbEntityToDomainEntity(@Nullable BackendClientInfo dbBackendInfo) {        
        if (dbBackendInfo == null) {            
            return null;
        }
        DomibusConnectorBackendClientInfo clientInfo = new DomibusConnectorBackendClientInfo();
        BeanUtils.copyProperties(dbBackendInfo, clientInfo);
        return clientInfo;
    }

}
