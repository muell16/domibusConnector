package eu.ecodex.connector.security;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.ecodex.connector.security.container.ECodexSecurityContainer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/context/ECodexConnectorSecurityToolkitContext.xml",
        "/test/context/testContext.xml" })
public class ECodexConnectorSecurityToolkitTest {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorSecurityToolkitTest.class);

    @Resource
    private ECodexSecurityContainer securityContainer;

    @Test
    public void testSignedDoc() {

        System.out.println("Test skipped!");

        // MessageDetails details = new MessageDetails();
        //
        // MessageContent content = new MessageContent();
        //
        // try {
        // content.setPdfDocument(getRessource("siginfos-1_p12-acro_pers1-acro_pers2-acro.pdf"));
        // // content.setPdfDocument(getRessource("content.pdf"));
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        //
        // Message message = new Message(details, content);
        //
        // try {
        // securityContainer.createContainer(message);
        //
        // } catch (Exception e) {
        // LOGGER.error(e.getMessage());
        // e.printStackTrace();
        // }
        //
        // Assert.notEmpty(message.getAttachments());
        //
        // securityContainer.recieveContainerContents(message);
        //
        // for (MessageAttachment attachment : message.getAttachments()) {
        // if (attachment.getName().equals("Token.xml")) {
        // try {
        // FileOutputStream fos = new FileOutputStream("/result_token.xml");
        // fos.write(attachment.getAttachment());
        // fos.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // } else if (attachment.getName().equals("Token.pdf")) {
        // try {
        // FileOutputStream fos = new FileOutputStream("/result_token.pdf");
        // fos.write(attachment.getAttachment());
        // fos.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        // }

    }

    protected static byte[] getRessource(final String name) throws IOException {
        final InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        return IOUtils.toByteArray(inputStream);
    }
}
