package eu.domibus.connector.controller.spring;

//import eu.domibus.connector.configuration.annotation.ConfigurationLabel;
import eu.domibus.connector.lib.spring.DomibusConnectorDuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConditionalOnProperty(prefix = "connector.controller.content", value = "enabled", havingValue = "true")
@Component("ContentDeletionTimeoutConfigurationProperties")
@ConfigurationProperties(prefix="connector.controller.content")
@Validated
public class ContentDeletionTimeoutConfigurationProperties {


    /**
     *
     */
    @NotNull
    private DomibusConnectorDuration checkTimeout;

    public DomibusConnectorDuration getCheckTimeout() {
        return checkTimeout;
    }

    public void setCheckTimeout(DomibusConnectorDuration checkTimeout) {
        this.checkTimeout = checkTimeout;
    }
}
