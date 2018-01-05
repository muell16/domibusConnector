package eu.domibus.connector.security.container;

import eu.domibus.connector.domain.model.DetachedSignature;
import eu.domibus.connector.domain.model.DetachedSignatureMimeType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import eu.domibus.connector.domain.model.builder.DetachedSignatureBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.Token;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.MimeType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * UnitTest for  DomibusSecurityContainer
 *  the underlying ECodexContainerService is mocked!
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusSecurityContainerTest {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusSecurityContainerTest.class);
    
    DomibusSecurityContainer securityContainer;
    
    //mock
    private ECodexContainerService eCodexContainerService;
    
    
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
        
        eCodexContainerService = Mockito.mock(ECodexContainerService.class);
        
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
        attach1.setMimeType("application/pdf"); //is not correctly converted to pdf?
        attach1.setName("test");
        testMessage.addAttachment(attach1);
        
        BusinessContent businessContent = securityContainer.buildBusinessContent(testMessage);                 
     
        assertThat(businessContent.getAttachments()).hasSize(1);
        assertThat(businessContent.getAttachments().get(0).getMimeType()).isEqualTo(MimeType.PDF);
    }
    
    
    @Test
    public void testCreateContainer() throws ECodexException {
        DomibusConnectorMessage testMessage = createTestMessage();
        
        ECodexContainer mockedECodexContainer = Mockito.mock(ECodexContainer.class);
        Mockito.when(eCodexContainerService.create(any(null), any(null))).thenReturn(mockedECodexContainer);
        
        CheckResult results = Mockito.mock(CheckResult.class);
        Mockito.when(results.isSuccessful()).thenReturn(true);
        
        Mockito.when(eCodexContainerService.check(any(ECodexContainer.class))).thenReturn(results);
        
        DSSDocument asicAttachmentDoc = Mockito.mock(DSSDocument.class);
        Mockito.when(asicAttachmentDoc.getName()).thenReturn("documentName");
        Mockito.when(asicAttachmentDoc.getMimeType()).thenReturn(MimeType.BINARY);
        Mockito.when(asicAttachmentDoc.openStream()).thenReturn(new ByteArrayInputStream("input".getBytes()));
        
        DSSDocument asicXmlTokenDocu = Mockito.mock(DSSDocument.class);
        Mockito.when(asicXmlTokenDocu.getName()).thenReturn("someTokenName");
        Mockito.when(asicXmlTokenDocu.getMimeType()).thenReturn(MimeType.XML);
        Mockito.when(asicXmlTokenDocu.openStream()).thenReturn(new ByteArrayInputStream("<xmldoc></xmldoc>".getBytes()));
                
        Mockito.when(mockedECodexContainer.getAsicDocument()).thenReturn(asicAttachmentDoc); //todo return useful asicDoc
        Mockito.when(mockedECodexContainer.getTokenXML()).thenReturn(asicXmlTokenDocu);
        
        securityContainer.createContainer(testMessage);
         
        assertThat(testMessage.getMessageAttachments()).hasSize(2); //2 attachments asicContainer, xmlToken
        
        //test xml token
        DomibusConnectorMessageAttachment xmlToken = testMessage.getMessageAttachments()
                .stream().filter((a) -> DomibusSecurityContainer.TOKEN_XML_IDENTIFIER.equals(a.getIdentifier())).findFirst().get();
        assertThat(xmlToken.getAttachment()).isEqualTo("<xmldoc></xmldoc>".getBytes());
        
        //test container
        DomibusConnectorMessageAttachment asicsContainer = testMessage.getMessageAttachments()
                .stream().filter((a) -> DomibusSecurityContainer.ASICS_CONTAINER_IDENTIFIER.equals(a.getIdentifier())).findFirst().get();
        assertThat(asicsContainer.getAttachment()).isEqualTo("input".getBytes());
        
    }
    
    @Test(expected=DomibusConnectorSecurityException.class)
    public void testCreateContainer_withUnsuccessfullContainer() throws ECodexException {
        DomibusConnectorMessage testMessage = createTestMessage();
        
        ECodexContainer mockedECodexContainer = Mockito.mock(ECodexContainer.class);
        Mockito.when(eCodexContainerService.create(any(null), any(null))).thenReturn(mockedECodexContainer);
        
        CheckResult results = Mockito.mock(CheckResult.class);
        Mockito.when(results.isSuccessful()).thenReturn(false);
        Mockito.when(results.getProblems()).thenReturn(new ArrayList<>());              
        Mockito.when(eCodexContainerService.check(any(ECodexContainer.class))).thenReturn(results);
        
        securityContainer.createContainer(testMessage);
    }

    @Ignore //not finished yet
    @Test
    public void testRecieveContainerContents() throws ECodexException {
        
        DomibusConnectorMessage testMessage = createTestMessage();
        
        DomibusConnectorMessageAttachment asicAttach = new DomibusConnectorMessageAttachment("asiccontent".getBytes(), DomibusSecurityContainer.ASICS_CONTAINER_IDENTIFIER);
        DomibusConnectorMessageAttachment xmlTokenAttach = new DomibusConnectorMessageAttachment("<xmlContent></xmlContent>".getBytes(), DomibusSecurityContainer.TOKEN_XML_IDENTIFIER);
        
        testMessage.addAttachment(asicAttach);
        testMessage.addAttachment(xmlTokenAttach);
        
        ECodexContainer mockedECodexContainer = Mockito.mock(ECodexContainer.class);
        Mockito.when(eCodexContainerService.receive(any(InputStream.class), any(InputStream.class))).thenReturn(mockedECodexContainer);
        
        CheckResult result = Mockito.mock(CheckResult.class);
        Mockito.when(result.isSuccessful()).thenReturn(true);
        Mockito.when(eCodexContainerService.check(any(ECodexContainer.class))).thenReturn(result);
        
        //return converted business doc
        DSSDocument businessDocument = Mockito.mock(DSSDocument.class);
        Mockito.when(businessDocument.getName()).thenReturn("documentName");
        Mockito.when(businessDocument.getMimeType()).thenReturn(MimeType.PDF);
        Mockito.when(businessDocument.openStream()).thenReturn(new ByteArrayInputStream("superbusinessdoc".getBytes()));
        
        Mockito.when(mockedECodexContainer.getBusinessDocument()).thenReturn(businessDocument);
        
        Token mockedTocken = Mockito.mock(Token.class);        
        Mockito.when(mockedECodexContainer.getToken()).thenReturn(mockedTocken);
       
        //TODO: mock other content:
        //TODO: business Attachments , or own test?
        
        //TODO: businessContent, or own test?
                
        //TODO: tokenPDF, or own test?
                
        //TODO: tokenXML, or own test?
        
        
        securityContainer.recieveContainerContents(testMessage);
        
        assertThat(testMessage.getMessageContent().getDocument().getDocument()).isEqualTo("superbusinessdoc".getBytes());
        
        
        
    }
    
}
