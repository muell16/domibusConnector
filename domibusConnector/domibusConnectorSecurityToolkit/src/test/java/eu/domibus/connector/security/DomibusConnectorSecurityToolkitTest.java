package eu.domibus.connector.security;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageAttachment;
import eu.domibus.connector.common.message.MessageContent;
import eu.domibus.connector.common.message.MessageDetails;
import eu.domibus.connector.security.container.DomibusSecurityContainer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.matchers.CompareMatcher;

import static org.assertj.core.api.Assertions.*;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/context/DomibusConnectorSecurityToolkitContext.xml",
"/test/context/testContext.xml" })
public class DomibusConnectorSecurityToolkitTest {

	static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitTest.class);

	public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";

	@Resource
	private DomibusSecurityContainer securityContainer;

	@Test
	public void testSignedDoc() {
		
		testDoc("ExamplePdfSigned.pdf", "signedResultToken");

	}
	
	@Test
	public void testUnsignedDoc() {
		
		testDoc("ExamplePdfUnsigned.pdf", "unsignedResultToken");

	}


	private void testDoc(final String exampleName, final String resultName) {
		MessageDetails details = new MessageDetails();

		MessageContent content = new MessageContent();

		content.setPdfDocument(readRessource(exampleName));
		
		Message message = new Message(details, content);

		try {
			securityContainer.createContainer(message);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		assertThat(message.getAttachments()).isNotEmpty();
		//Assert.notEmpty(message.getAttachments());

		securityContainer.recieveContainerContents(message);

		for (MessageAttachment attachment : message.getAttachments()) {

			if (attachment.getName().equals("Token.xml")) {
				writeResult(resultName +".xml", attachment.getAttachment());
				//test xml
                if ("signedResultToken".equals(resultName)) {
//TODO: compare resulting xml!
//                    Assert.assertThat(attachment.getAttachment(),
//                            isSimilarTo("control xml").withNodeMatcher(
//                                    new DefaultNodeMatcher(ElementSelectors.byName)));
                } else if ("unsignedResultToken".equals(resultName)) {

                } else {
                    throw new IllegalStateException("should not end up here! Passed unsupported result name!");
                }



			} else if (attachment.getName().equals("Token.pdf")) {
				writeResult(resultName +".pdf", attachment.getAttachment());
			}
		}
	}

	private static String getTestFilesDir() {
		String dir = System.getProperty(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "." + File.separator + "target" + File.separator + "testfileresults" + File.separator);
		dir = dir + DomibusConnectorSecurityToolkitTest.class.getSimpleName();
		return dir;
	}

	private static String getRessourceFolder(){
		String dir = System.getProperty("user.dir");
		dir = dir + "/src/test/resources/";

		return dir;

	}

	private static byte[] readRessource(final String name) {
		try {

			File file = new File(getRessourceFolder() + "examples/" + name);

			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fileInputStream.read(data);
			fileInputStream.close();

			return data;
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
