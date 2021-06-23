package wp4.testenvironment.singletests;

import java.io.IOException;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import util.ContainerToFilesystem;
import wp4.testenvironment.configurations.ValidConfig_BasicTechValidator;
import wp4.testenvironment.configurations.ValidConfig_BusinessContent;
import wp4.testenvironment.configurations.ValidConfig_CertificateVerifier;
import wp4.testenvironment.configurations.ValidConfig_NationalLegalValidator;
import wp4.testenvironment.configurations.ValidConfig_SignatureParameters;
import wp4.testenvironment.configurations.ValidConfig_TokenIssuer;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.LegalTrustLevel;
import eu.ecodex.dss.model.token.TechnicalTrustLevel;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;
import eu.ecodex.dss.util.DocumentStreamUtil;
import eu.ecodex.dss.util.ZipStreamUtil;

@Disabled("TODO: repair test")
public class Test_SND_SMIX_2_Test {
	static DSSECodexContainerService containerService;
	
	/**
	 * Initializes all test cases with the same, working configuration.
	 * Test case specific configurations are done within each test case itself. 
	 * @throws IOException 
	 */
	@BeforeAll
	public static void init() throws IOException
	{
		containerService = new DSSECodexContainerService();
		
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfiguration());
    	containerService.setTechnicalValidationService(ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig());
    	containerService.setLegalValidationService(ValidConfig_NationalLegalValidator.get_NationalLegalValidator_FullData());
    	containerService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());
	}
	
	/**
	 * Within this test, a working configuration for the combined implementations 
	 * of a signature-based system (National Implementation of Tech. Validator and Basic Legal Validator) is tested
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A signed PDF Business Document
	 * - A complete "TokenIssuer" object
	 * 
	 * The respective test is SND_SMIX_2 - Variant 1 - PDF Business Document - No Attachments
	 */
    @Test
    public void test_WithSig_NoAttachment() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithoutAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullSignatureBased();
    	
    	ECodexContainer container = containerService.create(content, issuer);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        
        Assertions.assertEquals(LegalTrustLevel.SUCCESSFUL, container.getToken().getLegalValidationResult().getTrustLevel());
        Assertions.assertEquals(TechnicalTrustLevel.SUCCESSFUL, container.getToken().getTechnicalValidationResult().getTrustLevel());
        Assertions.assertEquals(AdvancedSystemType.SIGNATURE_BASED, container.getToken().getAdvancedElectronicSystem());
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        ContainerToFilesystem.writeFiles("results/SND-SMIX-2_V1", container);
    }
}
