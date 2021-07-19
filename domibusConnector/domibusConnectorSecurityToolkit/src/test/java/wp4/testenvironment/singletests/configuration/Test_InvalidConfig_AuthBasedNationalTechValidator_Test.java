package wp4.testenvironment.singletests.configuration;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wp4.testenvironment.configurations.InvalidConfig_AuthBasedNationalTechValidator;
import wp4.testenvironment.configurations.ValidConfig_BusinessContent;
import wp4.testenvironment.configurations.ValidConfig_NationalLegalValidator;
import wp4.testenvironment.configurations.ValidConfig_SignatureParameters;
import wp4.testenvironment.configurations.ValidConfig_TokenIssuer;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;

// SUB-CONF-6
public class Test_InvalidConfig_AuthBasedNationalTechValidator_Test {
	
	/**
	 * The respective test is SUB-CONF-6 - Variant 1 No Result
	 */
    @Test
    public void test_NoResult() throws Exception {
    	
    	final DSSECodexContainerService containerService = new DSSECodexContainerService();
    	
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfiguration());
    	containerService.setTechnicalValidationService(InvalidConfig_AuthBasedNationalTechValidator.get_AuthBasedNationalTechValidator_NullResult());
    	containerService.setLegalValidationService(ValidConfig_NationalLegalValidator.get_NationalLegalValidator_NoDisclaimer());

    	BusinessContent content = ValidConfig_BusinessContent.get_UnsignedFile_WithoutAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	containerService.setCertificateVerifier(new CommonCertificateVerifier());
    	
    	try {
    		ECodexContainer container = containerService.create(content, issuer);
    		
    		CheckResult checkResult = containerService.check(container);
    		
    		if(checkResult.isSuccessful()) {
    			Assertions.fail("The technical validator was invalid and a valid container has been created! " +
    					"The expected result either was an invalid container or an exception at the time of container creation!");
    		}
    	} catch(eu.ecodex.dss.service.ECodexBusinessException e) {
    		Assertions.assertTrue(true);
    	} catch(ECodexException el) {
    		Assertions.assertTrue(true);
    	} catch(Exception ex) {
    		Assertions.fail("An unexpected exception has been thrown: " + ex.getMessage(), ex);
    	}
    }
    
	/**
	 * The respective test is SUB-CONF-6 - Variant 2 Empty Result
	 */
    @Test
    public void test_EmptyResult() throws Exception {
    		
    	final DSSECodexContainerService containerService = new DSSECodexContainerService();
    	
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfiguration());
    	containerService.setTechnicalValidationService(InvalidConfig_AuthBasedNationalTechValidator.get_AuthBasedNationalTechValidator_EmptyResult());
    	containerService.setLegalValidationService(ValidConfig_NationalLegalValidator.get_NationalLegalValidator_NoDisclaimer());

    	BusinessContent content = ValidConfig_BusinessContent.get_UnsignedFile_WithoutAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	containerService.setCertificateVerifier(new CommonCertificateVerifier());
    	
    	try {
    		ECodexContainer container = containerService.create(content, issuer);
    		
    		CheckResult checkResult = containerService.check(container);
    		
    		if(checkResult.isSuccessful()) {
    			Assertions.fail("The technical validator was invalid and a valid container has been created! " +
    					"The expected result either was an invalid container or an exception at the time of container creation!");
    		}
    	} catch(eu.ecodex.dss.service.ECodexBusinessException e) {
    		Assertions.assertTrue(true);
    	} catch(ECodexException el) {
    		Assertions.assertTrue(true);
    	} catch(Exception ex) {
    		Assertions.fail("An unexpected exception has been thrown: " + ex.getMessage(), ex);
    	}
    }
    
	/**
	 * The respective test is SUB-CONF-6 - Variant 3 Invalid Result
	 */
    @Test
    public void test_InvalidResult() throws Exception {
    	
    	final DSSECodexContainerService containerService = new DSSECodexContainerService();
    	
    	containerService.setContainerSignatureParameters(ValidConfig_SignatureParameters.getJKSConfiguration());
    	containerService.setTechnicalValidationService(InvalidConfig_AuthBasedNationalTechValidator.get_AuthBasedNationalTechValidator_InvalidResult());
    	containerService.setLegalValidationService(ValidConfig_NationalLegalValidator.get_NationalLegalValidator_NoDisclaimer());

    	BusinessContent content = ValidConfig_BusinessContent.get_UnsignedFile_WithoutAttachments();
    	TokenIssuer issuer = ValidConfig_TokenIssuer.get_FullAuthenticationBased();
    	
    	containerService.setCertificateVerifier(new CommonCertificateVerifier());
    	
    	try {
    		ECodexContainer container = containerService.create(content, issuer);
    		
    		CheckResult checkResult = containerService.check(container);
    		
    		if(checkResult.isSuccessful()) {
    			Assertions.fail("The technical validator was invalid and a valid container has been created! " +
    					"The expected result either was an invalid container or an exception at the time of container creation!");
    		}
    	} catch(eu.ecodex.dss.service.ECodexBusinessException e) {
    		Assertions.assertTrue(true);
    	} catch(ECodexException el) {
    		Assertions.assertTrue(true);
    	} catch(Exception ex) {
    		Assertions.fail("An unexpected exception has been thrown: " + ex.getMessage(), ex);
    	}
    }
}
