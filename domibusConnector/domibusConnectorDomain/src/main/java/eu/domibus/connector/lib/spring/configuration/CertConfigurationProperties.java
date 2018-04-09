package eu.domibus.connector.lib.spring.configuration;

import javax.annotation.Nullable;

/**
 * Configuration properties for referencing a
 *  key, cert in a key store
 *   a alias and a optional password
 */
public class CertConfigurationProperties {

    /**
     * The alias of the Certificate/Key
     */
    String alias;

    /**
     * The password of the Certificate/Key
     */
    @Nullable
    String password;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public @Nullable String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
