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
import java.util.List;
import org.apache.commons.io.IOUtils;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
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
                    .createBuilder()
                    .setName("sig1")
                    .setMimeType(DetachedSignatureMimeType.BINARY)
                    .setSignature("asupersignature".getBytes())
                    .build();
            
            DomibusConnectorMessageDocument document = DomibusConnectorMessageDocumentBuilder
                    .createBuilder()
                    .setContent(pdf)
                    .setName("Form_A")
                    .withDetachedSignature(sig)
                    .build();
            
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
        assertThat(businessContent.getAttachments().get(0).getMimeType())
                .as("Mime Type %s should be equal to Mime Type %s", 
                        businessContent.getAttachments().get(0).getMimeType().getMimeTypeString(),
                        MimeType.PDF.getMimeTypeString())
                .isEqualTo(MimeType.PDF);
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

    
    ECodexContainer mockedECodexContainer;
    
    void testRecieveContainerContents_initMocks() throws ECodexException {
        mockedECodexContainer = Mockito.mock(ECodexContainer.class);
                
        //mock eCodexContainerService to return mockedECodexContainer
        Mockito.when(eCodexContainerService.receive(any(InputStream.class), any(InputStream.class))).thenReturn(mockedECodexContainer);
        //mock eCodexContainerService for successfull result                
        CheckResult result = Mockito.mock(CheckResult.class);
        Mockito.when(result.isSuccessful()).thenReturn(true);
        Mockito.when(eCodexContainerService.check(any(ECodexContainer.class))).thenReturn(result);
        
        //return converted business doc, out of the asic container extracted files
        DSSDocument businessDocument = Mockito.mock(DSSDocument.class);
        Mockito.when(businessDocument.getName()).thenReturn("documentName");
        Mockito.when(businessDocument.getMimeType()).thenReturn(MimeType.PDF);
        Mockito.when(businessDocument.openStream()).thenReturn(new ByteArrayInputStream("superbusinessdoc".getBytes()));
        
        Mockito.when(mockedECodexContainer.getBusinessDocument()).thenReturn(businessDocument);
        
        //mock signature of business project
        DSSDocument detachedSignatureOfBusinessDoc = Mockito.mock(DSSDocument.class);
        Mockito.when(detachedSignatureOfBusinessDoc.getName()).thenReturn("sig");
        Mockito.when(detachedSignatureOfBusinessDoc.getMimeType()).thenReturn(MimeType.PKCS7);
        Mockito.when(detachedSignatureOfBusinessDoc.openStream()).thenReturn(new ByteArrayInputStream("signature".getBytes()));
        
        //BusinessContent
        BusinessContent mockedBusinesContent = Mockito.mock(BusinessContent.class);
        Mockito.when(mockedBusinesContent.getDetachedSignature()).thenReturn(detachedSignatureOfBusinessDoc);
        Mockito.when(mockedECodexContainer.getBusinessContent()).thenReturn(mockedBusinesContent);
        
        
        //mock the returned token
        Token mockedToken = Mockito.mock(Token.class);        
        Mockito.when(mockedECodexContainer.getToken()).thenReturn(mockedToken);
        Mockito.when(mockedToken.getDocumentType()).thenReturn(MimeType.PDF.getMimeTypeString());
        
               
        //a attachment
        DSSDocument textDoc = Mockito.mock(DSSDocument.class);
        Mockito.when(textDoc.getName()).thenReturn("textdoc");
        Mockito.when(textDoc.getMimeType()).thenReturn(MimeType.TEXT);
        Mockito.when(textDoc.openStream()).thenReturn(new ByteArrayInputStream("a super text".getBytes()));
        
        //mock attachment
        List<DSSDocument> dssDocList = new ArrayList<>();
        dssDocList.add(textDoc);
        Mockito.when(mockedECodexContainer.getBusinessAttachments()).thenReturn(dssDocList);
        
        //mock token PDF
        DSSDocument tokenPDF = Mockito.mock(DSSDocument.class);                
        Mockito.when(tokenPDF.openStream()).thenReturn(new ByteArrayInputStream("pdf token".getBytes()));
        Mockito.when(mockedECodexContainer.getTokenPDF()).thenReturn(tokenPDF);
        
        //mock token XML
        DSSDocument tokenXML = Mockito.mock(DSSDocument.class);                
        Mockito.when(tokenXML.openStream()).thenReturn(new ByteArrayInputStream("xml token".getBytes()));
        Mockito.when(mockedECodexContainer.getTokenXML()).thenReturn(tokenXML);
    }
    
    /**
     * Should throw an exception if the message does not contain a
     * asic container in the message attachments
     * 
     * @throws ECodexException the Exception expected by the test
     * 
     */
    @Test(expected=DomibusConnectorSecurityException.class)
    public void testRecieveContainerContents_noAsicContainer_shouldThrowException() throws ECodexException {
        testRecieveContainerContents_initMocks();
        DomibusConnectorMessage testMessage = createTestMessage();
        
        DomibusConnectorMessageAttachment xmlTokenAttach = new DomibusConnectorMessageAttachment(
                "<xmlContent></xmlContent>".getBytes(), 
                DomibusSecurityContainer.TOKEN_XML_IDENTIFIER);
        
        testMessage.addAttachment(xmlTokenAttach);
                        
        //execute method to test                
        securityContainer.recieveContainerContents(testMessage);
    }
    
    /**
     * should throw an Exception if the message does not contain a
     * token Xml in the message attachments
     * 
     * @throws ECodexException the Exception expected by the test
     */
    @Test(expected=DomibusConnectorSecurityException.class)
    public void testRecieveContainerContents_noTokenXML_shouldThrowException() throws ECodexException {
        testRecieveContainerContents_initMocks();
        DomibusConnectorMessage testMessage = createTestMessage();
        
        DomibusConnectorMessageAttachment xmlTokenAttach = new DomibusConnectorMessageAttachment(
                "<xmlContent></xmlContent>".getBytes(), 
                DomibusSecurityContainer.TOKEN_XML_IDENTIFIER);
        
        testMessage.addAttachment(xmlTokenAttach);
                        
        //execute method to test                
        securityContainer.recieveContainerContents(testMessage);
    }
    
    
    
    @Test
    public void testRecieveContainerContents() throws ECodexException, IOException {
        testRecieveContainerContents_initMocks();
        DomibusConnectorMessage testMessage = createTestMessage();
        
        DomibusConnectorMessageAttachment asicAttach = new DomibusConnectorMessageAttachment(
                "asiccontent".getBytes(), 
                DomibusSecurityContainer.ASICS_CONTAINER_IDENTIFIER);
        
        DomibusConnectorMessageAttachment xmlTokenAttach = new DomibusConnectorMessageAttachment(
                "<xmlContent></xmlContent>".getBytes(), 
                DomibusSecurityContainer.TOKEN_XML_IDENTIFIER);
        
        testMessage.addAttachment(asicAttach);
        testMessage.addAttachment(xmlTokenAttach);
        
        
        
        //execute method to test                
        securityContainer.recieveContainerContents(testMessage);
        
                
        //test xml Content
        assertThat(testMessage.getMessageContent().getXmlContent()).isNotNull();
        
        //test document
        assertThat(IOUtils.toString(testMessage.getMessageContent().getDocument().getDocument(), "UTF8")).as("is provided").isEqualTo("superbusinessdoc");
                
        //Test signature
        DetachedSignature detachedSignature = testMessage.getMessageContent().getDocument().getDetachedSignature();
        assertThat(detachedSignature).as("is set in testdata").isNotNull();
        assertThat(detachedSignature.getDetachedSignatureName()).as("signature is named sig").isEqualTo("sig");
        assertThat(IOUtils.toString(detachedSignature.getDetachedSignature(), "UTF8")).as("signature should be").isEqualTo("signature");
        
        //test attachments
        assertThat(testMessage.getMessageAttachments()).as("Contains one attachment + pdf token and xml token makes 3").hasSize(3);
        
        //test pdf token
        DomibusConnectorMessageAttachment pdfTokenAttachment = testMessage.getMessageAttachments().stream()
                .filter( a -> DomibusSecurityContainer.TOKEN_PDF_IDENTIFIER.equals(a.getIdentifier()))
                .findFirst().get();
        assertThat(pdfTokenAttachment.getAttachment()).isEqualTo("pdf token".getBytes());
        
                        
        //test xml token
        DomibusConnectorMessageAttachment xmlTokenAttachment = testMessage.getMessageAttachments().stream()
                .filter( a -> DomibusSecurityContainer.TOKEN_XML_IDENTIFIER.equals(a.getIdentifier()))
                .findFirst().get();
        assertThat(xmlTokenAttachment.getAttachment()).isEqualTo("xml token".getBytes());
        
    }
    
}
