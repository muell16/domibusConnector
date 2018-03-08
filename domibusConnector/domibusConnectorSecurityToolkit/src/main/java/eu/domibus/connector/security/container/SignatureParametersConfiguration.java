package eu.domibus.connector.security.container;

import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.EncryptionAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SignatureParametersConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(SignatureParametersConfiguration.class);

    @Autowired
    SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

    @Bean
    public eu.ecodex.dss.model.SignatureParameters signatureParameters() throws Exception {
        LOGGER.debug("creatingSignatureParameters");
        // KlarA: Changed the functionality of this method to use the methods
        // that have been ordered by Austria
        // and realized by Arhs.

        CertificateStoreInfo certStore = new CertificateStoreInfo();

        String storeLocation = securityToolkitConfigurationProperties.getKeyStore().getPathUrlAsString();
        LOGGER.debug("resolve url [{}] to string [{}]", securityToolkitConfigurationProperties.getKeyStore().getPath(), securityToolkitConfigurationProperties.getKeyStore().getPathUrlAsString());
        certStore.setLocation(storeLocation);
        certStore.setPassword(securityToolkitConfigurationProperties.getKeyStore().getPassword());

        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.RSA;
        DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA1;

        String keyAlias = securityToolkitConfigurationProperties.getKey().getAlias();
        String keyPassword = securityToolkitConfigurationProperties.getKey().getPassword();

        LOGGER.info("SignatureParameters are certStore [{}], keyAlias [{}], encryptionAlgorithm [{}], digestAlgorithm [{}]",
                certStore.getLocation(), keyAlias, encryptionAlgorithm, digestAlgorithm);
        final eu.ecodex.dss.model.SignatureParameters mySignatureParameters =
                eu.ecodex.dss.util.SignatureParametersFactory.create(certStore, keyAlias,
                keyPassword, encryptionAlgorithm, digestAlgorithm);

        return mySignatureParameters;

    }


}
