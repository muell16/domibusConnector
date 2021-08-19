package eu.domibus.connector.security.spring;


import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.dss.configuration.TrustListSourceConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Holds the configuration for the signature based document validation
 *
 *
 */
@BusinessDomainScoped
@Component
@Valid
@ConfigurationProperties(prefix = DocumentValidationConfigurationProperties.CONFIG_PREFIX)
public class DocumentValidationConfigurationProperties {

    public static final String CONFIG_PREFIX = "connector.document-validation.signature-based";

    @Valid
    @NotNull
    @NestedConfigurationProperty
    @ConfigurationLabel("Trust Store")
    @ConfigurationDescription("This store holds all valid certificates for document validation")
    private StoreConfigurationProperties trustStore = new StoreConfigurationProperties();

    @NotNull
    @ConfigurationLabel("Validation Constraints")
    @ConfigurationDescription("The DSS Certificate Validation Constraints config")
    private Resource certificateValidationConstraintXml = new ClassPathResource("/102853/constraint.xml");

    private TrustListSourceConfigurationProperties trustedLists = new TrustListSourceConfigurationProperties();

    private boolean ocspEnabled = true;

    private boolean crlEnabled = true;

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

    public TrustListSourceConfigurationProperties getTrustedLists() {
        return trustedLists;
    }

    public void setTrustedLists(TrustListSourceConfigurationProperties trustedLists) {
        this.trustedLists = trustedLists;
    }
}
