package eu.domibus.connector.security.configuration;


import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.dss.configuration.SignatureValidationConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

}