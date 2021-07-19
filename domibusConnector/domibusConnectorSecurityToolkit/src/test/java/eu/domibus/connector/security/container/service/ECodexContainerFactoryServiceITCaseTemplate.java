package eu.domibus.connector.security.container.service;

import eu.domibus.connector.common.spring.CommonProperties;
import eu.domibus.connector.domain.model.DCMessageProcessSettings;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageContentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.security.aes.DomibusConnectorAESTechnicalValidationService;
import eu.domibus.connector.security.aes.DomibusConnectorAESTokenValidationCreator;
import eu.domibus.connector.security.configuration.DomibusConnectorEnvironmentConfiguration;
import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import eu.domibus.connector.security.validation.DomibusConnectorCertificateVerifier;
import eu.domibus.connector.security.validation.DomibusConnectorTechnicalValidationServiceFactory;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.Token;
import eu.ecodex.dss.model.token.TokenDocument;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.util.TokenJAXBObjectFactory;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;
import org.xml.sax.SAXException;

import javax.naming.spi.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
@SpringBootTest(classes={
        DomibusConnectorEnvironmentConfiguration.class,
        DomibusConnectorCertificateVerifier.class,
        DomibusConnectorProxyConfig.class,
        DomibusConnectorTechnicalValidationServiceFactory.class,
        SecurityToolkitConfigurationProperties.class,
        ECodexContainerFactoryService.class,
        TokenIssuerFactory.class,
        DomibusConnectorAESTokenValidationCreator.class,
        CommonProperties.class,
        TokenIssuerFactoryProperties.class
})
@EnableConfigurationProperties
@TestPropertySource({"classpath:test.properties", "classpath:test-sig.properties"})
//@ActiveProfiles({"test", "test-sig"})
@Disabled("Is used as template") //there is currently no other smoth way to run a
// spring test with different environments/settings via junit5 test-templates, extension,...
// so inheritance is used
public class ECodexContainerFactoryServiceITCaseTemplate {

    private final static Logger LOGGER = LoggerFactory.getLogger(ECodexContainerFactoryServiceITCaseTemplate.class);
    public static final String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private static File TEST_RESULTS_FOLDER;


    @Autowired
    DomibusConnectorTechnicalValidationServiceFactory technicalValidationServiceFactory;

    @Autowired
    ECodexContainerFactoryService eCodexContainerFactoryService;

    @Autowired
    TokenIssuerFactory tokenIssuerFactory;


    @BeforeAll
    public static void initClass(TestInfo testInfo) {
        String dir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        dir = dir + "/" + testInfo.getTestClass().get().getSimpleName();
        TEST_RESULTS_FOLDER = new File(dir);
        TEST_RESULTS_FOLDER.mkdirs();

    }


    @BeforeEach
    public void setUp() {

    }

    @Test
    @DisplayName("Build ASIC-S container with XML as business doc")
    public void createContainerFromXML(TestInfo testInfo) throws ECodexException, IOException {

        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());

        BusinessContent businessContent = new BusinessContent();

        //bytes name mimetype
        DSSDocument businessDoc = loadDocumentFromResource("examples/ExampleXmlSigned.xml", "ExampleXmlSigned.xml", MimeType.XML);
        businessContent.setDocument(businessDoc);

        ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent, tokenIssuerFactory.getTokenIssuer(new DomibusConnectorMessage()));

        DSSDocument asicDocument = eCodexContainer.getAsicDocument();
        assertThat(asicDocument).isNotNull();
//
        asicDocument.setName("asic-s.asics.zip");
        writeDssDocToDisk(testInfo, asicDocument);

        DSSDocument tokenXML = eCodexContainer.getTokenXML();
        assertThat(tokenXML).isNotNull();
        tokenXML.setName("asic-s_trustoktoken.xml");
        writeDssDocToDisk(testInfo, tokenXML);

        //check if produced container and token can also be resolved again
        byte[] asics = StreamUtils.copyToByteArray(asicDocument.openStream());
        byte[] tokenXml = StreamUtils.copyToByteArray(tokenXML.openStream());

        eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());
        eCodexContainer = eCodexContainerService.receive(new ByteArrayInputStream(asics), new ByteArrayInputStream(tokenXml));

    }

    @Test
    @DisplayName("Build ASIC-S container with XML authentication based")
    public void createContainerFromXMLWithAuthenticationBased(TestInfo testInfo) throws ECodexException, IOException, ParserConfigurationException, SAXException, JAXBException {


        DCMessageProcessSettings settings = new DCMessageProcessSettings();
        settings.setValidationServiceName(AdvancedSystemType.AUTHENTICATION_BASED.name());
        DomibusConnectorMessage msg = DomibusConnectorMessageBuilder.createBuilder()
                .setMessageDetails(DomibusConnectorMessageDetailsBuilder.create()
                        .withOriginalSender("originalSender")
                        .build())
                .build();
        msg.setDcMessageProcessSettings(settings);

        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(msg);

        BusinessContent businessContent = new BusinessContent();

        //bytes name mimetype
        DSSDocument businessDoc = loadDocumentFromResource("examples/ExampleXmlSigned.xml", "ExampleXmlSigned.xml", MimeType.XML);
        businessContent.setDocument(businessDoc);

        ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent, tokenIssuerFactory.getTokenIssuer(msg));

        DSSDocument asicDocument = eCodexContainer.getAsicDocument();
        assertThat(asicDocument).isNotNull();
