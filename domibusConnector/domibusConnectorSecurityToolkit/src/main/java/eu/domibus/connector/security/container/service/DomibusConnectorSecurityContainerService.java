package eu.domibus.connector.security.container.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.domibus.connector.security.validation.DomibusConnectorCertificateVerifier;
import eu.ecodex.dss.model.EnvironmentConfiguration;
import eu.ecodex.dss.service.ECodexLegalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;
import eu.ecodex.dss.service.impl.dss.DSSECodexLegalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;

@Component("domibusConnectorContainerService")
public class DomibusConnectorSecurityContainerService extends DSSECodexContainerService implements InitializingBean {
	
	@Resource(name="domibusConnectorEnvironmentConfiguration")
	EnvironmentConfiguration environmentConfiguration;
	
	@Resource(name="domibusConnectorCertificateVerifier")
	DomibusConnectorCertificateVerifier certificateVerifier;
	
	@Resource(name="domibusConnectorProxyConfig")
	DomibusConnectorProxyConfig proxyPreferenceManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		setEnvironmentConfiguration(environmentConfiguration);
		
		setCertificateVerifier(certificateVerifier);
		
		ECodexLegalValidationService legalValidationService = new DSSECodexLegalValidationService();
		legalValidationService.setEnvironmentConfiguration(environmentConfiguration);
		setLegalValidationService(legalValidationService);
		
		DSSECodexTechnicalValidationService technicalValidationService = new DSSECodexTechnicalValidationService();
		technicalValidationService.setProxyPreferenceManager(proxyPreferenceManager);
		technicalValidationService.setEnvironmentConfiguration(environmentConfiguration);
		technicalValidationService.setCertificateVerifier(certificateVerifier);
		setTechnicalValidationService(technicalValidationService);
		
	}

}
