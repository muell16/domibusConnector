package eu.domibus.connector.controller.spring;

import eu.domibus.connector.lib.spring.DomibusConnectorDuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component("ContentDeletionTimeoutConfigurationProperties")
@ConfigurationProperties(prefix="connector.controller.content")
@Validated
public class ContentDeletionTimeoutConfigurationProperties {


    /**
     *
     */
    @NotNull
//    @ConfigurationLabel("Configures the fixed interval for checking if any message content can be deleted")
    private DomibusConnectorDuration checkTimeout;

    public DomibusConnectorDuration getCheckTimeout() {
        return checkTimeout;
    }

    public void setCheckTimeout(DomibusConnectorDuration checkTimeout) {
        this.checkTimeout = checkTimeout;
    }
}
