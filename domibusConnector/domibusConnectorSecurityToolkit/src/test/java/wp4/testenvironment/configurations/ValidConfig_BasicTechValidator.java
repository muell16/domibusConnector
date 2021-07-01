package wp4.testenvironment.configurations;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;

import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


// SUB-CONF-05
public class ValidConfig_BasicTechValidator {

	private static final Resource IGNORED_KEYSTORE_PATH = new ClassPathResource("/keystores/ignore_tom_store.jks");
	private static final String IGNORED_KEYSTORE_PASSWORD = "teststore";
	
	// No Proxy
	// SUB-CONF-05 Variant 1
	public static DSSECodexTechnicalValidationService get_BasicTechValidator_NoProxy_NoAuthCertConfig() throws IOException {
		
		DSSECodexTechnicalValidationService	techValService = new DSSECodexTechnicalValidationService();

		techValService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());
		
		return techValService;
	}
	
	// No Proxy - With Authentication Certificate Verification
	// SUB-CONF-05 Variant 2
	public static DSSECodexTechnicalValidationService get_BasicTechValidator_NoProxy_WithAuthCertConfig() throws IOException {
		
		DSSECodexTechnicalValidationService techValService = new DSSECodexTechnicalValidationService();

		techValService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());
		
		FileInputStream fis = ValidConfig_BasicTechValidator_AuthCertificateTSL.get_FileInputStream_with_TSL();
		techValService.setAuthenticationCertificateTSL(fis);
		
		try {
			techValService.initAuthenticationCertificateVerification();
		} catch (ECodexException e) {
			e.printStackTrace();
			Assertions.fail("Initialization of authentication certificate verification failed!", e);
		}
		
		IOUtils.closeQuietly(fis);
		
		return techValService;
	}

	public static ECodexTechnicalValidationService get_BasicTechValidator_WithSignatureFilter() throws IOException {
		
		DSSECodexTechnicalValidationService techValService = new DSSECodexTechnicalValidationService();
		
		techValService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());
		
		CertificateStoreInfo certStore = new CertificateStoreInfo();
		certStore.setLocation(IGNORED_KEYSTORE_PATH);
		certStore.setPassword(IGNORED_KEYSTORE_PASSWORD);
		
		techValService.setIgnoredCertificatesStore(certStore);
		
		return techValService;
	}
}
