package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.enums.LinkMode;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Profile(DCWsPluginConfiguration.DC_WS_GATEWAY_PLUGIN_PROFILE_NAME)
@ConfigurationProperties(prefix = DCWsPluginConfiguration.DC_WS_GATEWAY_PLUGIN_PROFILE_NAME)
@Validated
@Valid
public class DCWsGatewayLinkConfigurationProperties extends DCWsPluginConfigurationProperties {

    @ConfigurationLabel("Where should the cxf endpoint for the gateawy plugin be exposed")
    @ConfigurationDescription("Specifies the address where the Backend WebService should be published\n" +
            "the path specefied here is added to the path of the CXF-Servlet\n" +
            "(which is per default configured as /services - this leads to the default URL of\n" +
            "'/services/gateway'")
    private String gatewaySubmissionPublishAddress = "/gateway";

    private LinkMode mode = LinkMode.PUSH;

    public String getGatewaySubmissionPublishAddress() {
        return gatewaySubmissionPublishAddress;
    }


    public void setGatewaySubmissionPublishAddress(String gatewaySubmissionPublishAddress) {
        this.gatewaySubmissionPublishAddress = gatewaySubmissionPublishAddress;
    }

    public LinkMode getMode() {
        return mode;
    }

    public void setMode(LinkMode mode) {
        this.mode = mode;
    }

    @Override
    protected String getPrefix() {
        return DCWsPluginConfiguration.DC_WS_GATEWAY_PLUGIN_PROFILE_NAME;
    }
}
