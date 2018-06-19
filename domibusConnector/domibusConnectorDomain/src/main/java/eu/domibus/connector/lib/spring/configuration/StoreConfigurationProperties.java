package eu.domibus.connector.lib.spring.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


public class StoreConfigurationProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreConfigurationProperties.class);

    /**
     * Path to the Key/Truststore
     */
    @Nonnull
    private Resource path;

    /**
     * Password to open the Store
     */
    private String password;

    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    public StoreConfigurationProperties() {}

    public StoreConfigurationProperties(Resource path, String password) {
        this.path = path;
        this.password = password;
    }

    public Resource getPath() {
        return path;
    }

    public void setPath(Resource path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

     @Nullable
     @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
     public String getPathUrlAsString() {
        try {
            if (path == null) {
                LOGGER.debug("#getPathUrlAsString: resolved to null");
                return null;
            }
            LOGGER.trace("#getPathUrlAsString: get url from [{}] to [{}]", path, path.getURL().toString());
            return path.getURL().toString();
        } catch (IOException e) {
            throw new UncheckedIOException("#getPathUrlAsString: path: [" + path + "]", e);
        }
    }
}
