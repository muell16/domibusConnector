package eu.domibus.connector.link.impl.wsplugin;


import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(DCWsPluginConfiguration.DC_WS_BACKEND_PLUGIN_PROFILE_NAME)
@ConfigurationProperties(prefix = DCWsPluginConfiguration.DC_WS_BACKEND_PLUGIN_PROFILE_NAME)
public class DCWsBackendLinkConfigurationProperties extends  DCWsPluginConfigurationProperties {

    /**
     * Specifies the address where the Backend WebService should be published
     * the path specefied here is added to the path of the CXF-Servlet
     * (which is per default configured as /service - this leads to the default URL of
     * "/services/backend"
     */
    @ConfigurationLabel("Where should the cxf endpoint for the backends be exposed")
    @ConfigurationDescription("Specifies the address where the Backend WebService should be published\n" +
            "the path specefied here is added to the path of the CXF-Servlet\n" +
            "(which is per default configured as /services - this leads to the default URL of\n" +
            "'/services/backend'")
    private String backendPublishAddress = "/backend";

    public String getBackendPublishAddress() {
        return backendPublishAddress;
    }

    public void setBackendPublishAddress(String backendPublishAddress) {
        this.backendPublishAddress = backendPublishAddress;
    }

}
