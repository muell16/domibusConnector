package eu.domibus.connector.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageAttachment;
import eu.domibus.connector.common.message.MessageContent;
import eu.domibus.connector.common.message.MessageDetails;
import eu.domibus.connector.security.container.DomibusSecurityContainer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/context/DomibusConnectorSecurityToolkitContext.xml",
"/test/context/testContext.xml" })
public class DomibusConnectorSecurityToolkitTest {

	static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitTest.class);

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

//		try {
			securityContainer.createContainer(message);

//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			e.printStackTrace();
//		}

		Assert.notEmpty(message.getAttachments());

		securityContainer.recieveContainerContents(message);

		for (MessageAttachment attachment : message.getAttachments()) {

			if (attachment.getName().equals("Token.xml")) {
				writeResult(resultName +".xml", attachment.getAttachment());
			} else if (attachment.getName().equals("Token.pdf")) {
				writeResult(resultName +".pdf", attachment.getAttachment());
			}
		}
	}


	private static String getRessourceFolder(){
		String dir = System.getProperty("user.dir");
		dir = dir + "/src/test/resources/";

		return dir;

	}

	private static byte[] readRessource(final String name){
		try{
			File file = new File(getRessourceFolder() + "examples/" + name);

			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fileInputStream.read(data);
			fileInputStream.close();

			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void writeResult(final String name, final byte[] data){
		try {
			FileOutputStream fos = new FileOutputStream(getRessourceFolder() + "result/" + name);
			fos.write(data);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
