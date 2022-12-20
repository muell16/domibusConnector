package eu.domibus.connector.testdata;

import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.ecodex.dc5.message.model.DC5Ebms;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5MessageContent;
import eu.ecodex.dc5.message.model.EbmsMessageId;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Profile("dev")
@Component
public class StartWithTestData {

    private final DCMessagePersistenceService msgPersistenceService;


    public StartWithTestData(DCMessagePersistenceService msgPersistenceService) {
        this.msgPersistenceService = msgPersistenceService;
    }

    @EventListener
    public void initDb(ContextRefreshedEvent event){
//        Faker faker = new Faker();
        IntStream.iterate(0, n -> n + 1)
                .limit(3)
                .mapToObj(i -> {
                    final DC5Message message = DomainEntityCreator.createMessage();

                    message.setEbmsData(DC5Ebms.builder()
                            .ebmsMessageId(EbmsMessageId.ofString("ebmsId"+i))
                            .build());
                    return message;
                })
                .peek(m -> {
                    final DC5MessageContent messageContentWithDocumentWithNoPdfDocument = DomainEntityCreator.createMessageContentWithDocumentWithNoPdfDocument();
                    m.setMessageContent(messageContentWithDocumentWithNoPdfDocument);
                })
                .forEach(msgPersistenceService::persistBusinessMessageIntoDatabase);
    }
}
