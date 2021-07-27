package eu.domibus.connector.lib.spring.configuration;

import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.lib.spring.configuration.validation.CheckResourceIsReadable;
import eu.domibus.connector.lib.spring.configuration.validation.CheckStoreIsLoadable;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.security.*;
import java.security.cert.Certificate;


@Validated
@CheckStoreIsLoadable
public class StoreConfigurationProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreConfigurationProperties.class);

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Path to the Key/Truststore
     */
    @ConfigurationLabel("Path to key or truststore")
    @CheckResourceIsReadable
    private String path;

    /**
     * Password to open the Store
     */
    @NotNull
    @ConfigurationLabel("Password of the key or truststore")
    private String password;

    @ConfigurationLabel("JavaKeystoreType - default JKS")
    private String type = "JKS";

    public StoreConfigurationProperties() {
    }

//    public StoreConfigurationProperties(Resource path, String password) {
//        this.path = path;
//        this.password = password;
//    }

    public String getPath() {
        return path;
    }

//    public Resource getPathAsResource() {
//        return getPath();
//    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public boolean isWriteable() {
//        if (this.getPath().isFile()) {
//            try {
//                return this.getPath().getFile().canWrite();
//            } catch (IOException e) {
//                LOGGER.error("#isWriteable: cannot open path", this.getPath());
//            }
//        }
//        return false;
//    }

//    public String getPathUrlAsString() {
//        try {
//            if (path == null) {
//                LOGGER.debug("#getPathUrlAsString: resolved to null");
//                return null;
//            }
//            LOGGER.trace("#getPathUrlAsString: get url from [{}] to [{}]", path, path.getURL().toString());
//            return path.getURL().toString();
//        } catch (IOException e) {
//            throw new UncheckedIOException("#getPathUrlAsString: path: [" + path + "]", e);
//        }
//    }

//    public void validatePathReadable() {
//        if (getPath() == null) {
//            throw new ValidationException("Path is null!");
//        }
//        try {
//            InputStream inputStream = this.getPathAsResource().getInputStream();
//            if (inputStream == null) {
//                throw new ValidationException("Input Stream from path is null!");
//            }
//            inputStream.close();
//        } catch (IOException e) {
//            throw new ValidationException("IOException occured during open", e);
//        }
//    }



//    public KeyStore loadKeyStore() {
//        validatePathReadable();
//        if (password == null) {
//            password = "";
//        }
//        char[] pwdArray = password.toCharArray();
//        try (InputStream inputStream = getPathAsResource().getInputStream()) {
//            KeyStore keyStore = KeyStore.getInstance(this.type);
//            keyStore.load(inputStream, pwdArray);
//            return keyStore;
//        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
//            throw new CannotLoadKeyStoreException(String.format("Cannot load key store from path %s", getPath()), e);
//        }
//        return null;
//    }


}
