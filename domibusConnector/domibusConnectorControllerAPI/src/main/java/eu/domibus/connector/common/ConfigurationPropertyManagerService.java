package eu.domibus.connector.common;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.springframework.lang.Nullable;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * then {@link #loadConfiguration(DC5BusinessDomain.BusinessDomainId, Class, String)} is called
     *
     */
    <T> T loadConfiguration(@Nullable DC5BusinessDomain.BusinessDomainId laneId, @NotNull Class<T> clazz);

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
    <T> T loadConfiguration(@Nullable DC5BusinessDomain.BusinessDomainId laneId, @NotNull Class<T> clazz, String prefix);


    <T> T loadConfigurationOnlyFromMap(Map<String, String> map, Class<T> clazz, String prefix);

    <T> T loadConfigurationOnlyFromMap(Map<String, String> map, Class<T> clazz);

    <T> Set<ConstraintViolation<T>> validateConfiguration(T updatedConfigClazz);

    /**
     *
     * @param laneId the laneId, if null defaultLaneId is used
     * @param configurationBean TODO: write javadoc...
     *
     *
     */
    void updateConfiguration(@Nullable DC5BusinessDomain.BusinessDomainId laneId, Object configurationBean);


    void updateConfiguration(DC5BusinessDomain.BusinessDomainId laneId, Class<?> updatedConfigClazz, Map<String, String> diffProps);

    Map<String, String> getUpdatedConfiguration(DC5BusinessDomain.BusinessDomainId laneId, List<Object> updatedConfigClazzes);
}
