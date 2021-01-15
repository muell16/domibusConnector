package eu.domibus.connector.link.impl.wsbackendplugin.childctx;


import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.impl.wsbackendplugin.WsBackendPluginActiveLinkPartner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WsActiveLinkPartnerManager {

    private static final Logger LOGGER = LogManager.getLogger(WsActiveLinkPartnerManager.class);

    private Map<String, WsBackendPluginActiveLinkPartner> certificateDnToLinkPartnerMap = new HashMap<>();


    public synchronized void registerDn(WsBackendPluginActiveLinkPartner wsBackendPluginActiveLinkPartner) {
        String certificateDn = wsBackendPluginActiveLinkPartner.getConfig().getCertificateDn();
        certificateDn = certificateDn.toLowerCase();
        certificateDnToLinkPartnerMap.put(certificateDn, wsBackendPluginActiveLinkPartner);
        LOGGER.info("Registered certificate DN [{}] for BackendClient [{}]", certificateDn, wsBackendPluginActiveLinkPartner);
    }

    public synchronized void deregister(WsBackendPluginActiveLinkPartner wsBackendPluginActiveLinkPartner) {
        String certificateDn = wsBackendPluginActiveLinkPartner.getConfig().getCertificateDn();
        certificateDn = certificateDn.toLowerCase();
        certificateDnToLinkPartnerMap.remove(certificateDn);
        LOGGER.info("Removed certificate DN [{}] for BackendClient [{}]", certificateDn, wsBackendPluginActiveLinkPartner);
    }

    public Optional<WsBackendPluginActiveLinkPartner> getDomibusConnectorLinkPartnerByDn(String certificateDn) {
        certificateDn = certificateDn.toLowerCase();
        WsBackendPluginActiveLinkPartner wsBackendPluginActiveLinkPartner = certificateDnToLinkPartnerMap.get(certificateDn);
        return Optional.ofNullable(wsBackendPluginActiveLinkPartner);
    }

}
