package eu.domibus.connector.controller.processor;

import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.flow.steps.MessageConfirmationStep;
import eu.ecodex.dc5.message.ConfirmationCreatorService;
import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.message.model.*;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ITCaseTestContext.class},
        properties = {"connector.controller.evidence.timeoutActive=false", //deactivate the evidence timeout checking timer job during this test
                "processing.send-generated-evidences-to-backend=false", "spring.jta.enabled=false"

        }
)
@Commit
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test", "flow-test"})
@Disabled
public class EvidenceMessageProcessorTest {


//    @Autowired
//    ConfirmationMessageProcessingFlow evidenceMessageProcessor;

    @Autowired
    ITCaseTestContext.QueueBasedDomibusConnectorBackendDeliveryService backendDeliveryService;

    @Autowired
    ITCaseTestContext.QueueBasedDomibusConnectorGatewaySubmissionService gwService;

//    @Autowired
//    ITCaseTestContext.QueueBasedSubmitToLinkService submitToLinkService;

    @Autowired
    ConfirmationCreatorService confirmationCreatorService;

    @Autowired
    MessageConfirmationStep messageConfirmationStep;

//    @Autowired
//    DCMessagePersistenceService messagePersistenceService;

    @Autowired
    TransactionTemplate txTemplate;

//    @MockBean(answer = Answers.CALLS_REAL_METHODS, reset = MockReset.AFTER)
//    ConfigurationPropertyLoaderServiceImpl configurationPropertyLoaderService;

    @Test
    @Timeout(30)
    public void testDeliverTrigger() throws InterruptedException {
        EbmsMessageId EBMSID = EbmsMessageId.ofString("testDeliverTrigger_1");

        //send trigger to evidenceMessageProcessor...
        try {
            //set domain to DefaultDomain
            CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());

            //prepare test message in DB
            DC5Message businessMsg = DomainEntityCreator.createOutgoingEpoFormAMessage();
            businessMsg.setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
            businessMsg.getEbmsData().setEbmsMessageId(EBMSID);
            businessMsg.setConnectorMessageId(DomibusConnectorMessageId.ofRandom());
            businessMsg.getBackendData().setBackendMessageId(BackendMessageId.ofRandom());
            businessMsg.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
//            messagePersistenceService.persistBusinessMessageIntoDatabase(businessMsg);

            DC5Confirmation submissionAcc = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, businessMsg, null, "");
//            businessMsg.addRelatedMessageConfirmation(submissionAcc);
            messageConfirmationStep.processConfirmationForMessage(businessMsg, submissionAcc);

            DC5Confirmation relayRemd = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, businessMsg, null, "");
//            businessMsg.addRelatedMessageConfirmation(relayRemd);
            messageConfirmationStep.processConfirmationForMessage(businessMsg, relayRemd);

            txTemplate.execute((t) -> {

//                DC5Message deliveryTrigger = DomibusConnectorMessageBuilder.createBuilder()
//                        .setConnectorMessageId(UUID.randomUUID().toString())
//                        .setMessageDetails(DomibusConnectorMessageDetailsBuilder.create()
//                                .withRefToMessageId(EBMSID)
//                                .build())
//                        .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder.createBuilder()
//                                .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
//                                .build())
//                        .build();
//                deliveryTrigger.getEbmsData().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//                deliveryTrigger.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());

//                evidenceMessageProcessor.processMessage(deliveryTrigger);
                return null;
            });
        } finally {
            CurrentBusinessDomain.setCurrentBusinessDomain(null);
        }

        DC5Message m1 = backendDeliveryService.toBackendDeliveredMessages.poll(10, TimeUnit.SECONDS);
        DC5Message m2 = gwService.toGatewayDeliveredMessages.poll(10, TimeUnit.SECONDS);

        Assertions.assertAll(
                () -> assertThat(backendDeliveryService.toBackendDeliveredMessages).as("Queue must be emtpy").hasSize(0),
                () -> assertThat(gwService.toGatewayDeliveredMessages).as("Queue must be emtpy").hasSize(0),
                () -> assertThat(m1).as("No message should be sent to backend!").isNull(), //no message transported to backend
                () -> assertThat(m2).as("There should be a confirmation message sent to gw!").isNotNull() //evidence trigger transported to gw
        );
    }

    @Test
    @Timeout(30)
    public void testDeliverTrigger_evidenceShouldBeSentBack() throws InterruptedException {
        EbmsMessageId EBMSID = EbmsMessageId.ofString("testDeliverTrigger_evidenceShouldBeSentBack_1");

        DomibusConnectorBusinessDomain.BusinessDomainId domain = new DomibusConnectorBusinessDomain.BusinessDomainId();
        domain.setBusinessDomainId("lane1");
        //send trigger to evidenceMessageProcessor...
        try {
            //set domain to DefaultDomain
            CurrentBusinessDomain.setCurrentBusinessDomain(domain);

            //prepare test message in DB
            DC5Message businessMsg = DomainEntityCreator.createOutgoingEpoFormAMessage();
//            businessMsg.getEbmsData().setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
            businessMsg.getEbmsData().setEbmsMessageId(EBMSID);
            businessMsg.setConnectorMessageId(DomibusConnectorMessageId.ofRandom());
//            businessMsg.getEbmsData().setBackendMessageId(UUID.randomUUID().toString());
            businessMsg.setMessageLaneId(domain);
//            messagePersistenceService.persistBusinessMessageIntoDatabase(businessMsg);

            DC5Confirmation submissionAcc = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, businessMsg, null, "");
//            businessMsg.addRelatedMessageConfirmation(submissionAcc);
            messageConfirmationStep.processConfirmationForMessage(businessMsg, submissionAcc);

            DC5Confirmation relayRemd = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, businessMsg, null, "");
//            businessMsg.addRelatedMessageConfirmation(relayRemd);
            messageConfirmationStep.processConfirmationForMessage(businessMsg, relayRemd);

            txTemplate.execute((t) -> {

//                DC5Message deliveryTrigger = DomibusConnectorMessageBuilder.createBuilder()
//                        .setConnectorMessageId(UUID.randomUUID().toString())
//                        .setMessageDetails(DomibusConnectorMessageDetailsBuilder.create()
//                                .withRefToMessageId(EBMSID)
//                                .build())
//                        .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder.createBuilder()
//                                .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
//                                .build())
//                        .build();
//                deliveryTrigger.getEbmsData().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//                deliveryTrigger.setMessageLaneId(domain);

//                evidenceMessageProcessor.processMessage(deliveryTrigger);
                return null;
            });
        } finally {
            CurrentBusinessDomain.setCurrentBusinessDomain(null);
        }

        DC5Message m1 = backendDeliveryService.toBackendDeliveredMessages.poll(10, TimeUnit.SECONDS);
        DC5Message m2 = gwService.toGatewayDeliveredMessages.poll(10, TimeUnit.SECONDS);

        Assertions.assertAll(
                () -> assertThat(backendDeliveryService.toBackendDeliveredMessages).as("Queue must be emtpy").hasSize(0),
                () -> assertThat(gwService.toGatewayDeliveredMessages).as("Queue must be emtpy").hasSize(0),
                () -> assertThat(m1).as("There should be a confirmation message sent to  backend!").isNotNull(), //a message transported to backend
                () -> assertThat(m2).as("There should be a confirmation message sent to gw!").isNotNull() //evidence trigger transported to gw
        );
    }


}