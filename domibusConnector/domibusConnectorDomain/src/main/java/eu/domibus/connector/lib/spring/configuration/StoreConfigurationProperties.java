package eu.domibus.connector.lib.spring.configuration;

import org.springframework.core.io.Resource;

/**
 * Basic configuration for a key or truststore
 *  a location (path)
 *  a optional password
 */
public class StoreConfigurationProperties {

    Resource path;

    String password;

    public Resource getPath() {
        return path;
    }

    public void setPath(Resource path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
