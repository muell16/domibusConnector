package eu.domibus.connector.testdata;

import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class LoadStoreTransitionMessageTest {


    public static final String TEST_DIR = "./target/teststore/";

    @BeforeAll
    public static void beforeAll() throws IOException {
        Path p = Paths.get(TEST_DIR);
        FileSystemUtils.deleteRecursively(p);
        Files.createDirectory(p);
    }

    @Test
    public void loadMessageFrom() throws Exception {
        DomibusConnectorMessageType msg1 = LoadStoreTransitionMessage.loadMessageFrom(new ClassPathResource("endtoendtest/messages/epo_forma_backend_to_gw/"));

        assertThat(msg1).isNotNull();

        assertThat(msg1.getMessageAttachments()).hasSize(0);
        assertThat(msg1.getMessageContent().getXmlContent()).isNotNull();
        assertThat(msg1.getMessageContent().getDocument().getDocument()).isNotNull();
    }

    @Test
    public void testStoreMessage() throws Exception {
        Path p = Paths.get(TEST_DIR).resolve("testmsg1");

        FileSystemResource resource = new FileSystemResource(p.toFile());
        DomibusConnectorMessageType testmessage = TransitionCreator.createMessage();

        LoadStoreTransitionMessage.storeMessageTo(resource, testmessage, true);

    }

    @Test
    public void testStoreThanLoad() throws Exception {
        DomibusConnectorMessageType testmessage = TransitionCreator.createMessage();

        Path p = Paths.get(TEST_DIR).resolve("testmsg2");
        LoadStoreTransitionMessage.storeMessageTo(p, testmessage, true);

        DomibusConnectorMessageType testmsg2 = LoadStoreTransitionMessage.loadMessageFrom(p);



    }

}