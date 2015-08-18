package eu.domibus.connector.controller.service;

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.HashValueBuilder;
import eu.domibus.connector.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.mapping.DomibusConnectorContentMapper;
import eu.domibus.connector.nbc.DomibusConnectorNationalBackendClient;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;

public class AbstractMessageService {

    CommonConnectorProperties connectorProperties;
    DomibusConnectorContentMapper contentMapper;
    DomibusConnectorNationalBackendClient nationalBackendClient;
    DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient;
    DomibusConnectorEvidencesToolkit evidencesToolkit;
    DomibusConnectorSecurityToolkit securityToolkit;
    HashValueBuilder hashValueBuilder;
    DomibusConnectorPersistenceService persistenceService;

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setContentMapper(DomibusConnectorContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    public void setNationalBackendClient(DomibusConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }

    public void setGatewayWebserviceClient(DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient) {
        this.gatewayWebserviceClient = gatewayWebserviceClient;
    }

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
