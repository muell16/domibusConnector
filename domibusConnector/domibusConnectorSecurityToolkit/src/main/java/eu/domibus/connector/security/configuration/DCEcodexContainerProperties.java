package eu.domibus.connector.security.configuration;


import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.dss.configuration.SignatureValidationConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import eu.domibus.connector.security.aes.DCAuthenticationBasedTechnicalValidationServiceFactory;
import eu.domibus.connector.security.aes.OriginalSenderBasedAESAuthenticationServiceFactory;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Component
@BusinessDomainScoped
@ConfigurationProperties(prefix = DCEcodexContainerProperties.PREFIX)
public class DCEcodexContainerProperties {

    public static final String PREFIX = "connector.ecodex-container";

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private SignatureConfigurationProperties signature;

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private SignatureValidationConfigurationProperties signatureValidation;


    public SignatureValidationConfigurationProperties getSignatureValidation() {
        return signatureValidation;
    }

    public void setSignatureValidation(SignatureValidationConfigurationProperties signatureValidation) {
        this.signatureValidation = signatureValidation;
    }

    public SignatureConfigurationProperties getSignature() {
        return signature;
    }

    public void setSignature(SignatureConfigurationProperties signature) {
        this.signature = signature;
    }

    public static class AuthenticationValidationConfigurationProperties {
        /**
         * If the AUTHENTICATION_BASED is used, the identity provider must be set
         *  the identity provider is the system which has authenticated the user
         */
        @NotBlank
        private String identityProvider;

        @NotNull
        private Class<? extends DCAuthenticationBasedTechnicalValidationServiceFactory> authenticatorServiceFactoryClass = OriginalSenderBasedAESAuthenticationServiceFactory.class;

        @NotNull
        private Map<String, String> properties = new HashMap<>();

        public String getIdentityProvider() {
            return identityProvider;
        }

        public void setIdentityProvider(String identityProvider) {
            this.identityProvider = identityProvider;
        }

        public Class<? extends DCAuthenticationBasedTechnicalValidationServiceFactory> getAuthenticatorServiceFactoryClass() {
            return authenticatorServiceFactoryClass;
        }

        public void setAuthenticatorServiceFactoryClass(Class<? extends DCAuthenticationBasedTechnicalValidationServiceFactory> authenticatorServiceFactoryClass) {
            this.authenticatorServiceFactoryClass = authenticatorServiceFactoryClass;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }

    public static class SignatureConfigurationProperties extends KeyAndKeyStoreConfigurationProperties {

        @NotNull
        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.RSA;

        @NotNull
        DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;

        public EncryptionAlgorithm getEncryptionAlgorithm() {
            return encryptionAlgorithm;
        }

        public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
            this.encryptionAlgorithm = encryptionAlgorithm;
        }

        public DigestAlgorithm getDigestAlgorithm() {
            return digestAlgorithm;
        }

        public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
            this.digestAlgorithm = digestAlgorithm;
        }
    }

}
