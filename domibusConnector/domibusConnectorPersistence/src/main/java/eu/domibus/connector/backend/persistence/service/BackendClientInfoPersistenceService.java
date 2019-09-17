
package eu.domibus.connector.backend.persistence.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorService;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface BackendClientInfoPersistenceService {

    @Nullable
    DomibusConnectorBackendClientInfo getBackendClientInfoByName(String backendName);

    /**
     * Returns null if no backend with this name has been found
     * @param backendName the backend name
     * @return the backendInfo
     */
    public @Nullable DomibusConnectorBackendClientInfo getEnabledBackendClientInfoByName(String backendName);

    /**
     *
     * @param service - the service
     * @return - the backend which is configured to handle this service
     */
    public @Nullable DomibusConnectorBackendClientInfo getEnabledBackendClientInfoByService(DomibusConnectorService service);

    /**
     * save a backendClientInfo
     * @param backendClientInfo - the backendClient to save
     * @return - the saved backendClient info, by persistence layer generated fields are set, the returned object
     * can be a different object as the provided one!
     */
    public DomibusConnectorBackendClientInfo save(DomibusConnectorBackendClientInfo backendClientInfo);

    /**
     * Throws an exception if there is no default backend configured!
     * @return the default backend
     */
    public @Nullable DomibusConnectorBackendClientInfo getDefaultBackendClientInfo();
    
    public List<DomibusConnectorBackendClientInfo> getAllBackendClients();
}
