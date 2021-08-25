package eu.domibus.connector.dss.configuration;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CertificateVerifierConfigurationProperties {

    private boolean trustStoreEnabled = true;

    @Valid
    @NotNull
    @NestedConfigurationProperty
    @ConfigurationLabel("Trust Store")
    @ConfigurationDescription("This store holds all valid certificates for validation")
    private StoreConfigurationProperties trustStore = new StoreConfigurationProperties();


    @NestedConfigurationProperty
    @ConfigurationLabel("Trust Store")
    @ConfigurationDescription("This store holds all ignored certificates for validation")
    private StoreConfigurationProperties ignoreStore;

    @NotNull
    @ConfigurationLabel("Validation Constraints")
    @ConfigurationDescription("The DSS Certificate Validation Constraints config")
    private Resource certificateValidationConstraintXml = new ClassPathResource("/102853/constraint.xml");

    @ConfigurationLabel("Trusted List Sources")
    @ConfigurationDescription("The names of trusted lists sources. The sources are configured under: " + BasicDssConfigurationProperties.PREFIX + ".trust-source.*")
    private List<String> trustedListSources = new ArrayList<>();

    private boolean ocspEnabled = true;

    private boolean crlEnabled = true;

    public boolean isTrustStoreEnabled() {
        return trustStoreEnabled;
    }

    public void setTrustStoreEnabled(boolean trustStoreEnabled) {
        this.trustStoreEnabled = trustStoreEnabled;
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

    public StoreConfigurationProperties getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(StoreConfigurationProperties trustStore) {
        this.trustStore = trustStore;
    }

    public Resource getCertificateValidationConstraintXml() {
        return certificateValidationConstraintXml;
    }

    public void setCertificateValidationConstraintXml(Resource certificateValidationConstraintXml) {
        this.certificateValidationConstraintXml = certificateValidationConstraintXml;
    }

    public List<String> getTrustedListSources() {
        return trustedListSources;
    }

    public void setTrustedListSources(List<String> trustedListSources) {
        this.trustedListSources = trustedListSources;
    }

    public StoreConfigurationProperties getIgnoreStore() {
        return ignoreStore;
    }

    public void setIgnoreStore(StoreConfigurationProperties ignoreStore) {
        this.ignoreStore = ignoreStore;
    }
}
