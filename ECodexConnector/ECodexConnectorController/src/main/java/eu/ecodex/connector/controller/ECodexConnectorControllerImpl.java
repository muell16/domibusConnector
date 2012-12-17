package eu.ecodex.connector.controller;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.evidences.ECodexConnectorEvidencesToolkit;
import eu.ecodex.connector.evidences.HashValueBuilder;
import eu.ecodex.connector.gwc.ECodexConnectorGatewayWebserviceClient;
import eu.ecodex.connector.mapping.ECodexConnectorContentMapper;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;

public abstract class ECodexConnectorControllerImpl implements ECodexConnectorController {

    ECodexConnectorProperties connectorProperties;
    ECodexConnectorContentMapper contentMapper;
    ECodexConnectorNationalBackendClient nationalBackendClient;
    ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient;
    ECodexConnectorEvidencesToolkit evidencesToolkit;
    HashValueBuilder hashValueBuilder;
    ECodexConnectorPersistenceService dbMessageService;

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

    protected void setHashValueBuilder(HashValueBuilder hashValueBuilder) {
        this.hashValueBuilder = hashValueBuilder;
    }

    protected void setDbMessageService(ECodexConnectorPersistenceService dbMessageService) {
        this.dbMessageService = dbMessageService;
    }

}
