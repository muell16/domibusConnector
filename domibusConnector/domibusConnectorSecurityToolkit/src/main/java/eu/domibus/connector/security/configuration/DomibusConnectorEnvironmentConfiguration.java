package eu.domibus.connector.security.configuration;

import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.EnvironmentConfiguration;

@Component("domibusConnectorEnvironmentConfiguration")
public class DomibusConnectorEnvironmentConfiguration extends EnvironmentConfiguration implements InitializingBean {

	@Autowired
	SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

//	@Value("${connector.truststore.path:null}")
//	String truststoreLocation;
//	@Value("${connector.truststore.password:null}")
//	String truststorePassword;

	@Override
	public void afterPropertiesSet() throws Exception {
		CertificateStoreInfo trustedCertificates = new CertificateStoreInfo();
		trustedCertificates.setLocation(securityToolkitConfigurationProperties.getTrustStore().getPath().getURL().toString());
		trustedCertificates.setPassword(securityToolkitConfigurationProperties.getTrustStore().getPassword());
		
		setConnectorCertificates(trustedCertificates);
	}

}
