package eu.domibus.connector.security.container;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageContent;
import eu.domibus.connector.common.message.MessageDetails;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.service.ECodexContainerService;
import java.io.IOException;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;


/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusSecurityContainerTest {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusSecurityContainerTest.class);
    
    DomibusSecurityContainer securityContainer;
    
    @Before
    public void setUp() throws Exception {
        securityContainer = new DomibusSecurityContainer();
        securityContainer.javaKeyStorePath = "file:src/test/resources/keys/connector-keystore.jks";
        securityContainer.javaKeyStorePassword = "connector";
        securityContainer.keyAlias = "domibusConnector";
        securityContainer.keyPassword = "connector";
        securityContainer.country = "AT";
        securityContainer.serviceProvider = "BRZ";
        securityContainer.advancedElectronicSystem = AdvancedSystemType.SIGNATURE_BASED;
        
        ECodexContainerService eCodexContainerService = Mockito.mock(ECodexContainerService.class);
        
        securityContainer.containerService = eCodexContainerService;
        
    
        
    }
    
    /**
     * tests if initializes correct with the
     * set properties
     * @throws Exception 
     */
    @Test
    public void testAfterPropertiesSet() throws Exception {
        LOGGER.info("testCreateSignatureParameters: javaKeyStorePath is [{}]", securityContainer.javaKeyStorePath);
        securityContainer.afterPropertiesSet();        
    }
    
    /**
     * tests if an exception is thrown
     * if the key store cannot be opened
     * because of a wrong password
     * 
     * @throws Exception 
     */
    @Test(expected=IOException.class)
    public void testAfterPropertiesSet_withWrongKeyPassword_shouldThrowException() throws Exception {
        LOGGER.info("testAfterPropertiesSet_withWrongKeyPassword");
        securityContainer.javaKeyStorePassword = "wrong!";
        securityContainer.afterPropertiesSet();
    }
    
        /**
     * tests if an exception is thrown
     * if the key store cannot be read
     * @throws Exception 
     */
    @Test(expected=IOException.class)
    public void testAfterPropertiesSet_withWrongKeyStorePath_shouldThrowException() throws Exception {
        LOGGER.info("testAfterPropertiesSet_withWrongKeyStorePath_shouldThrowException");
        securityContainer.javaKeyStorePath = "wrong!";
        securityContainer.afterPropertiesSet();
    }
    

    /**
     * tests CreateSignatureParameters
     * @throws Exception 
     */
    @Test
    public void testCreateSignatureParameters() throws Exception {
        LOGGER.info("testCreateSignatureParameters: javaKeyStorePath is [{}]", securityContainer.javaKeyStorePath);
        SignatureParameters createSignatureParameters = securityContainer.createSignatureParameters();        
        assertThat(createSignatureParameters).isNotNull();        
    }

    
    @Test
    public void testBuildBusinessContent() {
        

        
        
    }
    
    
//    @Test
//    public void testCreateContainer() {
//    }
//
//    @Test
//    public void testRecieveContainerContents() {
//    }
    
}
