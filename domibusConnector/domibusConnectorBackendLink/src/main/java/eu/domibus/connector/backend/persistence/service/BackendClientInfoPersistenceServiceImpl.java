
package eu.domibus.connector.backend.persistence.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
public class BackendClientInfoPersistenceServiceImpl implements BackendClientInfoPersistenceService {

    @Autowired
    BackendClientDao backendClientDao;

    public void setBackendClientDao(BackendClientDao backendClientDao) {
        this.backendClientDao = backendClientDao;
    }
    
    @Override
    public DomibusConnectorBackendClientInfo getBackendClientInfoByName(String backendName) {
        BackendClientInfo dbBackendInfo = backendClientDao.findOneBackendByBackendName(backendName);
        return mapDbEntityToDomainEntity(dbBackendInfo);
    }
    
    DomibusConnectorBackendClientInfo mapDbEntityToDomainEntity(BackendClientInfo dbBackendInfo) {
        DomibusConnectorBackendClientInfo clientInfo = new DomibusConnectorBackendClientInfo();
        BeanUtils.copyProperties(dbBackendInfo, clientInfo);
        return clientInfo;
    }

}
