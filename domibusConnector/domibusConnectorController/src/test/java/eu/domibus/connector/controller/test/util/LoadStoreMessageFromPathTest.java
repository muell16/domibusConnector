package eu.domibus.connector.controller.test.util;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class LoadStoreMessageFromPathTest {


    @Test
    public void testLoadMsg() throws IOException {

        Resource r = new ClassPathResource("testmessages/msg2/");

        DC5Message message = LoadStoreMessageFromPath.loadMessageFrom(r);


        assertThat(message).isNotNull();

//        assertThat(message.getMessageAttachments()).hasSize(2);
        assertThat(message.getTransportedMessageConfirmations()).hasSize(1);

    }


    @Test
    public void testStoreMsg() throws  IOException {
        File file = new File("./target/testmsg/msg1/");
        FileSystemUtils.deleteRecursively(file);
        file.mkdirs();

        Resource r = new FileSystemResource("./target/testmsg/msg1/");

        DC5Message message = DomainEntityCreator.createEpoMessage();

        LoadStoreMessageFromPath.storeMessageTo(r, message);
    }


}
