package eu.domibus.connector.link.impl.domibusgwwsplugin.childctx;

import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "." )
@Data
public class DomibusGwWsPluginPartnerConfigurationProperties {

    @ConfigurationLabel("PluginUsername")
    @ConfigurationDescription("Username if basic authentication is used")
    private String username;

    @ConfigurationLabel("PluginPassword")
    @ConfigurationDescription("Password if basic authentication is used")
    private String password;

//    @ConfigurationLabel("Key/Keystore")
//    @ConfigurationDescription("Used for Certificate Based authentication")
//    private KeyAndKeyStoreConfigurationProperties key;

//    @ConfigurationLabel("Pull Timeout")
//    @ConfigurationDescription("How often should be pulled for new messages")
//    private Duration pullInterval;


}
