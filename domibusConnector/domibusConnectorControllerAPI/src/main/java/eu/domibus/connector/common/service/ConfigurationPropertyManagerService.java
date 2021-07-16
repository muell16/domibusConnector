package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * This service loads with @ConfigurationProperties annotated
 * classes and also binds the configuration
 *
 */
public interface ConfigurationPropertyManagerService {


    /**
     *
     * determines the prefix from the clazz
     * which must be annotated with @see {@link org.springframework.boot.context.properties.ConfigurationProperties}
     *
     * then {@link #loadConfiguration(DomibusConnectorMessageLane.MessageLaneId, Class, String)} is called
     *
     */
    <T> T loadConfiguration(@Nullable DomibusConnectorMessageLane.MessageLaneId laneId, @NotNull Class<T> clazz);

    /**
     *
     * Initializes the clazz from the property source
     *  the properties are taken from the message lane, if not provided the
     *  default application environment is used @see {@link org.springframework.core.env.Environment}
     *
     *
     * @param laneId - the lane id
     * @param clazz - the clazz to init
     * @param prefix - the prefix for the properties
     * @param <T> - type of the clazz
     * @return the initialized class
     */
    <T> T loadConfiguration(@Nullable DomibusConnectorMessageLane.MessageLaneId laneId, @NotNull Class<T> clazz, String prefix);


    /**
     *
     * @param laneId the laneId, if null defaultLaneId is used
     * @param configurationClazz must be annotated with @see {@link org.springframework.boot.context.properties.ConfigurationProperties}
     *
     *
     */
    void updateConfiguration(@Nullable DomibusConnectorMessageLane.MessageLaneId laneId, Object configurationClazz);


}
