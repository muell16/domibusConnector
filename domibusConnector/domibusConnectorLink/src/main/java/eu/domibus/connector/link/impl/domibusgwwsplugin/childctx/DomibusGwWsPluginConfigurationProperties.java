package eu.domibus.connector.link.impl.domibusgwwsplugin.childctx;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@Configuration
//@Profile(DomibusGwWsPlugin.DC_DOMIBUSGW_WS_PLUGIN_PROFILE_NAME)
//@ConfigurationProperties(prefix = "link." + DomibusGwWsPlugin.IMPL_NAME )
@ConfigurationProperties(prefix = ".")
@Data
public class DomibusGwWsPluginConfigurationProperties {

    private String gwAddress;

}
