
package eu.domibus.connector.backend.persistence.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorService;
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

    public @Nullable DomibusConnectorBackendClientInfo getBackendClientInfoByServiceName(DomibusConnectorService service);

    public DomibusConnectorBackendClientInfo save(DomibusConnectorBackendClientInfo backendClientInfo);

}
