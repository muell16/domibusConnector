package eu.domibus.connector.security;

import eu.domibus.connector.common.spring.CommonProperties;
import eu.domibus.connector.domain.testutil.DomibusConnectorBigDataReferenceGetSetBased;
import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.testutil.DomibusConnectorBigDataPersistenceServicePassthroughImpl;
import eu.domibus.connector.security.container.DomibusSecurityContainer;
import eu.domibus.connector.test.logging.MemoryAppender;
import eu.domibus.connector.test.logging.MemoryAppenderAssert;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.TestPropertySource;

/**
 * basic test of security toolkit
 * 
 * is an integration test because DSSECodexContainerService is not mocked
 * and DSSECodexContainerService is loading data from remote location
 * (the trust lists)
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DomibusConnectorSecurityToolkitITCase.TestContextConfiguration.class, CommonProperties.class})
@TestPropertySource(locations={"classpath:test.properties", "classpath:test-sig.properties"}, 
        properties= {   "liquibase.enabled=false"
})
public class DomibusConnectorSecurityToolkitITCase {


    @SpringBootApplication(scanBasePackages = {"eu.domibus.connector.security"}, exclude = {
        DataSourceAutoConfiguration.class, 
        DataSourceTransactionManagerAutoConfiguration.class, 
        HibernateJpaAutoConfiguration.class
    })
    public static class TestContextConfiguration {

        @Bean
        public static PropertySourcesPlaceholderConfigurer
                propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
                    
        @Bean
        public static DomibusConnectorBigDataPersistenceService bigDataPersistenceService() {
            DomibusConnectorBigDataPersistenceServicePassthroughImpl service = new DomibusConnectorBigDataPersistenceServicePassthroughImpl();
            return Mockito.spy(service);            
        }
        
    }
    
    
	static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitITCase.class);

	public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";

    //mock
    @Autowired
    DomibusConnectorBigDataPersistenceService bigDataPersistenceService;
    
	@Resource
	private DomibusSecurityContainer securityContainer;
	
	@Resource
	private DomibusConnectorSecurityToolkit securityToolkit;

	@Test
	public void testSignedDoc() throws IOException {
		
		testDoc("ExamplePdfSigned.pdf", "signedResultToken");

	}
	
	@Test
	public void testUnsignedDoc() throws IOException {
		
		testDoc("ExamplePdfUnsigned.pdf", "unsignedResultToken");
	}

	@Test
	/**
	 * Ensures that a warning log message is generated
	 * @Since(4.1)
	 */
	public void testUnsignedDoc_WarningLogMessageShouldGenerated() throws IOException {
        testDoc("ExamplePdfUnsigned.pdf", "testUnsignedDoc_WarningLogMessageShouldGenerated");

        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender())
//                .filterOnMarker(LoggingMarker.BUSINESS_CERT_LOG.toString())
                .containsLogLine(DomibusSecurityContainer.RED_TOKEN_WARNING_MESSAGE);


	}


	private void testDoc(final String exampleName, final String resultName) throws IOException {
		DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();

		DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();

        
        DomibusConnectorMessageDocumentBuilder documentBuilder = DomibusConnectorMessageDocumentBuilder.createBuilder()
                .setName("myDocument.pdf")
                .setContent(readRessource(exampleName));
		content.setDocument(documentBuilder.build());
        
		DomibusConnectorMessage message = new DomibusConnectorMessage(details, content);

		
		securityToolkit.buildContainer(message);
		assertThat(message.getMessageAttachments()).as("there must be some attachments").isNotEmpty();

// TODO: put this code in a second test:
		securityToolkit.validateContainer(message);

		for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
			DomibusConnectorBigDataReference result = bigDataPersistenceService.getReadableDataSource(attachment.getAttachment());
			if (attachment.getName().equals("Token.xml")) {
				writeResult(resultName +".xml", StreamUtils.copyToByteArray(result.getInputStream()));
				//test xml


//                if ("signedResultToken".equals(resultName)) {
////TODO: compare resulting xml!
////                    Assert.assertThat(attachment.getAttachment(),
////                            isSimilarTo("control xml").withNodeMatcher(
////                                    new DefaultNodeMatcher(ElementSelectors.byName)));
//                } else if ("unsignedResultToken".equals(resultName)) {
//
//                } else {
//                    throw new IllegalStateException("should not end up here! Passed unsupported result name!");
//                }



			} else if (attachment.getName().equals("Token.pdf")) {
				writeResult(resultName +".pdf", StreamUtils.copyToByteArray(result.getInputStream()));
			}
		}
	}

	private static String getTestFilesDir() {
		String dir = System.getProperty(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "." + File.separator + "target" + File.separator + "testfileresults" + File.separator);
		dir = dir + DomibusConnectorSecurityToolkitITCase.class.getSimpleName();
		return dir;
	}

	private static String getRessourceFolder(){
		String dir = System.getProperty("user.dir");
		dir = dir + "/src/test/resources/";

		return dir;

	}

	private static DomibusConnectorBigDataReference readRessource(final String name) {
		try {
            DomibusConnectorBigDataReferenceGetSetBased dataRef = new DomibusConnectorBigDataReferenceGetSetBased();
			File file = new File(getRessourceFolder() + "examples/" + name);

			FileInputStream fileInputStream = new FileInputStream(file);

            dataRef.setInputStream(fileInputStream);

			return dataRef;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void writeResult(final String name, final byte[] data){
		LOGGER.info("writeResult: writing to [{}]", name);
		try {
			File dir = new File(getTestFilesDir() + File.separator );
			dir.mkdirs();
			FileOutputStream fos = new FileOutputStream(dir + File.separator + name);
			fos.write(data);
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
