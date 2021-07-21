package eu.domibus.connector.evidences.spring;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.HashValueBuilder;
import eu.ecodex.evidences.ECodexEvidenceBuilder;
import eu.ecodex.evidences.EvidenceBuilder;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan(basePackageClasses = {DomibusConnectorEvidencesToolkit.class})
@EnableConfigurationProperties
public class DomibusConnectorEvidencesToolkitContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorEvidencesToolkitContext.class);

    @Autowired
    private EvidencesToolkitConfigurationProperties evidencesToolkitConfigurationProperties;

    @Bean
    @BusinessDomainScoped
    public EvidenceBuilder domibusConnectorEvidenceBuilder() {
        Resource javaKeyStorePath = evidencesToolkitConfigurationProperties.getKeyStore().getPathAsResource();
        String javaKeyStorePassword = evidencesToolkitConfigurationProperties.getKeyStore().getPassword();
        String keyAlias = evidencesToolkitConfigurationProperties.getPrivateKey().getAlias();
        String keyPassword = evidencesToolkitConfigurationProperties.getPrivateKey().getPassword();
        LOGGER.debug("Creating ECodexEvidenceBuilder with keyStorePath [{}], keyStorePassword [{}], keyAlias [{}], keyPassword [password={}]",
                javaKeyStorePath, javaKeyStorePassword, keyAlias, keyPassword);
        return new ECodexEvidenceBuilder(javaKeyStorePath, javaKeyStorePassword, keyAlias, keyPassword);
    }

    @Bean
    @BusinessDomainScoped
    public HashValueBuilder hashValueBuilder() {
        return new HashValueBuilder(evidencesToolkitConfigurationProperties.getHashAlgorithm());
    }


}
