package eu.ecodex.connector.security;

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

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
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

        MessageDetails details = new MessageDetails();

        MessageContent content = new MessageContent();

        try {
            content.setPdfDocument(getRessource("siginfos-1_p12-acro_pers1-acro_pers2-acro.pdf"));
            // content.setPdfDocument(getRessource("content.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = new Message(details, content);

        try {
            securityContainer.createContainer(message);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        Assert.notEmpty(message.getAttachments());

        securityContainer.recieveContainerContents(message);

        String dir = System.getProperty("user.dir") + "/src/test/resources/";
        for (MessageAttachment attachment : message.getAttachments()) {

            if (attachment.getName().equals("Token.xml")) {
                try {
                    FileOutputStream fos = new FileOutputStream(dir + "result_token.xml");
                    fos.write(attachment.getAttachment());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (attachment.getName().equals("Token.pdf")) {
                try {
                    FileOutputStream fos = new FileOutputStream(dir + "result_token.pdf");
                    fos.write(attachment.getAttachment());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected static byte[] getRessource(final String name) throws IOException {
        String dir = System.getProperty("user.dir");

        File file = new File(dir + "/src/test/resources/" + name);

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();

        return data;
    }
}
