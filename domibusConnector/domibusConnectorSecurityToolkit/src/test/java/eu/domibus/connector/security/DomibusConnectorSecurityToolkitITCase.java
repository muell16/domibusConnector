package eu.domibus.connector.security;

import eu.domibus.connector.common.spring.CommonProperties;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.domain.testutil.LargeFileReferenceGetSetBased;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.testutil.LargeFilePersistenceServicePassthroughImpl;
import eu.domibus.connector.security.container.DomibusSecurityContainer;
import eu.domibus.connector.test.logging.MemoryAppender;
import eu.domibus.connector.test.logging.MemoryAppenderAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * basic test of security toolkit
 * 
 * is an integration test because DSSECodexContainerService is not mocked
 * and DSSECodexContainerService is loading data from remote location
 * (the trust lists)
 * 
 *
 */
@ExtendWith(SpringExtension.class)
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
        public static LargeFilePersistenceService bigDataPersistenceService() {
            LargeFilePersistenceServicePassthroughImpl service = new LargeFilePersistenceServicePassthroughImpl();
            return Mockito.spy(service);            
        }
        
    }
    
    
	static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitITCase.class);

	public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";

    //mock
    @Autowired
    LargeFilePersistenceService bigDataPersistenceService;
    
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
	@Disabled("defect on jenkins CI")
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
			LargeFileReference result = bigDataPersistenceService.getReadableDataSource(attachment.getAttachment());
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

	private static LargeFileReference readRessource(final String name) {
		try {
            LargeFileReferenceGetSetBased dataRef = new LargeFileReferenceGetSetBased();
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
