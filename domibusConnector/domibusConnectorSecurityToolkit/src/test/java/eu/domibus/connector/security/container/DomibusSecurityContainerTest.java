package eu.domibus.connector.security.container;

import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageContent;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.domain.model.DetachedSignature;
import eu.domibus.connector.domain.model.DetachedSignatureMimeType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import eu.domibus.connector.domain.model.builder.DetachedSignatureBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.europa.esig.dss.MimeType;
import java.io.IOException;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;


/**
 * UnitTest for  DomibusSecurityContainer
 *  the underlying ECodexContainerService is mocked!
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusSecurityContainerTest {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusSecurityContainerTest.class);
    
    DomibusSecurityContainer securityContainer;
    
    
    /**
     * creates a test message where only the MessageContent and the Document of the
     * MessageContent is initialized
     * @return the message
     */
    public DomibusConnectorMessage createTestMessage() {
        try {
            DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();
            DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();

            byte[] pdf = "pdf".getBytes();
            byte[] xml = "xml".getBytes();
            
            DetachedSignature sig = DetachedSignatureBuilder
                    .build()
                    .setName("sig1")
                    .setMimeType(DetachedSignatureMimeType.BINARY)
                    .setSignature("asupersignature".getBytes())
                    .create();
            
            DomibusConnectorMessageDocument document = DomibusConnectorMessageDocumentBuilder
                    .build()
                    .setContent(pdf)
                    .setName("Form_A")
                    .withDetachedSignature(sig)
                    .create();
            
            content.setXmlContent(xml);
            content.setDocument(document);
            
            DomibusConnectorMessage message = new DomibusConnectorMessage(details, content);        
            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
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
        DomibusConnectorMessage testMessage = createTestMessage();
        BusinessContent businessContent = securityContainer.buildBusinessContent(testMessage);        
        assertThat(businessContent).isNotNull();        
        assertThat(businessContent.getDetachedSignature()).isNotNull();    
        assertThat(businessContent.getDocument().getName()).isEqualTo("Form_A");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBuildBusinessContent_messageContentIsNull_shouldThrowIllegalArgumentException() {
        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        DomibusConnectorMessage testMessage = new DomibusConnectorMessage(details, (DomibusConnectorMessageContent) null);
        
        BusinessContent businessContent = securityContainer.buildBusinessContent(testMessage); 
    }
    
    //TODO: document is missing: specify if it should fail silently or not?
    /**
     * 
     */
    @Test(expected=RuntimeException.class)
    public void testBuildBusinessContent_noDocumentProvided_shouldThrowException() {
            DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();
            DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();

            byte[] xml = "xml".getBytes();
                        
            content.setXmlContent(xml);

            DomibusConnectorMessage testMessage = new DomibusConnectorMessage(details, content);
            
            BusinessContent businessContent = securityContainer.buildBusinessContent(testMessage);      
            fail("not enough specification");
    }
    
    
    @Test
    public void testCreateContainer_withMessageAttachments() {
        DomibusConnectorMessage testMessage = createTestMessage();
        
        DomibusConnectorMessageAttachment attach1 = new DomibusConnectorMessageAttachment("attach1".getBytes(), "attach1");
        attach1.setMimeType("application/text");
        attach1.setName("test");
        testMessage.addAttachment(attach1);
        
        BusinessContent businessContent = securityContainer.buildBusinessContent(testMessage);                 
     
        assertThat(businessContent.getAttachments()).hasSize(1);        
    }
    
    /**
     * This test is a reminder to discuss MimeType conversion between domainModel and dssLib
     * 
     * Tests the conversion of DomibusConnectorMessageAttachment mimeType to 
     * dss.MimeType
     * 
     * there is some more specifcation needed
     * 
     */
    @Test
    public void testCreateContainer_withMessageAttachmentsMimeType() {
        DomibusConnectorMessage testMessage = createTestMessage();
        
        DomibusConnectorMessageAttachment attach1 = new DomibusConnectorMessageAttachment("attach1".getBytes(), "attach1");
        attach1.setMimeType("application/pdf");
        attach1.setName("test");
        testMessage.addAttachment(attach1);
        
        BusinessContent businessContent = securityContainer.buildBusinessContent(testMessage);                 
     
        assertThat(businessContent.getAttachments()).hasSize(1);
        assertThat(businessContent.getAttachments().get(0).getMimeType()).isEqualTo(MimeType.PDF);
    }
    
    
//    @Test
//    public void testCreateContainer() {
//    }
//
//    @Test
//    public void testRecieveContainerContents() {
//    }
    
}
