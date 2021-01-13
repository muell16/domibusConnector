package eu.domibus.connector.link.api;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import lombok.Data;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

@Data
public class ActiveLink {

    private LinkPlugin linkPlugin;

    private DomibusConnectorLinkConfiguration linkConfiguration;

    @CheckForNull
    private ConfigurableApplicationContext childContext;

    private SubmitToLink submitToLink;

}
