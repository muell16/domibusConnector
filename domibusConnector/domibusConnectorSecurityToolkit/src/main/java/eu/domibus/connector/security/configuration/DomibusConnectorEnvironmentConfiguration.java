package eu.domibus.connector.security.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.EnvironmentConfiguration;

@Component("domibusConnectorEnvironmentConfiguration")
public class DomibusConnectorEnvironmentConfiguration extends EnvironmentConfiguration implements InitializingBean {

	@Value("${connector.truststore.path:null}")
	String truststoreLocation;
	@Value("${connector.truststore.password:null}")
	String truststorePassword;

	@Override
	public void afterPropertiesSet() throws Exception {
		CertificateStoreInfo trustedCertificates = new CertificateStoreInfo();
		trustedCertificates.setLocation(truststoreLocation);
		trustedCertificates.setPassword(truststorePassword);
		
		setConnectorCertificates(trustedCertificates);
	}

}
