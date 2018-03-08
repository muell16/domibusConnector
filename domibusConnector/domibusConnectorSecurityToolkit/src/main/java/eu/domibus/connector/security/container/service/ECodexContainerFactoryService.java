package eu.domibus.connector.security.container.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import eu.domibus.connector.security.validation.DomibusConnectorCertificateVerifier;
import eu.domibus.connector.security.validation.DomibusConnectorTechnicalValidationServiceFactory;
import eu.ecodex.dss.model.CertificateStoreInfo;
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
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.EncryptionAlgorithm;
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
    SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

    @Autowired
    DomibusConnectorTechnicalValidationServiceFactory technicalValidationServiceFactory;

    public ECodexContainerService createECodexContainerService(DomibusConnectorMessage message) {
        DSSECodexContainerService containerService = new DSSECodexContainerService();
        containerService.setEnvironmentConfiguration(environmentConfiguration);
        containerService.setCertificateVerifier(certificateVerifier);

        ECodexLegalValidationService legalValidationService = new DSSECodexLegalValidationService();
        legalValidationService.setEnvironmentConfiguration(environmentConfiguration);
        containerService.setLegalValidationService(legalValidationService);

        containerService.setContainerSignatureParameters(createSignatureParameters());

        ECodexTechnicalValidationService eCodexTechnicalValidationService = technicalValidationServiceFactory.technicalValidationService(message);
        containerService.setTechnicalValidationService(eCodexTechnicalValidationService);

        return containerService;
    }

    private SignatureParameters createSignatureParameters() {
        try {
            LOGGER.debug("creatingSignatureParameters");
            // KlarA: Changed the functionality of this method to use the methods
            // that have been ordered by Austria
            // and realized by Arhs.

            CertificateStoreInfo certStore = new CertificateStoreInfo();

            String storeLocation = securityToolkitConfigurationProperties.getKeyStore().getPathUrlAsString();
            LOGGER.debug("resolve url [{}] to string [{}]", securityToolkitConfigurationProperties.getKeyStore().getPath(), securityToolkitConfigurationProperties.getKeyStore().getPathUrlAsString());
            certStore.setLocation(storeLocation);
            certStore.setPassword(securityToolkitConfigurationProperties.getKeyStore().getPassword());

            EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.RSA;
            DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA1;

            String keyAlias = securityToolkitConfigurationProperties.getKey().getAlias();
            String keyPassword = securityToolkitConfigurationProperties.getKey().getPassword();

            LOGGER.info("SignatureParameters are certStore [{}], keyAlias [{}], encryptionAlgorithm [{}], digestAlgorithm [{}]",
                    certStore.getLocation(), keyAlias, encryptionAlgorithm, digestAlgorithm);
            final eu.ecodex.dss.model.SignatureParameters mySignatureParameters =
                    eu.ecodex.dss.util.SignatureParametersFactory.create(certStore, keyAlias,
                            keyPassword, encryptionAlgorithm, digestAlgorithm);

            return mySignatureParameters;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
