package wp4.testenvironment.singletests.configuration;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import wp4.testenvironment.configurations.InvalidConfig_BasicTechValidator_AuthCertificateTSL;
import wp4.testenvironment.configurations.ValidConfig_BasicLegalValidator;
import wp4.testenvironment.configurations.ValidConfig_SignatureParameters;
import wp4.testenvironment.configurations.ValidConfig_TokenIssuer;
import wp4.testenvironment.configurations.ValidConfig_BasicTechValidator;
import wp4.testenvironment.configurations.ValidConfig_BasicTechValidator_AuthCertificateTSL;
import wp4.testenvironment.configurations.ValidConfig_BusinessContent;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.LegalTrustLevel;
import eu.ecodex.dss.model.token.TechnicalTrustLevel;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;
import eu.ecodex.dss.util.DocumentStreamUtil;
import eu.ecodex.dss.util.ZipStreamUtil;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;

// SUB-CONF-21
@Disabled("TODO: repair test") //TODO
public class Test_InvalidConfig_BasicTechValidator_AuthCertificateTSL_Test {
	static DSSECodexContainerService containerService;
	
	/**
	 * Initializes all test cases with the same, working configuration.
	 * Test case specific configurations are done within each test case itself. 
	 */
	@BeforeAll
	static public void init() {
		containerService = new DSSECodexContainerService();
		
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfiguration());
    	containerService.setLegalValidationService(ValidConfig_BasicLegalValidator.get_LegalValidator());
	}
    
	/**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - No TSL has been configured.
	 * - TSL has not been initialized.
	 */
    @Test
    public void test_Missing_Initialization() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
    
	/**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - No TSL has been configured.
	 * - TSL has been initialized.
	 */
    @Test
    public void test_Missing_TSL() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();
    	
    	techValService.initAuthenticationCertificateVerification();
    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
    
    /**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - A random XML has been configured as TSL
	 * - TSL has been initialized.
	 */
    @Test
    public void test_Random_XML_for_TSL() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();
    	
    	techValService.setAuthenticationCertificateTSL(InvalidConfig_BasicTechValidator_AuthCertificateTSL.get_RandomXML());
    	techValService.initAuthenticationCertificateVerification();
    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
    
    /**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - A random file has been configured as TSL
	 * - TSL has been initialized.
	 */
    @Test
    public void test_Random_file_for_TSL() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();
    	
    	techValService.setAuthenticationCertificateTSL(InvalidConfig_BasicTechValidator_AuthCertificateTSL.get_RandomFile());
    	techValService.initAuthenticationCertificateVerification();
    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
    
    /**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - The file meant to be the TSL does not exist
	 * - TSL has been initialized.
	 */
    @Test
    public void test_Invalid_path_to_TSL() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();
    	
    	techValService.setAuthenticationCertificateTSL(InvalidConfig_BasicTechValidator_AuthCertificateTSL.get_Invalid_Path());
    	techValService.initAuthenticationCertificateVerification();
    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
    
    /**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - A LOTL pointing at a random XML has been configured as TSL
	 * - LOTL has been marked as LOTL.
	 * - TSL has been initialized.
	 */
    @Test
    public void test_Random_XML_as_LOTL() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();
    	
    	techValService.setAuthenticationCertificateTSL(InvalidConfig_BasicTechValidator_AuthCertificateTSL.get_LOTL_with_RandomXML());
    	techValService.isAuthenticationCertificateLOTL(true);
    	techValService.initAuthenticationCertificateVerification();
    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
    
    /**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - A LOTL pointing at a random file has been configured as TSL
	 * - LOTL has been marked as LOTL.
	 * - TSL has been initialized.
	 */
    @Test
    public void test_Random_file_as_LOTL() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();
    	
    	techValService.setAuthenticationCertificateTSL(InvalidConfig_BasicTechValidator_AuthCertificateTSL.get_LOTL_with_RandomFile());
    	techValService.isAuthenticationCertificateLOTL(true);
    	techValService.initAuthenticationCertificateVerification();
    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
    
    /**
	 * Within this test, a invalid configuration of the authentication certificate TSL is simulated.
	 * The test contains:
	 * - A complete "BusinessContent" object with
	 * &nbsp;&nbsp;- A PDF Business Document
	 * &nbsp;&nbsp;- A single Attachment
	 * &nbsp;&nbsp;- A complete "TokenIssuer" object
	 * - A valid LOTL has been configured 
	 * - LOTL has not been marked as LOTL.
	 * - TSL has been initialized.
	 */
    @Test
    public void test_LOTL_is_marked_as_TSL() throws Exception {

    	BusinessContent content = ValidConfig_BusinessContent.get_SignedFile_WithAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	DSSECodexTechnicalValidationService techValService = ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig();
    	
    	techValService.setAuthenticationCertificateTSL(ValidConfig_BasicTechValidator_AuthCertificateTSL.get_ByteArray_with_LOTL());
    	techValService.initAuthenticationCertificateVerification();
    	
    	containerService.setTechnicalValidationService(techValService);
    	
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
        // Check Contains Business Data
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessDocument()));
        // The Attachments are in place
        Assertions.assertNotNull(container.getBusinessAttachments());
        Assertions.assertTrue(DocumentStreamUtil.hasData(container.getBusinessAttachments().get(0)));
        
        CheckResult checkResult = containerService.check(container);
        
        Assertions.assertTrue(checkResult.isSuccessful());
        
        // Result of the token must be NOT_SUCCESSFUL as the certificate has to be present within the TSL (and no TSL has been defined)
        Assertions.assertTrue(container.getToken().getLegalValidationResultTrustLevel().equals(LegalTrustLevel.NOT_SUCCESSFUL));
        
        // Result of the technical validation of the token must be SUCCESSFUL the usual check against the European TSL still takes place
        Assertions.assertTrue(container.getToken().getTechnicalValidationResultTrustLevel().equals(TechnicalTrustLevel.SUCCESSFUL));
    }
}
