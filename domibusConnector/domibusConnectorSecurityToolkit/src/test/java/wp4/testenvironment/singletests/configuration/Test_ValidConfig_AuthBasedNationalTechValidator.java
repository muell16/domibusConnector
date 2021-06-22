package wp4.testenvironment.singletests.configuration;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeAll;
import wp4.testenvironment.configurations.ValidConfig_AuthBasedNationalTechValidator;
import wp4.testenvironment.configurations.ValidConfig_BusinessContent;
import wp4.testenvironment.configurations.ValidConfig_NationalLegalValidator;
import wp4.testenvironment.configurations.ValidConfig_SignatureParameters;
import wp4.testenvironment.configurations.ValidConfig_TokenIssuer;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;
import eu.ecodex.dss.util.DocumentStreamUtil;
import eu.ecodex.dss.util.ZipStreamUtil;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;

// SUB-CONF-3
public class Test_ValidConfig_AuthBasedNationalTechValidator {
	
	static DSSECodexContainerService containerService;
	
	/**
	 * Initializes all test cases with the same, working configuration.<br/>
	 * Test case specific configurations are done within each test case itself. 
	 */
	@BeforeAll
	static public void init() {
		containerService = new DSSECodexContainerService();
		
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfiguration());
    	containerService.setTechnicalValidationService(ValidConfig_AuthBasedNationalTechValidator.get_AuthBasedNationalTechValidator());
    	containerService.setLegalValidationService(ValidConfig_NationalLegalValidator.get_NationalLegalValidator_FullData());
	}
	    
	/**
	 * Within this test, a working configuration for an authentication-based system is 
	 * meant to be tested.<br/>
	 * The test contains:<br/>
	 * - A complete "BusinessContent" object with<br/>
	 * &nbsp;&nbsp;- A PDF Business Document<br/>
	 * &nbsp;&nbsp;- No Attachments<br/>
	 * - A complete "TokenIssuer" object<br/>
	 * <br/>
	 * The respective test is SUB-CONF-3 - Variant 1
	 */
	@Test
    public void test_PDF_without_Attachments() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_UnsignedFile_WithoutAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	ECodexContainer container = containerService.create(content, issuer);
    	
    	containerService.setCertificateVerifier(new CommonCertificateVerifier());
    	
        // The eCodex container has been created
        Assertions.assertNotNull(container);
        // The ASiC document has been created and contains data
		Assertions.assertTrue(DocumentStreamUtil.hasData(container.getAsicDocument()));
		Assertions.assertTrue(ZipStreamUtil.isZipFile(container.getAsicDocument()));
        // The PDF document has been created and contains data
		Assertions.assertTrue(DocumentStreamUtil.hasData(container.getTokenPDF()));
        // The XML document has been created and contains data
		Assertions.assertTrue(DocumentStreamUtil.hasData(container.getTokenXML()));
		Assertions.assertNotNull(container.getToken());
        //Check Contains Business Data
		Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        CheckResult checkResult = containerService.check(container);
		Assertions.assertTrue(checkResult.isSuccessful());
    }
}
