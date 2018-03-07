package eu.domibus.connector.evidences.spring;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.HashValueBuilder;
import eu.ecodex.evidences.ECodexEvidenceBuilder;
import eu.ecodex.evidences.EvidenceBuilder;

@Configuration
@ComponentScan(basePackageClasses={DomibusConnectorEvidencesToolkit.class})
@EnableConfigurationProperties
public class DomibusConnectorEvidencesToolkitContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorEvidencesToolkitContext.class);

//	@Value("${connector.evidences.keystore.path:null}")
//	String javaKeyStorePath;
//	@Value("${connector.evidences.keystore.password:null}")
//	String javaKeyStorePassword;
//	@Value("${connector.evidences.key.alias:null}")
//	String keyAlias;
//	@Value("${connector.evidences.key.password:null}")
//	String keyPassword;
//
//	@Value("${hash.algorithm:MD5}")
//	String hashAlgorithm;

	@Autowired
	EvidencesToolkitConfigurationProperties evidencesToolkitConfigurationProperties;

	@Bean
	public EvidenceBuilder domibusConnectorEvidenceBuilder() {
		String javaKeyStorePath = evidencesToolkitConfigurationProperties.getKeystore().getPathUrlAsString();
		String javaKeyStorePassword = evidencesToolkitConfigurationProperties.getKeystore().getPassword();
		String keyAlias = evidencesToolkitConfigurationProperties.getKey().getAlias();
		String keyPassword = evidencesToolkitConfigurationProperties.getKey().getPassword();
		LOGGER.debug("Createing ECodexEvidenceBuilder with keyStorePath [{}], keyStorePassword [{}], keyAlias [{}], keyPassword [password={}]",
				javaKeyStorePath, javaKeyStorePassword, keyAlias, keyPassword);
		return new ECodexEvidenceBuilder(javaKeyStorePath, javaKeyStorePassword, keyAlias, keyPassword);
	}
	
	@Bean 
	public HashValueBuilder hashValueBuilder() {
		return new HashValueBuilder(evidencesToolkitConfigurationProperties.getHashAlgorithm());
	}
	
	

}
