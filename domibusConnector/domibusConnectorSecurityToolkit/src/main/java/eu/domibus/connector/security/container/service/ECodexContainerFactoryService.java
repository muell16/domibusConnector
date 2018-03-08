package eu.domibus.connector.security.container.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.validation.DomibusConnectorCertificateVerifier;
import eu.domibus.connector.security.validation.DomibusConnectorTechnicalValidationServiceFactory;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.EnvironmentConfiguration;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexLegalValidationService;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;
import eu.ecodex.dss.service.impl.dss.DSSECodexLegalValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ECodexContainerFactoryService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ECodexContainerFactoryService.class);

    @Autowired
    EnvironmentConfiguration environmentConfiguration;

    @Autowired
    DomibusConnectorCertificateVerifier certificateVerifier;

    @Autowired
    SignatureParameters signatureParameters;

    @Autowired
    DomibusConnectorTechnicalValidationServiceFactory technicalValidationServiceFactory;

    public ECodexContainerService createECodexContainerService(DomibusConnectorMessage message) {
        DSSECodexContainerService containerService = new DSSECodexContainerService();
        containerService.setEnvironmentConfiguration(environmentConfiguration);
        containerService.setCertificateVerifier(certificateVerifier);

        ECodexLegalValidationService legalValidationService = new DSSECodexLegalValidationService();
        legalValidationService.setEnvironmentConfiguration(environmentConfiguration);
        containerService.setLegalValidationService(legalValidationService);

        containerService.setContainerSignatureParameters(signatureParameters);

        ECodexTechnicalValidationService eCodexTechnicalValidationService = technicalValidationServiceFactory.technicalValidationService(message);
        containerService.setTechnicalValidationService(eCodexTechnicalValidationService);

        return containerService;
    }



}
