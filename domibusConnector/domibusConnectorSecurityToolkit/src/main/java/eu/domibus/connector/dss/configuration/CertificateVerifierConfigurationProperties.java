package eu.domibus.connector.dss.configuration;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CertificateVerifierConfigurationProperties {

    @NotBlank
    private String certificateVerifierName = "default";

    private boolean trustStoreEnabled = true;

    @Valid
    @NotNull
    @NestedConfigurationProperty
    @ConfigurationLabel("Trust Store")
    @ConfigurationDescription("This store holds all valid certificates for validation")
    private StoreConfigurationProperties trustStore = new StoreConfigurationProperties();


    /**
     * Any certificate within this store
     * would not be considered for certificate validation
     */
    @NestedConfigurationProperty
    @ConfigurationLabel("Trust Store")
    @ConfigurationDescription("This store holds all ignored certificates for validation")
    private StoreConfigurationProperties ignoreStore;

    @NotNull
    @ConfigurationLabel("Validation Constraints")
    @ConfigurationDescription("The DSS Certificate Validation Constraints config")
    private Resource signatureValidationConstraintsXml = new ClassPathResource("/102853/constraint.xml");

    /**
     * The trust source which should be used
     * the sources are configured under: {@link BasicDssConfigurationProperties}
     */
    @ConfigurationLabel("Trusted List Source")
    @ConfigurationDescription("The names of trusted list source. The sources are configured under: " + BasicDssConfigurationProperties.PREFIX + ".trust-source.*")
    private String trustedListSource;

    /**
     * should ocsp be queried
     */
    private boolean ocspEnabled = true;

    /**
     * should a crl be queried
     */
    private boolean crlEnabled = true;

    public String getCertificateVerifierName() {
        return certificateVerifierName;
    }

    public void setCertificateVerifierName(String certificateVerifierName) {
        this.certificateVerifierName = certificateVerifierName;
    }

    public boolean isTrustStoreEnabled() {
        return trustStoreEnabled;
    }

    public void setTrustStoreEnabled(boolean trustStoreEnabled) {
        this.trustStoreEnabled = trustStoreEnabled;
    }

    public StoreConfigurationProperties getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(StoreConfigurationProperties trustStore) {
        this.trustStore = trustStore;
    }

    public StoreConfigurationProperties getIgnoreStore() {
        return ignoreStore;
    }

    public void setIgnoreStore(StoreConfigurationProperties ignoreStore) {
        this.ignoreStore = ignoreStore;
    }

    public Resource getSignatureValidationConstraintsXml() {
        return signatureValidationConstraintsXml;
    }

    public void setSignatureValidationConstraintsXml(Resource signatureValidationConstraintsXml) {
        this.signatureValidationConstraintsXml = signatureValidationConstraintsXml;
    }

    public String getTrustedListSource() {
        return trustedListSource;
    }

    public void setTrustedListSource(String trustedListSource) {
        this.trustedListSource = trustedListSource;
    }

    public boolean isOcspEnabled() {
        return ocspEnabled;
    }

    public void setOcspEnabled(boolean ocspEnabled) {
        this.ocspEnabled = ocspEnabled;
    }

    public boolean isCrlEnabled() {
        return crlEnabled;
    }

    public void setCrlEnabled(boolean crlEnabled) {
        this.crlEnabled = crlEnabled;
    }
}
