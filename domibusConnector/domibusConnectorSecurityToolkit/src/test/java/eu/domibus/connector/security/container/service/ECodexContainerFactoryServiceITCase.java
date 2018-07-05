package eu.domibus.connector.security.container.service;

import eu.domibus.connector.common.spring.CommonProperties;
import eu.domibus.connector.security.configuration.DomibusConnectorEnvironmentConfiguration;
import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import eu.domibus.connector.security.validation.DomibusConnectorCertificateVerifier;
import eu.domibus.connector.security.validation.DomibusConnectorTechnicalValidationServiceFactory;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckProblem;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;


import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={
        DomibusConnectorEnvironmentConfiguration.class,
        DomibusConnectorCertificateVerifier.class,
        DomibusConnectorProxyConfig.class,
        DomibusConnectorTechnicalValidationServiceFactory.class,
        SecurityToolkitConfigurationProperties.class,
        ECodexContainerFactoryService.class,
        TokenIssuerFactory.class,
        CommonProperties.class
})
@EnableConfigurationProperties
@TestPropertySource({"classpath:test.properties", "classpath:test-sig.properties"})
public class ECodexContainerFactoryServiceITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(ECodexContainerFactoryServiceITCase.class);
    public static final String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private static File TEST_RESULTS_FOLDER;


    @Autowired
    DomibusConnectorTechnicalValidationServiceFactory technicalValidationServiceFactory;

    @Autowired
    ECodexContainerFactoryService eCodexContainerFactoryService;

    @Autowired
    TokenIssuerFactory tokenIssuerFactory;


    @BeforeClass
    public static void initClass() {
        String dir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        dir = dir + "/" + ECodexContainerFactoryServiceITCase.class.getSimpleName();
        TEST_RESULTS_FOLDER = new File(dir);
        TEST_RESULTS_FOLDER.mkdirs();

    }


    @Before
    public void setUp() {

    }

    @Test
    public void simpleTestCreateContainerServiceAndBuildAsicContainer() throws ECodexException, IOException {


        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(null);

        BusinessContent businessContent = new BusinessContent();

        //bytes name mimetype
        DSSDocument businessDoc = loadDocumentFromResource("examples/Form_A.pdf", "Form_A.pdf", MimeType.PDF);
        businessContent.setDocument(businessDoc);

        DSSDocument dssDocument = loadDocumentFromResource("examples/Form_A.pdf", "Addition.pdf", MimeType.PDF);
        businessContent.addAttachment(dssDocument);

        ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent, tokenIssuerFactory.getTokenIssuer(null));

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

        eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(null);
        eCodexContainer = eCodexContainerService.receive(new ByteArrayInputStream(asics), new ByteArrayInputStream(tokenXml));

    }


    @Test
    public void testCreateContainerServiceAndResolveAsicContainer() throws IOException, ECodexException {

        ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(null);


        InputStream asicContainer = getClass().getResourceAsStream("/examples/asic-s.asics");
        InputStream xmlToken = getClass().getResourceAsStream("/examples/asic-s_trustoktoken.xml");

        assertThat(asicContainer).isNotNull();
        assertThat(xmlToken).isNotNull();


        ECodexContainer eCodexContainer =
                eCodexContainerService.receive(asicContainer, xmlToken);

        CheckResult check = eCodexContainerService.check(eCodexContainer);

        assertThat(check.isSuccessful()).isTrue();

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


    private void writeDssDocToDisk(String prefix, DSSDocument document) throws IOException {

        File f = new File(TEST_RESULTS_FOLDER + File.separator + prefix);
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