//
        asicDocument.setName("asic-s.asics.zip");
        writeDssDocToDisk(testInfo, asicDocument);

        DSSDocument tokenXML = eCodexContainer.getTokenXML();
        assertThat(tokenXML).isNotNull();
        tokenXML.setName("asic-s_trustoktoken.xml");
        writeDssDocToDisk(testInfo, tokenXML);

        //check if produced container and token can also be resolved again
        byte[] asics = StreamUtils.copyToByteArray(asicDocument.openStream());
        byte[] tokenXml = StreamUtils.copyToByteArray(tokenXML.openStream());



        eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());
        eCodexContainer = eCodexContainerService.receive(new ByteArrayInputStream(asics), new ByteArrayInputStream(tokenXml));

        JAXBContext jaxbContext = JAXBContext.newInstance(Token.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<Token> jaxbToken = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(tokenXml)), Token.class);

        Token token = jaxbToken.getValue();

        assertThat(token.getAdvancedElectronicSystem())
                .as("The system type has been passed with the message and should be AUTHENTICATION BASED!")
                .isEqualTo(AdvancedSystemType.AUTHENTICATION_BASED);

    }


    @Test
    @DisplayName("Build ASIC-S container with ASIC-S as business doc")
    public void createContainerFromAsicS(TestInfo testInfo) throws ECodexException, IOException {

        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());

        BusinessContent businessContent = new BusinessContent();

        //bytes name mimetype
        DSSDocument businessDoc = loadDocumentFromResource("examples/ExampleAsics.asics", "ExampleAsics.asics", MimeType.ASICS);
        businessContent.setDocument(businessDoc);

        ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent, tokenIssuerFactory.getTokenIssuer(new DomibusConnectorMessage()));

        DSSDocument asicDocument = eCodexContainer.getAsicDocument();
        assertThat(asicDocument).isNotNull();
//
        asicDocument.setName("asic-s.asics.zip");
        writeDssDocToDisk(testInfo, asicDocument);

        DSSDocument tokenXML = eCodexContainer.getTokenXML();
        assertThat(tokenXML).isNotNull();
        tokenXML.setName("asic-s_trustoktoken.xml");
        writeDssDocToDisk(testInfo, tokenXML);

        //check if produced container and token can also be resolved again
        byte[] asics = StreamUtils.copyToByteArray(asicDocument.openStream());
        byte[] tokenXml = StreamUtils.copyToByteArray(tokenXML.openStream());

        eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());
        eCodexContainer = eCodexContainerService.receive(new ByteArrayInputStream(asics), new ByteArrayInputStream(tokenXml));

        //there must be a token XML
        assertThat(eCodexContainer.getTokenXML()).isNotNull();

        //there must be a pdf token!
        assertThat(eCodexContainer.getTokenPDF()).isNotNull();

        //there must also be a business document!
        assertThat(eCodexContainer.getBusinessContent().getDocument()).isNotNull();
    }


    @Test
    @DisplayName("Build ASIC-S container with unsigned doc.txt")
    public void createContainerFromTextDocument(TestInfo testInfo) throws ECodexException, IOException {

        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());

        BusinessContent businessContent = new BusinessContent();

        //bytes name mimetype
        DSSDocument businessDoc = loadDocumentFromResource("examples/text.txt", "text.txt", MimeType.TEXT);
        businessContent.setDocument(businessDoc);

        ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent, tokenIssuerFactory.getTokenIssuer(new DomibusConnectorMessage()));

        DSSDocument asicDocument = eCodexContainer.getAsicDocument();
        assertThat(asicDocument).isNotNull();
