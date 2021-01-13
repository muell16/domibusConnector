package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import lombok.Data;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.CheckForNull;

@Data
public class ActiveLinkPartner {

    private DomibusConnectorLinkPartner linkPartner;

    private ActiveLink parentLink;

    @CheckForNull
    private ConfigurableApplicationContext childContext;

}
