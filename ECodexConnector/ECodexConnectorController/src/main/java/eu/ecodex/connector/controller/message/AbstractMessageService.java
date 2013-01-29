package eu.ecodex.connector.controller.message;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.evidences.ECodexConnectorEvidencesToolkit;
import eu.ecodex.connector.evidences.HashValueBuilder;
import eu.ecodex.connector.gwc.ECodexConnectorGatewayWebserviceClient;
import eu.ecodex.connector.mapping.ECodexConnectorContentMapper;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;
import eu.ecodex.connector.security.ECodexConnectorSecurityToolkit;

public class AbstractMessageService {

    ECodexConnectorProperties connectorProperties;
    ECodexConnectorContentMapper contentMapper;
    ECodexConnectorNationalBackendClient nationalBackendClient;
    ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient;
    ECodexConnectorEvidencesToolkit evidencesToolkit;
    ECodexConnectorSecurityToolkit securityToolkit;
    HashValueBuilder hashValueBuilder;
    ECodexConnectorPersistenceService persistenceService;

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
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

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
