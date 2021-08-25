package eu.domibus.connector.security.spring;


import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.dss.configuration.BasicDssConfigurationProperties;
import eu.domibus.connector.dss.configuration.CertificateVerifierConfigurationProperties;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the configuration for the signature based document validation
 *
 *
 */
@BusinessDomainScoped
@Component
@Valid
@ConfigurationProperties(prefix = DocumentValidationConfigurationProperties.CONFIG_PREFIX)
public class DocumentValidationConfigurationProperties extends CertificateVerifierConfigurationProperties {

    public static final String CONFIG_PREFIX = "connector.document-validation.signature-based";

}
