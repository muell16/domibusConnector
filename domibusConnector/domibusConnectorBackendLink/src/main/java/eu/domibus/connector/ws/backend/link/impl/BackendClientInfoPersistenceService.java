
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import javax.annotation.Nullable;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface BackendClientInfoPersistenceService {

    /**
     * Returns null if no backend with this name has been found
     * @param backendName the backend name
     * @return the backendInfo
     */
    public @Nullable DomibusConnectorBackendClientInfo getBackendClientInfoByName(String backendName);

}
