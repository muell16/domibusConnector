package eu.domibus.connector.controller.processor;


import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DCMessageContentManager;
import eu.domibus.connector.security.container.service.TokenIssuerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ITCaseTestContext.class, TokenIssuerFactory.class})
@TestPropertySource("classpath:config/application-test.properties")
@Commit
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test"})
@Sql(scripts = "/testdata.sql") //adds testdata to database like domibus-blue party
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ToBackendBusinessMessageProcessorITCase {


    @Autowired
    @Qualifier("GatewayToBackendMessageProcessor")
    private DomibusConnectorMessageProcessor gatewayToBackendMessageProcessor;

    @Autowired
    @Qualifier(ITCaseTestContext.TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    private BlockingQueue<DomibusConnectorMessage> toGatewayDeliveredMessages;

    @Autowired
    @Qualifier(ITCaseTestContext.TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    private BlockingQueue<DomibusConnectorMessage> toBackendDeliveredMessages;

    @Autowired
    private DCMessagePersistenceService messagePersistenceService;

    @Autowired
    private DCMessageContentManager bigDataWithMessagePersistenceService;

    @Test
    @Disabled("testmessage msg2 crt is outdated")
    public void testProcessMessage() throws IOException {
        Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
            //create test originalMessage and persist originalMessage into DB
            DomibusConnectorMessage message = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/msg2/"));
            message.setConnectorMessageId("msg3");
            message.getMessageDetails().setEbmsMessageId("EBMS1");


            message = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
//            message = bigDataWithMessagePersistenceService.persistAllBigFilesFromMessage(message);
//            message = messagePersistenceService.mergeMessageWithDatabase(message);
//            message = bigDataWithMessagePersistenceService.setAllLargeFilesReadable(message);

            //start test
            gatewayToBackendMessageProcessor.processMessage(message);


            //validate test results
            assertThat(toBackendDeliveredMessages).as("One originalMessage should be delivered to backendlink").hasSize(1);
        });
    }


}
