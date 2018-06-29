package eu.domibus.connector.lib.spring.configuration;

import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

@Validated
public class StoreConfigurationProperties {

    private final static Logger LOGGER = LoggerFactory.getLogger(StoreConfigurationProperties.class);

    /**
     * Path to the Key/Truststore
     */
    @NotNull
    Resource path;

    /**
     * Password to open the Store
     */
    String password = "";

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

    public @Nullable
    String getPathUrlAsString() {
        try {
            if (path == null) {
                LOGGER.debug("#getPathUrlAsString: resolved to null");
                return null;
            }
            LOGGER.trace("#getPathUrlAsString: get url from [{}] to [{}]", path, path.getURL().toString());
            return path.getURL().toString();
        } catch (IOException e) {
            throw new RuntimeException("#getPathUrlAsString: path: [" + path + "]", e);
        }
    }

    public void validatePathReadable() {
        if (getPath() == null) {
            throw new ValidationException("Path is null!");
        }
        try {
            InputStream inputStream = this.getPath().getInputStream();
            if (inputStream == null) {
                throw new ValidationException("Input Stream from path is null!");
            }
            inputStream.close();
        } catch (IOException e) {
            throw new ValidationException("IOException occured during open", e);
        }
    }

    public static class ValidationException extends  RuntimeException {
        public ValidationException() {
        }

        public ValidationException(String message) {
            super(message);
        }

        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }

        public ValidationException(Throwable cause) {
            super(cause);
        }

        public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}
