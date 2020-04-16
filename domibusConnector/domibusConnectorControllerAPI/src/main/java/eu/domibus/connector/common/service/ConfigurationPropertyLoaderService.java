package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;

/**
 * This service loads with @ConfigurationProperties annotated
 * classes and also binds the configuration
 *
 */
public interface ConfigurationPropertyLoaderService {


    <T> T loadConfiguration(DomibusConnectorMessageLane.MessageLaneId laneId, Class<T> clazz);

}
