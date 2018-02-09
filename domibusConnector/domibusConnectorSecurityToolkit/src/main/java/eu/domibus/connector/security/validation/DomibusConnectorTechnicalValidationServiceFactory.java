package eu.domibus.connector.security.validation;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.aes.DomibusConnectorAESTechnicalValidationService;
import eu.domibus.connector.security.aes.DomibusConnectorAESTokenValidationCreator;
import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.ecodex.dss.model.EnvironmentConfiguration;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;

@Service
public class DomibusConnectorTechnicalValidationServiceFactory {
	
	@Value("${token.issuer.aes.value:#{null}}")
    AdvancedSystemType advancedElectronicSystem;
	
	@Resource(name="domibusConnectorEnvironmentConfiguration")
	EnvironmentConfiguration environmentConfiguration;
	
	@Resource(name="domibusConnectorCertificateVerifier")
	DomibusConnectorCertificateVerifier certificateVerifier;
	
	@Resource(name="domibusConnectorProxyConfig")
	DomibusConnectorProxyConfig proxyPreferenceManager;
	
	@Resource(name="domibusConnectorAESTokenValidationCreator")
    private DomibusConnectorAESTokenValidationCreator delegate;

	public ECodexTechnicalValidationService technicalValidationService(DomibusConnectorMessage message) {
		switch(advancedElectronicSystem) {
		case SIGNATURE_BASED: return getSignTechnicalValidationService();
		case AUTHENTICATION_BASED: return getAuthTechnicalAESValidationService(message);
		default: throw new IllegalArgumentException("Configuration for 'token.issuer.aes.value' not properly set!");
		}
	}
	
	private DomibusConnectorAESTechnicalValidationService getAuthTechnicalAESValidationService(DomibusConnectorMessage message) {
    	DomibusConnectorAESTechnicalValidationService technicalValidationService = new DomibusConnectorAESTechnicalValidationService(message, delegate, proxyPreferenceManager);
    	technicalValidationService.setEnvironmentConfiguration(new EnvironmentConfiguration());
    	
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
