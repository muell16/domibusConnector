package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.controller.exception.DomainMatchingException;
import eu.domibus.connector.controller.routing.DCDomainRoutingManager;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.ecodex.dc5.message.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DC5LookupDomainStepTest {

    @Mock
    private DCBusinessDomainManager domainManager;
    @Mock
    private FindBusinessMessageByMsgId msgService;
    @Mock
    private DCDomainRoutingManager dcDomainRoutingManager;

    // 1. If the field refToMsgId exists (either in backend or ebms data) then -> lookup the referred msg and associate incoming message with that domain.
    @Test
    void given_a_message_that_refers_to_another_message_via_ebmsdata_then_the_domain_of_the_referred_message_is_used_when_we_try_to_associate_the_message_with_a_domain_and_we_can_find_the_message() {

        // Arrange
        final String id = String.valueOf(ThreadLocalRandom.current().nextInt());
        final DC5Message incomingMsg = DC5Message.builder().ebmsData(DC5Ebms.builder().refToEbmsMessageId(EbmsMessageId.ofRandom()).build()).build();
        final DC5Message stub = DC5Message.builder().businessDomainId(new DomibusConnectorBusinessDomain.BusinessDomainId(id)).build();
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.of(stub));

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act
        final DC5Message result = sut.lookupDomain(incomingMsg);

        // Assert
        assertThat(result.getBusinessDomainId().getBusinessDomainId()).isEqualTo(id);
    }

    @Test
    void given_a_message_that_refers_to_another_message_via_backenddata_then_the_domain_of_the_referred_message_is_used_when_we_try_to_associate_the_message_with_a_domain_and_we_can_find_the_message() {

        // Arrange
        final String id = String.valueOf(ThreadLocalRandom.current().nextInt());
        final DC5Message incomingMsg = DC5Message.builder().backendData(DC5BackendData.builder().refToBackendMessageId(BackendMessageId.ofRandom()).build()).build();
        final DC5Message stub = DC5Message.builder().businessDomainId(new DomibusConnectorBusinessDomain.BusinessDomainId(id)).build();
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.of(stub));

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act
        final DC5Message result = sut.lookupDomain(incomingMsg);

        // Assert
        assertThat(result.getBusinessDomainId().getBusinessDomainId()).isEqualTo(id);
    }

    // 2. If there are messages with the same ConversationID -> then associate messsage with domain of those messages.
    @Test
    void associate_an_incoming_message_that_has_a_conversation_id_with_domain_of_any_of_those_messages() {

        // Arrange
        final String id = String.valueOf(ThreadLocalRandom.current().nextInt());
        final DC5Message incomingMsg = DC5Message.builder().ebmsData(DC5Ebms.builder().conversationId("test-conversation-id").build()).build();
        final DC5Message stub = DC5Message.builder().businessDomainId(new DomibusConnectorBusinessDomain.BusinessDomainId(id)).build();
        when(msgService.findBusinessMsgByConversationId(any())).thenReturn(Collections.singletonList(stub));

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act
        final DC5Message result = sut.lookupDomain(incomingMsg);

        // Assert
        assertThat(result.getBusinessDomainId().getBusinessDomainId()).isEqualTo(id);
    }

    // 3. If there are domain routing rules then they should be applied.
    // TODO: needs routing rule
//    @Test
//    void if_none_of_the_above_apply_then_try_to_associate_an_incoming_message_with_a_domain_via_domain_routing_rules() {
//
//        // Arrange
//        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.empty());
//        when(msgService.findBusinessMsgByConversationId(any())).thenReturn(new ArrayList<>());
//
//        final String id1 = String.valueOf(ThreadLocalRandom.current().nextInt());
//        final String id2 = String.valueOf(ThreadLocalRandom.current().nextInt());
//        when(domainManager.getValidBusinessDomains()).thenReturn(Arrays.asList(new DomibusConnectorBusinessDomain.BusinessDomainId(id1), new DomibusConnectorBusinessDomain.BusinessDomainId(id2)));
//
//        final DomainRoutingRule rule1 = new DomainRoutingRule();
//        final DomainRoutingRule rule2 = new DomainRoutingRule();
//
//        when(dcDomainRoutingManager.getDomainRoutingRules(new DomibusConnectorBusinessDomain.BusinessDomainId(id1))).thenReturn();
//        when(dcDomainRoutingManager.getDomainRoutingRules(new DomibusConnectorBusinessDomain.BusinessDomainId(id2))).thenReturn();
//
//        final DC5Message incomingMsg = DC5Message.builder().build();
//        final DC5Message stub = DC5Message.builder().businessDomainId(new DomibusConnectorBusinessDomain.BusinessDomainId(id)).build();
//
//        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);
//
//        // Act
//        final DC5Message result = sut.lookupDomain(incomingMsg);
//
//        // Assert
//        assertThat(result.getBusinessDomainId().getBusinessDomainId()).isEqualTo(id);
//    }

    // 4. If the message can be associated with multiple domains (see step 3) => Error!
    // TODO: implement 4

    // 5. If in the end the message could not be associated with any domain   => Error!
    @Test
    void if_none_of_the_above_apply_then_try_to_associate_an_incoming_message_with_a_domain_via_domain_routing_rules() {

        // Arrange
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.empty());
        when(msgService.findBusinessMsgByConversationId(any())).thenReturn(new ArrayList<>());

        final String id1 = String.valueOf(ThreadLocalRandom.current().nextInt());
        when(domainManager.getValidBusinessDomains()).thenReturn(Arrays.asList(new DomibusConnectorBusinessDomain.BusinessDomainId(id1)));
        when(dcDomainRoutingManager.getDomainRoutingRules(new DomibusConnectorBusinessDomain.BusinessDomainId(id1))).thenReturn(new HashMap<>());
        final DC5Message incomingMsg = DC5Message.builder().ebmsData(DC5Ebms.builder().build()).build();
        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act & Assert
        assertThatExceptionOfType(DomainMatchingException.class).isThrownBy(() -> sut.lookupDomain(incomingMsg));
    }
}