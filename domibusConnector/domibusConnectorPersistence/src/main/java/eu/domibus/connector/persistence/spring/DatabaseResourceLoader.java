package eu.domibus.connector.persistence.spring;

import eu.domibus.connector.persistence.dao.DomibusConnectorKeystoreDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorKeystore;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class DatabaseResourceLoader implements ResourceLoader {

    public static final String DB_URL_PREFIX = "db:";

    private final ApplicationContext applicationContext;
    private final ResourceLoader delegate;

    public DatabaseResourceLoader(
            ApplicationContext applicationContext,
            ResourceLoader delegate) {
        this.applicationContext = applicationContext;
        this.delegate = delegate;
    }

    @Override
    public Resource getResource(String location) {
        if (location.startsWith(DB_URL_PREFIX)) {
            DomibusConnectorKeystoreDao databaseResourceDao =
                    this.applicationContext.getBean(DomibusConnectorKeystoreDao.class);
            String resourceName = location.substring(DB_URL_PREFIX.length());
            Optional<PDomibusConnectorKeystore> byUuid = databaseResourceDao.findByUuid(resourceName);
            if (byUuid.isPresent()) {
                return new ByteArrayResource(byUuid.get().getKeystore());
            }
        }
        return this.delegate.getResource(location);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.delegate.getClassLoader();
    }

}
