package wp4.testenvironment.configurations;

import java.io.IOException;


import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;
import org.junit.jupiter.api.Assertions;

public class InvalidConfig_BasicTechValidator {
	
	public static DSSECodexTechnicalValidationService get_BasicTechValidator_NoProxy_WithInvalidAuthCertConfig() throws IOException {
		
		DSSECodexTechnicalValidationService techValService = new DSSECodexTechnicalValidationService();

		techValService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());
		techValService.setAuthenticationCertificateTSL(InvalidConfig_BasicTechValidator_AuthCertificateTSL.get_Invalid_Path());
		
		try {
			techValService.initAuthenticationCertificateVerification();
		} catch (ECodexException e) {
			Assertions.fail("Initialization of authentication certificate verification failed!", e);
		}
		
		return techValService;
	}
}