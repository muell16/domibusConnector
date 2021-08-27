package eu.domibus.connector.security.configuration;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.dss.configuration.SignatureValidationConfigurationProperties;
import eu.domibus.connector.security.configuration.validation.CheckAllowedAdvancedElectronicSystemType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * This configuration class holds the settings for
 * <ul>
 *     <li>Validating the business document</li>
 * </ul>
 */
@Validated
@ConfigurationProperties(prefix = DCBusinessDocumentValidationConfigurationProperties.PREFIX)
@BusinessDomainScoped
@Component
@CheckAllowedAdvancedElectronicSystemType
public class DCBusinessDocumentValidationConfigurationProperties {

    public static final String PREFIX = "connector.business-document-sent";

    /**
     * The country where the connector is located
     */
    @NotBlank
    String country = "";

    /**
     * Name of the service provider which is operating the
     * connector
     */
    @NotBlank
    String serviceProvider = "";

    /**
     * The default AdvancedSystemType which should be used
     * <ul>
     *     <li>SIGNATURE_BASED</li>
     *     <li>AUTHENTICATION_BASED</li>
     * </ul>
     *
     * For SIGNATURE_BASED the signatureValidation properties must be configured
     * For AUTHENTICATION_BASED the
     *
     */
    @NotNull
    private AdvancedElectronicSystemType defaultAdvancedSystemType;

    /**
     * Provides a list of the allowed SystemTypes
     *  only a allowed system type can be 5
     */
    @NotNull
    private List<AdvancedElectronicSystemType> allowedAdvancedSystemTypes = Arrays.asList(AdvancedElectronicSystemType.values());

    /**
     *  If true the client can override the for the specific message used system type
     *   the system type must be within the list of allowedAdvancedSystemTypes
     */
    private boolean allowSystemTypeOverrideByClient = true;

    /**
     * Configuration for signature validation,
     *  used when the advancedSystemType is SIGNATURE_BASED
     */
    //TODO: condiational null check, if allowedAdvancedSystemTypes contains SIGNATURE_BASED
    @Valid
    @NestedConfigurationProperty
    private SignatureValidationConfigurationProperties signatureValidation;

    //TODO: conditional null check, if allowedAdvancedSystemTypes contains AUTHENTICATION_BASED
    @Valid
    @NestedConfigurationProperty
    private DCEcodexContainerProperties.AuthenticationValidationConfigurationProperties authenticationValidation;

    public List<AdvancedElectronicSystemType> getAllowedAdvancedSystemTypes() {
        return allowedAdvancedSystemTypes;
    }

    public void setAllowedAdvancedSystemTypes(List<AdvancedElectronicSystemType> allowedAdvancedSystemTypes) {
        this.allowedAdvancedSystemTypes = allowedAdvancedSystemTypes;
    }

    public boolean isAllowSystemTypeOverrideByClient() {
        return allowSystemTypeOverrideByClient;
    }

    public void setAllowSystemTypeOverrideByClient(boolean allowSystemTypeOverrideByClient) {
        this.allowSystemTypeOverrideByClient = allowSystemTypeOverrideByClient;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public DCEcodexContainerProperties.AuthenticationValidationConfigurationProperties getAuthenticationValidation() {
        return authenticationValidation;
    }

    public void setAuthenticationValidation(DCEcodexContainerProperties.AuthenticationValidationConfigurationProperties authenticationValidation) {
        this.authenticationValidation = authenticationValidation;
    }

    public AdvancedElectronicSystemType getDefaultAdvancedSystemType() {
        return defaultAdvancedSystemType;
    }

    public void setDefaultAdvancedSystemType(AdvancedElectronicSystemType defaultAdvancedSystemType) {
        this.defaultAdvancedSystemType = defaultAdvancedSystemType;
    }

    public SignatureValidationConfigurationProperties getSignatureValidation() {
        return signatureValidation;
    }

    public void setSignatureValidation(SignatureValidationConfigurationProperties signatureValidation) {
        this.signatureValidation = signatureValidation;
    }
}
