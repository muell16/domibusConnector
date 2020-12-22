package eu.domibus.connector.link.impl.gwwebserviceplugin;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(GwWsPlugin.DC_DOMIBUSGW_WS_PLUGIN_PROFILE_NAME)
@ConfigurationProperties(prefix = "link." + GwWsPlugin.IMPL_NAME )
public class GwWsPluginConfigurationProperties {
    

}
