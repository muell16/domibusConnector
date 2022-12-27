package eu.ecodex.dc5.payload.provider;

import eu.ecodex.dc5.payload.DomibusConnectorFilesystemPersistenceProperties;
import eu.ecodex.dc5.payload.DomibusConnectorPersistenceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = DomibusConnectorPersistenceProperties.PREFIX,
        value = "provider-" + LargeFilePersistenceServiceFilesystemImpl.PROVIDER_NAME,
        havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(DomibusConnectorFilesystemPersistenceProperties.class)
public class LargeFilePersistenceServiceFilesystemConfiguration {


    @Bean
    public LargeFilePersistenceServiceFilesystemImpl largeFilePersistenceServiceFilesystem(DomibusConnectorFilesystemPersistenceProperties properties) {
        return new LargeFilePersistenceServiceFilesystemImpl(properties);
    }


}
