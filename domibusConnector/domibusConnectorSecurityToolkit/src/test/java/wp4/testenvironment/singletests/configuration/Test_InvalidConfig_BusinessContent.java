package wp4.testenvironment.singletests.configuration;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wp4.testenvironment.configurations.InvalidConfig_BusinessContent;
import wp4.testenvironment.configurations.ValidConfig_BasicLegalValidator;
import wp4.testenvironment.configurations.ValidConfig_BasicTechValidator;
import wp4.testenvironment.configurations.ValidConfig_CertificateVerifier;
import wp4.testenvironment.configurations.ValidConfig_SignatureParameters;
import wp4.testenvironment.configurations.ValidConfig_TokenIssuer;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;

//SUB-CONF-14
public class Test_InvalidConfig_BusinessContent {
	
	/*
	 * Variant 1 - Empty Business Content
	 */
	@Test
    public void test_Empty_Business_Content() throws Exception {

		BusinessContent content = InvalidConfig_BusinessContent.get_EmptyContent();
		
    	final DSSECodexContainerService containerService = new DSSECodexContainerService();
    	
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfig_By_SigParamFactory());
    	containerService.setTechnicalValidationService(ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig());
    	containerService.setLegalValidationService(ValidConfig_BasicLegalValidator.get_LegalValidator());
    	containerService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());

    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullSignatureBased();
    	
    	try {
    		ECodexContainer container = containerService.create(content, issuer);
    		
    		CheckResult checkResult = containerService.check(container);
    		
    		if(checkResult.isSuccessful()) {
    			Assertions.fail("The Business Content was invalid and a valid container has been created! " +
    					"The expected result either was an invalid container or an exception at the time of container creation!");
    		}
    	} catch(eu.ecodex.dss.service.ECodexBusinessException e) {
    		Assertions.assertTrue(true);
    	} catch(eu.ecodex.dss.service.ECodexException keyEx) {
    		Assertions.assertTrue(true);
    	} catch(Exception ex) {
    		Assertions.fail("An unexpected exception has been thrown: " + ex.getMessage(), ex);
    	}
    }
	
	/*
	 * Variant 2 - Business Content with missing Business Document
	 */
	@Test
    public void test_No_Business_Document() throws Exception {

    	BusinessContent content = InvalidConfig_BusinessContent.get_MissingBusinessDocument();
    	
    	final DSSECodexContainerService containerService = new DSSECodexContainerService();
    	
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfig_By_SigParamFactory());
    	containerService.setTechnicalValidationService(ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig());
    	containerService.setLegalValidationService(ValidConfig_BasicLegalValidator.get_LegalValidator());
    	containerService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());

    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullSignatureBased();
    	
    	try {
    		ECodexContainer container = containerService.create(content, issuer);
    		
    		CheckResult checkResult = containerService.check(container);
    		
    		if(checkResult.isSuccessful()) {
    			Assertions.fail("The Business Content was invalid and a valid container has been created! " +
    					"The expected result either was an invalid container or an exception at the time of container creation!");
    		}
    	} catch(eu.ecodex.dss.service.ECodexBusinessException e) {
    		Assertions.assertTrue(true);
    	} catch(eu.ecodex.dss.service.ECodexException keyEx) {
    		Assertions.assertTrue(true);
    	} catch(Exception ex) {
    		Assertions.fail("An unexpected exception has been thrown: " + ex.getMessage(), ex);
    	}
    }
	
	/*
	 * Variant 3 - Null Business Content
	 */
	@Test
    public void test_Null_Business_Content() throws Exception {

    	BusinessContent content = InvalidConfig_BusinessContent.get_NullContent();
    	
    	final DSSECodexContainerService containerService = new DSSECodexContainerService();
    	
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfig_By_SigParamFactory());
    	containerService.setTechnicalValidationService(ValidConfig_BasicTechValidator.get_BasicTechValidator_NoProxy_NoAuthCertConfig());
    	containerService.setLegalValidationService(ValidConfig_BasicLegalValidator.get_LegalValidator());
    	containerService.setCertificateVerifier(ValidConfig_CertificateVerifier.get_WithProxy());

    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullSignatureBased();
    	
    	try {
    		ECodexContainer container = containerService.create(content, issuer);
    		
    		CheckResult checkResult = containerService.check(container);
    		
    		if(checkResult.isSuccessful()) {
    			Assertions.fail("The Business Content was invalid and a valid container has been created! " +
    					"The expected result either was an invalid container or an exception at the time of container creation!" );
    		}
    	} catch(eu.ecodex.dss.service.ECodexBusinessException e) {
    		Assertions.assertTrue(true);
    	} catch(eu.ecodex.dss.service.ECodexException keyEx) {
    		Assertions.assertTrue(true);
    	} catch(Exception ex) {
    		Assertions.fail("An unexpected exception has been thrown: " + ex.getMessage(), ex);
    	}
    }
}