//
        asicDocument.setName("asic-s.asics.zip");
        writeDssDocToDisk(testInfo, asicDocument);

        DSSDocument tokenXML = eCodexContainer.getTokenXML();
        assertThat(tokenXML).isNotNull();
        tokenXML.setName("asic-s_trustoktoken.xml");
        writeDssDocToDisk(testInfo, tokenXML);

        //check if produced container and token can also be resolved again
        byte[] asics = StreamUtils.copyToByteArray(asicDocument.openStream());
        byte[] tokenXml = StreamUtils.copyToByteArray(tokenXML.openStream());

        eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());
        eCodexContainer = eCodexContainerService.receive(new ByteArrayInputStream(asics), new ByteArrayInputStream(tokenXml));

    }


    @Test
    public void simpleTestCreateContainerServiceAndBuildAsicContainer() throws ECodexException, IOException {


        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());

        BusinessContent businessContent = new BusinessContent();

        //bytes name mimetype
        DSSDocument businessDoc = loadDocumentFromResource("examples/Form_A.pdf", "Form_A.pdf", MimeType.PDF);
        businessContent.setDocument(businessDoc);

        DSSDocument dssDocument = loadDocumentFromResource("examples/Form_A.pdf", "Addition.pdf", MimeType.PDF);
        businessContent.addAttachment(dssDocument);

        ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent, tokenIssuerFactory.getTokenIssuer(new DomibusConnectorMessage()));

        DSSDocument asicDocument = eCodexContainer.getAsicDocument();
        assertThat(asicDocument).isNotNull();
//
//        asicDocument.setName("asic-s.asics");
//        writeDssDocToDisk("simpleTest", asicDocument);

        DSSDocument tokenXML = eCodexContainer.getTokenXML();
        assertThat(tokenXML).isNotNull();
//        tokenXML.setName("asic-s_trustoktoken.xml");
//        writeDssDocToDisk("simpleTest", tokenXML);

        //check if produced container and token can also be resolved again
        byte[] asics = StreamUtils.copyToByteArray(asicDocument.openStream());
        byte[] tokenXml = StreamUtils.copyToByteArray(tokenXML.openStream());

        eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());
        eCodexContainer = eCodexContainerService.receive(new ByteArrayInputStream(asics), new ByteArrayInputStream(tokenXml));

    }


    @Test
    public void testCreateContainerServiceAndResolveAsicContainer() throws IOException, ECodexException {

        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());


        InputStream asicContainer = getClass().getResourceAsStream("/examples/asic-s.asics");
        InputStream xmlToken = getClass().getResourceAsStream("/examples/asic-s_trustoktoken.xml");

        assertThat(asicContainer).isNotNull();
        assertThat(xmlToken).isNotNull();


        ECodexContainer eCodexContainer =
                eCodexContainerService.receive(asicContainer, xmlToken);

        CheckResult check = eCodexContainerService.check(eCodexContainer);

        assertThat(check.isSuccessful()).isTrue();

        DSSDocument dssDocument = eCodexContainer.getBusinessAttachments().get(0);
        assertThat(dssDocument.getName()).isEqualTo("Addition.pdf");

    }

//    @Test
//    @Ignore("not finished yet!")
//    public void simpleTestCreateContainerServiceAndBuildAsicContainer_withStreams() throws IOException, ECodexException {
//
//        //securityContainerService.create()
//
//        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(null);
//
//        BusinessContent businessContent = new BusinessContent();
//
//        //bytes name mimetype
//        DSSDocument businessDoc = loadDocumentFromResource("examples/Form_A.pdf", "Form_A.pdf", MimeType.PDF);
//        businessContent.setDocument(businessDoc);
//
//        DSSDocument dssDocument = loadDocumentFromResource("examples/Form_A.pdf", "Addition.pdf", MimeType.PDF);
//        businessContent.addAttachment(dssDocument);
//
//        ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent, tokenIssuerFactory.getTokenIssuer(null));
//
//    }


    private void writeDssDocToDisk(TestInfo testInfo, DSSDocument document) throws IOException {

        String testMethodName = testInfo.getTestMethod().get().getName();

        File f = new File(TEST_RESULTS_FOLDER + File.separator + testMethodName);
        f.mkdirs();

        String docName = document.getName();
        File docPath = new File(f.getAbsolutePath() + File.separator + docName);

        LOGGER.info("Writing document [{}] to [{}]", document, docPath );


        FileOutputStream fileOutputStream = new FileOutputStream(docPath);
        document.writeTo(fileOutputStream);

    }

    private InMemoryDocument loadDocumentFromResource(String classpathResource, String name, MimeType mimeType) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(classpathResource);
        InputStream inputStream = classPathResource.getInputStream();
        byte[] content = StreamUtils.copyToByteArray(inputStream);
        InMemoryDocument doc = new InMemoryDocument(content, name, mimeType);

        return doc;
    }

    private byte[] loadByteArrayFromResource(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();
            return StreamUtils.copyToByteArray(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }



}
