package eu.domibus.connector.security.validation;

import eu.domibus.connector.dss.service.CommonCertificateVerifierFactory;
import eu.domibus.connector.security.container.service.TokenIssuerFactory;
import eu.domibus.connector.security.spring.DocumentValidationConfigurationProperties;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.europa.esig.dss.service.http.proxy.ProxyConfig;
import eu.europa.esig.dss.validation.executor.signature.DefaultSignatureProcessExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.aes.DomibusConnectorAESTechnicalValidationService;
import eu.domibus.connector.security.aes.DomibusConnectorAESTokenValidationCreator;
import eu.ecodex.dss.model.EnvironmentConfiguration;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;

import java.util.Optional;

@Service
public class DomibusConnectorTechnicalValidationServiceFactory {

	private static final Logger LOGGER = LogManager.getLogger(DomibusConnectorTechnicalValidationServiceFactory.class);

	private final TokenIssuerFactory tokenIssuerFactory;
	private final EnvironmentConfiguration environmentConfiguration;
	private final DomibusConnectorCertificateVerifier certificateVerifier;
	private final ProxyConfig proxyPreferenceManager;
	 //not required if SIGNATURE_BASED
    private final Optional<DomibusConnectorAESTokenValidationCreator> delegate;
	private final DocumentValidationConfigurationProperties documentValidationConfigurationProperties;
	private final CommonCertificateVerifierFactory commonCertificateVerifierFactory;

	public DomibusConnectorTechnicalValidationServiceFactory(TokenIssuerFactory tokenIssuerFactory,
															 EnvironmentConfiguration environmentConfiguration,
															 DomibusConnectorCertificateVerifier certificateVerifier,
															 CommonCertificateVerifierFactory commonCertificateVerifierFactory,
															 DocumentValidationConfigurationProperties documentValidationConfigurationProperties,
															 ProxyConfig proxyPreferenceManager,
															 Optional<DomibusConnectorAESTokenValidationCreator> delegate) {
		this.tokenIssuerFactory = tokenIssuerFactory;
		this.environmentConfiguration = environmentConfiguration;
		this.certificateVerifier = certificateVerifier;
		this.proxyPreferenceManager = proxyPreferenceManager;
		this.commonCertificateVerifierFactory = commonCertificateVerifierFactory;
		this.documentValidationConfigurationProperties = documentValidationConfigurationProperties;
		this.delegate = delegate;
	}



	public ECodexTechnicalValidationService createTechnicalBusinessDocumentValidationService(DomibusConnectorMessage message) {
		AdvancedSystemType advancedElectronicSystemType = tokenIssuerFactory.getAdvancedElectronicSystemType(message);
		switch(advancedElectronicSystemType) {
			case SIGNATURE_BASED: return getSignTechnicalValidationService();
			case AUTHENTICATION_BASED: return getAuthTechnicalAESValidationService(message);
			default: throw new IllegalArgumentException("Configuration for 'token.issuer.aes.value' not properly set!");
		}
	}


	private DomibusConnectorAESTechnicalValidationService getAuthTechnicalAESValidationService(DomibusConnectorMessage message) {
		if (!delegate.isPresent()) {
			throw new IllegalStateException("If the the token is AUTHENTICATION BASED a AES validation service must be available!");
		}
    	DomibusConnectorAESTechnicalValidationService technicalValidationService = new DomibusConnectorAESTechnicalValidationService(message, delegate.get());
    	return technicalValidationService;
    }
	
	private DSSECodexTechnicalValidationService getSignTechnicalValidationService() {
		DSSECodexTechnicalValidationService technicalValidationService = new DSSECodexTechnicalValidationService(certificateVerifier,
				new DefaultSignatureProcessExecutor(),
				Optional.empty(),
				Optional.empty());
		
//		technicalValidationService.setEnvironmentConfiguration(environmentConfiguration);
//    	technicalValidationService.setProxyPreferenceManager(proxyPreferenceManager);
//    	technicalValidationService.setCertificateVerifier(commonCertificateVerifierFactory.createCommonCertificateVerifier(documentValidationConfigurationProperties));
    	
    	return technicalValidationService;

	}


}
