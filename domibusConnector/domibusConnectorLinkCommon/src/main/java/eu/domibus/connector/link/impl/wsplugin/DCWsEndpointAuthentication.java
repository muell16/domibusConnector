package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendDeliveryException;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.security.Principal;

/**
 * Handles client authentication
 *  currently cert based only...
 */
public class DCWsEndpointAuthentication {

    public static final Logger LOGGER = LogManager.getLogger(DCWsEndpointAuthentication.class);

    @Autowired
    DCActiveLinkManagerService activeLink;


    public ActiveLinkPartner checkBackendClient(WebServiceContext webServiceContext) throws DomibusConnectorBackendDeliveryException {
        if (webServiceContext == null) {
            throw new RuntimeException("No webServiceContext found");
        }
        Principal userPrincipal = webServiceContext.getUserPrincipal();
        String backendName = userPrincipal == null ? null : userPrincipal.getName();
        if (userPrincipal == null || backendName == null) {
            String error = String.format("checkBackendClient: Cannot handle request because userPrincipal is [%s] the userName is [%s]. Cannot identify backend!", userPrincipal, backendName);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new DomibusConnectorBackendDeliveryException(error);
        }
        ActiveLinkPartner backendClientInfoByName = null;

        backendName = backendName.toLowerCase();
        backendClientInfoByName = activeLink.getActiveLinkPartner(new DomibusConnectorLinkPartner.LinkPartnerName(backendName)).orElse(null);

//        backendClientInfoByName = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(backendName);

        if (backendClientInfoByName == null) {

            String error = String.format("#checkBackendClient: No backend with name [%s] configured on connector!", backendName);
            //should be marked deprecated the removal of leading cn=
            backendName = backendName.toLowerCase();
            backendName = "cn=".equals(backendName.substring(0, 3)) ? backendName.replaceFirst("cn=", "") : backendName;
            //replace leading "cn=" with "" so common name cn=alice becomes alice

            LOGGER.warn("#checkBackendClient: {}, Looking for 4.0.x compatible connector backend naming [{}]", error, backendName);
            backendClientInfoByName = activeLink.getActiveLinkPartner(new DomibusConnectorLinkPartner.LinkPartnerName(backendName)).orElse(null);
        }
        if (backendClientInfoByName == null) {
            String error = String.format("#checkBackendClient: No link partner with name [%s] configured on connector!\n" +
                    "Connector takes the FQDN of the certificate and starts looking if a active link partner with this name is found\n" +
                    "Connector always converts the fqdn of the certificate to lower case letters!", backendName);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new DomibusConnectorBackendDeliveryException(error);
        }

        return backendClientInfoByName;
    }


}
