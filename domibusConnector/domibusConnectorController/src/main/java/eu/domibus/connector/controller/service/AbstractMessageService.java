package eu.domibus.connector.controller.service;

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.HashValueBuilder;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractMessageService {
//
    @Deprecated
    CommonConnectorProperties connectorProperties;
//    DomibusConnectorContentMapper contentMapper;
//    DomibusConnectorRemoteNationalBackendService nationalBackendClient;
//    DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient;
  
    @Autowired
    protected DomibusConnectorEvidencesToolkit evidencesToolkit;
    
    @Autowired
    protected DomibusConnectorSecurityToolkit securityToolkit;
    
    @Autowired
    protected HashValueBuilder hashValueBuilder;
    
    @Autowired
    protected DomibusConnectorPersistenceService persistenceService;
    
    @Autowired
    protected DomibusConnectorBackendSubmissionService sendMessageToBackendService;
    
    @Autowired
    protected DomibusConnectorGatewaySubmissionService sendMessageToGwService;

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

//    public void setContentMapper(DomibusConnectorContentMapper contentMapper) {
//        this.contentMapper = contentMapper;
//    }
//
//    public void setNationalBackendClient(DomibusConnectorRemoteNationalBackendService nationalBackendClient) {
//        this.nationalBackendClient = nationalBackendClient;
//    }

//    public void setGatewayWebserviceClient(DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient) {
//        this.gatewayWebserviceClient = gatewayWebserviceClient;
//    }

    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    public void setSecurityToolkit(DomibusConnectorSecurityToolkit securityToolkit) {
        this.securityToolkit = securityToolkit;
    }

    public void setHashValueBuilder(HashValueBuilder hashValueBuilder) {
        this.hashValueBuilder = hashValueBuilder;
    }

    public void setPersistenceService(DomibusConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
