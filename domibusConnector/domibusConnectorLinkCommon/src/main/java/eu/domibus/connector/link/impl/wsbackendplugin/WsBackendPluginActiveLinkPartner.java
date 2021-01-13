package eu.domibus.connector.link.impl.wsbackendplugin;

import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsBackendPluginLinkPartnerConfigurationProperties;
import lombok.Data;

@Data
public class WsBackendPluginActiveLinkPartner extends ActiveLinkPartner {

    WsBackendPluginLinkPartnerConfigurationProperties config;

}
