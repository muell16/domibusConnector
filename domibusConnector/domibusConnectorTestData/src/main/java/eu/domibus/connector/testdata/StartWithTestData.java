package eu.domibus.connector.testdata;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Random;
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
    public void initDb(ContextRefreshedEvent event) {
//        Faker faker = new Faker();
        System.out.println("foo");

        final Random r = new Random();

        final String[] domains = new String[2];
        domains[0] = DC5BusinessDomain.DEFAULT_LANE_NAME;
        domains[1] = "other domain";

        final DC5EcxAddress home = DC5EcxAddress.builder()
                .ecxAddress("home:sweet:home").party(DC5Party.builder()
                        .partyId("Me")
                        .partyIdType("Home")
                        .build())
                .build();

        final DC5Partner homey = DC5Partner.builder()
                .partnerAddress(home)
                .partnerRole(
                        DC5Role.builder()
                                .role("homey")
                                .roleType(r.nextBoolean() ? DC5RoleType.RESPONDER : DC5RoleType.INITIATOR).build())
                .build();


        final DC5EcxAddress shire = DC5EcxAddress.builder()
                .ecxAddress("home:not:home")
                .party(DC5Party.builder()
                        .partyId("Hobbits")
                        .partyIdType("Shire")
                        .build())
                .build();

        final DC5Partner funnyHobbits = DC5Partner.builder()
                .partnerAddress(shire)
                .partnerRole(
                        DC5Role.builder()
                                .role("funny")
                                .roleType(r.nextBoolean() ? DC5RoleType.RESPONDER : DC5RoleType.INITIATOR).build())
                .build();


        IntStream.iterate(0, n -> n + 1)
                .limit(2000)
                .mapToObj(i -> {
                    final DC5Message message = DomainEntityCreator.createMessage();
                    message.setBusinessDomainId(DC5BusinessDomain.BusinessDomainId.of(domains[r.nextInt(domains.length)]));
                    message.setConnectorMessageId(DC5MessageId.ofRandom());
                    message.setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);

                    boolean dice = r.nextBoolean();
                    DC5Partner initiator = dice ? homey : funnyHobbits;
                    DC5Partner responder = dice ? funnyHobbits : homey;

                    message.setEbmsData(DC5Ebms.builder()
                            .ebmsMessageId(EbmsMessageId.ofString("ebmsId" + i))
                                    .initiator(initiator)
                                    .responder(responder)
                            .build());
                    return message;
                })
                .peek(m -> {
                    m.setMessageContent(DomainEntityCreator.createMessageContentWithDocumentWithNoPdfDocument());

                    m.getMessageContent().changeCurrentState(DC5BusinessMessageState.builder()
                            .event(DC5BusinessMessageState.BusinessMessageEvents.NEW_MSG)
                            .state(DC5BusinessMessageState.BusinessMessagesStates.CREATED)
                            .build());

                    m.getMessageContent().changeCurrentState(DC5BusinessMessageState.builder()
                            .event(DC5BusinessMessageState.BusinessMessageEvents.SUBMISSION_ACCEPTANCE_RCV)
                            .state(DC5BusinessMessageState.BusinessMessagesStates.SUBMITTED)
                            .build());

                    m.getMessageContent().changeCurrentState(DC5BusinessMessageState.builder()
                            .event(DC5BusinessMessageState.BusinessMessageEvents.DELIVERY_RCV)
                            .state(DC5BusinessMessageState.BusinessMessagesStates.DELIVERED)
                            .build()
                    );
                })
                .peek(m -> {
                    validator.validate(m).forEach(System.out::println);
                })
                .filter(m -> validator.validate(m).isEmpty())
                .forEach(msgRepo::save);

        System.out.printf("There are now %s message(s) in the database.%n", msgRepo.findAll().size());
    }
}
