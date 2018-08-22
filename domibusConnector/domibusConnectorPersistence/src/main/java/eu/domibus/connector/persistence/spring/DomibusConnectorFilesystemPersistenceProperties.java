package eu.domibus.connector.persistence.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix = "connector.persistence.filesystem")
@PropertySource("classpath:eu/domibus/connector/persistence/config/default-filesystem-config.properties")
public class DomibusConnectorFilesystemPersistenceProperties {


    /**
     * Property to configure the storage location on filesystem
     *  under this folder the {@link eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl}
     *  is managing the data
     */
    private Path storagePath;

    public Path getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(Path storagePath) {
        this.storagePath = storagePath;
    }

}
