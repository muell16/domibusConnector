package eu.domibus.connector.link.impl.gwwebserviceplugin;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = GwWsPlugin.DC_DOMIBUSGW_WS_PLUGIN_PROFILE_NAME )
public class GwWsPluginPartnerConfigurationProperties {

    @ConfigurationLabel("BackendService URL")
    @ConfigurationDescription("URL of the Backend Service of the Gateway, URL is usually https://server:port/services/backend ")
    @NotBlank
    private String backendServiceUrl;

    @ConfigurationLabel("PluginUsername")
    @ConfigurationDescription("Username if basic authentication is used")
    private String username;

    @ConfigurationLabel("PluginPassword")
    @ConfigurationDescription("Password if basic authentication is used")
    private String password;

    @ConfigurationLabel("Key/Keystore")
    @ConfigurationDescription("Used for Certificate Based authentication")
    private KeyAndKeyStoreConfigurationProperties key;

    @ConfigurationLabel("Pull Timeout")
    @ConfigurationDescription("How often should be pulled for new messages")
    private Duration pullInterval;

    public String getBackendServiceUrl() {
        return backendServiceUrl;
    }

    public void setBackendServiceUrl(String backendServiceUrl) {
        this.backendServiceUrl = backendServiceUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public KeyAndKeyStoreConfigurationProperties getKey() {
        return key;
    }

    public void setKey(KeyAndKeyStoreConfigurationProperties key) {
        this.key = key;
    }

    public Duration getPullInterval() {
        return pullInterval;
    }

    public void setPullInterval(Duration pullInterval) {
        this.pullInterval = pullInterval;
    }
}
