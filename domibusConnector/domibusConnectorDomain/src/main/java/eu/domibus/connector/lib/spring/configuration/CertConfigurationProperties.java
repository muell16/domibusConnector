package eu.domibus.connector.lib.spring.configuration;

/**
 * Configuration properties for referencing a
 *  key, cert in a key store
 *   a alias and a optional password
 */
public class CertConfigurationProperties {

    String alias;

    String password;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
