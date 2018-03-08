package eu.domibus.connector.security.container.service;

import eu.domibus.connector.security.configuration.DomibusConnectorEnvironmentConfiguration;
import eu.domibus.connector.security.container.SignatureParametersConfiguration;
import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import eu.domibus.connector.security.validation.DomibusConnectorCertificateVerifier;
import eu.domibus.connector.security.validation.DomibusConnectorTechnicalValidationServiceFactory;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;


import java.io.*;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={
        DomibusConnectorEnvironmentConfiguration.class,
        DomibusConnectorCertificateVerifier.class,
        DomibusConnectorProxyConfig.class,
        DomibusConnectorTechnicalValidationServiceFactory.class,
        SignatureParametersConfiguration.class,
        SecurityToolkitConfigurationProperties.class

})
@TestPropertySource({"classpath:test.properties", "classpath:test-sig.properties"})
public class DomibusConnectorSecurityContainerServiceITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityContainerServiceITCase.class);
    public static final String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private static File TEST_RESULTS_FOLDER;


    @Autowired
    SignatureParameters signatureParameters;

    @Autowired
    DomibusConnectorTechnicalValidationServiceFactory technicalValidationServiceFactory;




    private TokenIssuer tokenIssuer;


    @BeforeClass
    public static void initClass() {
        String dir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        dir = dir + "/" + DomibusConnectorSecurityContainerServiceITCase.class.getSimpleName();
        TEST_RESULTS_FOLDER = new File(dir);
        TEST_RESULTS_FOLDER.mkdirs();

    }


    @Before
    public void setUp() {
        tokenIssuer = new TokenIssuer();
        tokenIssuer.setCountry("AT");
        tokenIssuer.setServiceProvider("TEST");
        tokenIssuer.setAdvancedElectronicSystem(AdvancedSystemType.SIGNATURE_BASED);

        ECodexTechnicalValidationService technicalValidationService = technicalValidationServiceFactory.technicalValidationService(null);


    }

    @Test
    public void simpleSecurityContainerTest() throws ECodexException, IOException {
        //securityContainerService.create()



        BusinessContent businessContent = new BusinessContent();

        //bytes name mimetype
        DSSDocument dssDocument = loadDocumentFromResource("examples/Form_A.pdf", "Form_A", MimeType.PDF);



//
//        writeDssDocToDisk("simpleTest", asicDocument);



    }

    private void writeDssDocToDisk(String prefix, DSSDocument document) throws IOException {

        File f = new File(TEST_RESULTS_FOLDER + File.separator + "prefix");
        f.mkdirs();

        String docName = document.getName();
        File docPath = new File(f.getAbsolutePath() + File.separator + docName);

        LOGGER.info("Writing document [{}] to [{}]", document, docPath );


        FileOutputStream fileOutputStream = new FileOutputStream(docPath);
        StreamUtils.copy(document.openStream(), fileOutputStream);
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