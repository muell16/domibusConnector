package eu.domibus.connector.security.spring;

import eu.domibus.connector.lib.spring.configuration.CertConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "connector.security")
public class SecurityToolkitConfigurationProperties {


    StoreConfigurationProperties store;

    CertConfigurationProperties key;

    public StoreConfigurationProperties getStore() {
        return store;
    }

    public void setStore(StoreConfigurationProperties store) {
        this.store = store;
    }

    public CertConfigurationProperties getKey() {
        return key;
    }

    public void setKey(CertConfigurationProperties key) {
        this.key = key;
    }
    
}
