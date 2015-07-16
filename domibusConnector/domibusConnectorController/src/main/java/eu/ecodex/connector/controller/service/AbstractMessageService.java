package eu.ecodex.connector.controller.service;

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.ecodex.connector.evidences.ECodexConnectorEvidencesToolkit;
import eu.ecodex.connector.evidences.HashValueBuilder;
import eu.ecodex.connector.gwc.ECodexConnectorGatewayWebserviceClient;
import eu.ecodex.connector.mapping.ECodexConnectorContentMapper;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;
import eu.ecodex.connector.security.ECodexConnectorSecurityToolkit;

public class AbstractMessageService {

    CommonConnectorProperties connectorProperties;
    ECodexConnectorContentMapper contentMapper;
    ECodexConnectorNationalBackendClient nationalBackendClient;
    ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient;
    ECodexConnectorEvidencesToolkit evidencesToolkit;
    ECodexConnectorSecurityToolkit securityToolkit;
    HashValueBuilder hashValueBuilder;
    DomibusConnectorPersistenceService persistenceService;

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setContentMapper(ECodexConnectorContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    public void setNationalBackendClient(ECodexConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }

    public void setGatewayWebserviceClient(ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient) {
        this.gatewayWebserviceClient = gatewayWebserviceClient;
    }

    public void setEvidencesToolkit(ECodexConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    public void setSecurityToolkit(ECodexConnectorSecurityToolkit securityToolkit) {
        this.securityToolkit = securityToolkit;
    }

    public void setHashValueBuilder(HashValueBuilder hashValueBuilder) {
        this.hashValueBuilder = hashValueBuilder;
    }

    public void setPersistenceService(DomibusConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
