package eu.domibus.connector.testdata;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.stream.IntStream;

@Profile("dev")
@Component
public class StartWithTestData {

    private final Validator validator;
    private final DC5MessageRepo msgRepo;


    public StartWithTestData(Validator validator, DC5MessageRepo msgRepo) {
        this.validator = validator;
        this.msgRepo = msgRepo;
    }

    @EventListener
    public void initDb(ContextRefreshedEvent event){
//        Faker faker = new Faker();
        System.out.println("foo");
        IntStream.iterate(0, n -> n + 1)
                .limit(2000)
                .mapToObj(i -> {
                    final DC5Message message = DomainEntityCreator.createMessage();
                    message.setConnectorMessageId(DomibusConnectorMessageId.ofRandom());
                    message.setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);

                    message.setEbmsData(DC5Ebms.builder()
                            .ebmsMessageId(EbmsMessageId.ofString("ebmsId"+i))
                            .build());
                    return message;
                })
                .peek(m -> {
                    final DC5MessageContent messageContentWithDocumentWithNoPdfDocument = DomainEntityCreator.createMessageContentWithDocumentWithNoPdfDocument();
                    messageContentWithDocumentWithNoPdfDocument.setCurrentState(
                            DC5BusinessMessageState.builder()
                                    .event(DC5BusinessMessageState.BusinessMessageEvents.NEW_MSG)
                                    .state(DC5BusinessMessageState.BusinessMessagesStates.CREATED)
                                    .build()
                    );
                    m.setMessageContent(messageContentWithDocumentWithNoPdfDocument);
                })
                .peek(m->{
                    validator.validate(m).forEach(System.out::println);
                })
                .filter(m->validator.validate(m).isEmpty())
                .forEach(msgRepo::save);

        System.out.printf("There are now %s message(s) in the database.%n", msgRepo.findAll().size());
    }
}
