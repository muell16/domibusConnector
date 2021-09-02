package eu.domibus.connector.link.impl.domibusgwwsplugin.childctx;

import eu.domibus.connector.link.api.ActiveLinkPartner;
import lombok.Data;

@Data
public class DomibusGwWsPluginActiveLinkPartner extends ActiveLinkPartner {

    private DomibusGwWsPluginPartnerConfigurationProperties config;

}
