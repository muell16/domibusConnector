package eu.domibus.connector.controller.test.util;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LoadStoreMessageFromPathTest {


    @Test
    public void testLoadMsg1() throws IOException {

        Resource r = new ClassPathResource("testmessages/msg1/");

        DomibusConnectorMessage message = LoadStoreMessageFromPath.loadMessageFrom(r);


        assertThat(message).isNotNull();

        assertThat(message.getMessageAttachments()).hasSize(2);
        assertThat(message.getMessageConfirmations()).hasSize(1);

    }




}
