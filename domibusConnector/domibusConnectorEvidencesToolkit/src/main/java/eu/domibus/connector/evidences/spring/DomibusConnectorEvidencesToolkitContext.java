package eu.domibus.connector.evidences.spring;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.HashValueBuilder;
import eu.ecodex.evidences.ECodexEvidenceBuilder;
import eu.ecodex.evidences.EvidenceBuilder;

@Configuration
@ComponentScan(basePackageClasses={DomibusConnectorEvidencesToolkit.class})
public class DomibusConnectorEvidencesToolkitContext {

	@Value("${connector.evidences.keystore.path:null}")
	String javaKeyStorePath;
	@Value("${connector.evidences.keystore.password:null}")
	String javaKeyStorePassword;
	@Value("${connector.evidences.key.alias:null}")
	String keyAlias;
	@Value("${connector.evidences.key.password:null}")
	String keyPassword;
	
	@Value("${hash.algorithm:MD5}")
	String hashAlgorithm;

	@Bean
	public EvidenceBuilder domibusConnectorEvidenceBuilder() {
		return new ECodexEvidenceBuilder(javaKeyStorePath, javaKeyStorePassword, keyAlias, keyPassword);
	}
	
	@Bean 
	public HashValueBuilder hashValueBuilder() {
		try {
			return new HashValueBuilder(hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	

}
