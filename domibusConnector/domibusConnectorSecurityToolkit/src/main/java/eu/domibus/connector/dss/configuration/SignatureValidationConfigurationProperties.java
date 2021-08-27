package eu.domibus.connector.dss.configuration;

import eu.domibus.connector.dss.configuration.validation.ValidEtsiValidationPolicyXml;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;

public class SignatureValidationConfigurationProperties extends CertificateVerifierConfigurationProperties {

    @NotNull
    @ConfigurationLabel("Validation Constraints")
    @ConfigurationDescription("The DSS Certificate Validation Constraints config")
    private Resource validationConstraintsXml = new ClassPathResource("/NotExistant/102853/container_constraint.xml");

    @NotNull
    @ValidEtsiValidationPolicyXml
    public Resource getValidationConstraintsXml() {
        return validationConstraintsXml;
    }

    public void setValidationConstraintsXml(Resource validationConstraintsXml) {
        this.validationConstraintsXml = validationConstraintsXml;
    }

}
