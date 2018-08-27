package eu.domibus.connector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.service.DomibusConnectorServicePersistenceService;

@Service("webBackendClientService")
public class WebBackendClientService {

	@Autowired
	private BackendClientInfoPersistenceService persistenceService;
	
	@Autowired
	private DomibusConnectorServicePersistenceService servicePersistenceService;
	
	public void setPersistenceService(BackendClientInfoPersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setServicePersistenceService(DomibusConnectorServicePersistenceService servicePersistenceService) {
		this.servicePersistenceService = servicePersistenceService;
	}
	
	public List<DomibusConnectorBackendClientInfo> getAllBackendClients(){
		return persistenceService.getAllBackendClients();
	}
	
	public DomibusConnectorBackendClientInfo saveBackendClientInfo(DomibusConnectorBackendClientInfo backendClientInfo) {
		if(backendClientInfo.isDefaultBackend()) {
			//There can only be one default backend. So, if set to default, all other backendInfos have to be checked and updated if necessary.
			for(DomibusConnectorBackendClientInfo info:getAllBackendClients()) {
				if(!info.getBackendName().equals(backendClientInfo.getBackendName()) && info.isDefaultBackend()) {
					// if the BackendInfo is not the BackendInfo to be saved and marked as default already -> deactivate default
					info.setDefaultBackend(false);
					this.persistenceService.save(info);
				}
			}
		}
		return persistenceService.save(backendClientInfo);
	}
	
	public List<DomibusConnectorService> getServiceList() {
		return this.servicePersistenceService.getServiceList();
	}
	
}
