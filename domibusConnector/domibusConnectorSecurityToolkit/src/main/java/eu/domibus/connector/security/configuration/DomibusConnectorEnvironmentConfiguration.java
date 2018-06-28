package eu.domibus.connector.security.configuration;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.EnvironmentConfiguration;

@Component("domibusConnectorEnvironmentConfiguration")
public class DomibusConnectorEnvironmentConfiguration extends EnvironmentConfiguration implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorEnvironmentConfiguration.class);

	private SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

	@Autowired
	public void setSecurityToolkitConfigurationProperties(SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties) {
		this.securityToolkitConfigurationProperties = securityToolkitConfigurationProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		CertificateStoreInfo trustedCertificates = new CertificateStoreInfo();
        StoreConfigurationProperties trustStore = securityToolkitConfigurationProperties.getTruststore();
        if (trustStore != null && trustStore.getPath() != null) {
            trustedCertificates.setLocation(trustStore.getPath().getURL().toString());
            trustedCertificates.setPassword(trustStore.getPassword());
            setConnectorCertificates(trustedCertificates);
            LOGGER.info("Setting trusted certificates trustStore to: [{}]", trustStore.getPath().getURL().toString());
        } else {
            LOGGER.info("Not setting trusted certificates, because no trusted certificates trust store is configured!");
        }
	}

}
