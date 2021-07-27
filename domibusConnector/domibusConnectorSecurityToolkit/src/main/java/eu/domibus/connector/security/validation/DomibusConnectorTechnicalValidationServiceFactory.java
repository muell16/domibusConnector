package eu.domibus.connector.security.validation;

import eu.domibus.connector.security.container.service.TokenIssuerFactory;
import eu.domibus.connector.security.container.service.TokenIssuerFactoryProperties;
import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.aes.DomibusConnectorAESTechnicalValidationService;
import eu.domibus.connector.security.aes.DomibusConnectorAESTokenValidationCreator;
import eu.ecodex.dss.model.EnvironmentConfiguration;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;

@Service
public class DomibusConnectorTechnicalValidationServiceFactory {

	private static final Logger LOGGER = LogManager.getLogger(DomibusConnectorTechnicalValidationServiceFactory.class);

	private final TokenIssuerFactory tokenIssuerFactory;
	private final EnvironmentConfiguration environmentConfiguration;
	private final DomibusConnectorCertificateVerifier certificateVerifier;
	private final DomibusConnectorProxyConfig proxyPreferenceManager;
	 //not required if SIGNATURE_BASED
    private final DomibusConnectorAESTokenValidationCreator delegate;

	public DomibusConnectorTechnicalValidationServiceFactory(TokenIssuerFactory tokenIssuerFactory,
															 EnvironmentConfiguration environmentConfiguration,
															 DomibusConnectorCertificateVerifier certificateVerifier,
															 DomibusConnectorProxyConfig proxyPreferenceManager,
															 @Autowired(required = false) DomibusConnectorAESTokenValidationCreator delegate) {
		this.tokenIssuerFactory = tokenIssuerFactory;
		this.environmentConfiguration = environmentConfiguration;
		this.certificateVerifier = certificateVerifier;
		this.proxyPreferenceManager = proxyPreferenceManager;
		this.delegate = delegate;
	}



	public ECodexTechnicalValidationService createTechnicalValidationService(DomibusConnectorMessage message) {
		AdvancedSystemType advancedElectronicSystemType = tokenIssuerFactory.getAdvancedElectronicSystemType(message);
		switch(advancedElectronicSystemType) {
			case SIGNATURE_BASED: return getSignTechnicalValidationService();
			case AUTHENTICATION_BASED: return getAuthTechnicalAESValidationService(message);
			default: throw new IllegalArgumentException("Configuration for 'token.issuer.aes.value' not properly set!");
		}
	}

	private DomibusConnectorAESTechnicalValidationService getAuthTechnicalAESValidationService(DomibusConnectorMessage message) {
		if (delegate == null) {
			throw new IllegalStateException("If the the token is AUTHENTICATION BASED a AES validation service must be available!");
		}
    	DomibusConnectorAESTechnicalValidationService technicalValidationService = new DomibusConnectorAESTechnicalValidationService(message, delegate);
    	return technicalValidationService;
    }
	
	private DSSECodexTechnicalValidationService getSignTechnicalValidationService() {
		DSSECodexTechnicalValidationService technicalValidationService = new DSSECodexTechnicalValidationService();
		
		technicalValidationService.setEnvironmentConfiguration(environmentConfiguration);
    	technicalValidationService.setProxyPreferenceManager(proxyPreferenceManager);
    	technicalValidationService.setCertificateVerifier(certificateVerifier);
    	
    	return technicalValidationService;
	}


}
