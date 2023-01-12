package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.exception.DomainMatchingException;
import eu.domibus.connector.controller.routing.DCDomainRoutingManager;
import eu.domibus.connector.controller.routing.DomainRoutingRule;
import eu.domibus.connector.controller.routing.RoutingRulePattern;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.ecodex.dc5.message.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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

    @BeforeEach
    public void beforeEach() {
        CurrentBusinessDomain.setCurrentBusinessDomain(null);
    }

    // 1. If the field refToMsgId exists (either in backend or ebms data) then -> lookup the referred msg and associate incoming message with that domain.
    @Test
    void given_a_message_that_refers_to_another_message_via_ebmsdata_then_the_domain_of_the_referred_message_is_used_when_we_try_to_associate_the_message_with_a_domain_and_we_can_find_the_message() {

        // Arrange
        final String domainId = "test-id-1";
        final DC5Message incomingMsg = DC5Message.builder().ebmsData(DC5Ebms.builder().refToEbmsMessageId(EbmsMessageId.ofRandom()).build()).build();
        final DC5Message stub = DC5Message.builder().businessDomainId(new DC5BusinessDomain.BusinessDomainId(domainId)).build();
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.of(stub));

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act
        final DC5Message result = sut.lookupDomain(incomingMsg);

        // Assert
        assertThat(result.getBusinessDomainId().getBusinessDomainId()).isEqualTo(domainId);
    }

    @Test
    void given_a_message_that_refers_to_another_message_via_backenddata_then_the_domain_of_the_referred_message_is_used_when_we_try_to_associate_the_message_with_a_domain_and_we_can_find_the_message() {

        // Arrange
        final String domainId = "test-domainId-2";
        final DC5Message incomingMsg = DC5Message.builder().backendData(DC5BackendData.builder().refToBackendMessageId(BackendMessageId.ofRandom()).build()).build();
        final DC5Message stub = DC5Message.builder().businessDomainId(new DC5BusinessDomain.BusinessDomainId(domainId)).build();
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.of(stub));

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act
        final DC5Message result = sut.lookupDomain(incomingMsg);

        // Assert
        assertThat(result.getBusinessDomainId().getBusinessDomainId()).isEqualTo(domainId);
    }

    // 2. If there are messages with the same ConversationID -> then associate messsage with domain of those messages.
    @Test
    void associate_an_incoming_message_that_has_a_conversation_id_with_domain_of_those_messages() {

        // Arrange
        final String id = "test-domainId-3";
        final DC5Message incomingMsg = DC5Message.builder().ebmsData(DC5Ebms.builder().conversationId("test-conversation-id").build()).build();
        final DC5Message stub = DC5Message.builder().businessDomainId(new DC5BusinessDomain.BusinessDomainId(id)).build();
        when(msgService.findBusinessMsgByConversationId(any())).thenReturn(Collections.singletonList(stub));

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act
        final DC5Message result = sut.lookupDomain(incomingMsg);

        // Assert
        assertThat(result.getBusinessDomainId().getBusinessDomainId()).isEqualTo(id);
    }

    // 3. If there are domain routing rules then they should be applied.
    // TODO: needs routing rule
    @Test
    void if_neither_referred_msg_nor_conversation_id_indicate_a_domain_then_the_domain_routing_rules_should_apply() {

        // Arrange
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.empty());
//        when(msgService.findBusinessMsgByConversationId(any())).thenReturn(new ArrayList<>());

        final String matchingDomainId1 = "test-domainId-foo1";
        final String notMatchingDomainId2 = "test-domainId-bar2";
        when(domainManager.getValidDomains()).thenReturn(Arrays.asList(
                DC5BusinessDomain.builder()
                    .id(DC5BusinessDomain.BusinessDomainId.of(matchingDomainId1))
                    .build(),
                DC5BusinessDomain.builder()
                    .id(DC5BusinessDomain.BusinessDomainId.of(notMatchingDomainId2))
                    .build())
        );




        final String serviceName = "service-that-should-go-to-particular-domain";
        final String serviceType = "urn:e-codex:services:";

        final DomainRoutingRule rule1 = new DomainRoutingRule();
        final RoutingRulePattern routingRulePattern = new RoutingRulePattern("&(equals(ServiceName, " + "'" + serviceName + "'" +"),equals(ServiceType, " + "'" + serviceType + "'" + "))");
        rule1.setMatchClause(routingRulePattern);

        final Map<String, DomainRoutingRule> ruleIdRuleMap = new HashMap<>();
        ruleIdRuleMap.put(rule1.getRoutingRuleId(), rule1);

        when(dcDomainRoutingManager.getDomainRoutingRules(DC5BusinessDomain.BusinessDomainId.of(matchingDomainId1))).thenReturn(ruleIdRuleMap);
        when(dcDomainRoutingManager.getDomainRoutingRules(DC5BusinessDomain.BusinessDomainId.of(notMatchingDomainId2))).thenReturn(new HashMap<String, DomainRoutingRule>());

        final DC5Message incomingMsg = DC5Message.builder()
                .ebmsData(DC5Ebms.builder()
                        .service(DC5Service.builder().service(serviceName)
                        .serviceType(serviceType).build()).build())
                .build();

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act
        final DC5Message result = sut.lookupDomain(incomingMsg);

        // Assert
        assertThat(result.getBusinessDomainId()).isEqualTo(DC5BusinessDomain.BusinessDomainId.of(matchingDomainId1));
    }

    // 4. If the message can be associated with multiple domains (see step 3) => Error!
    @Test
    void if_multiple_domain_routing_rules_can_be_applied_then_a_particular_exception_should_be_thrown() {

        // Arrange
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.empty());
        when(msgService.findBusinessMsgByConversationId(any())).thenReturn(new ArrayList<>());

        final String testDomain1 = "test-domainId-11";
        final String testDomain2 = "test-domainId-22";
        when(domainManager.getValidDomains())
                .thenReturn(Arrays.asList(createDomain(testDomain1), createDomain(testDomain2)));


        final String serviceName = "service-that-should-go-to-particular-domain";
        final String serviceType = "urn:e-codex:services:";

        final DomainRoutingRule rule1 = new DomainRoutingRule();
        final DomainRoutingRule rule2 = new DomainRoutingRule();
        final RoutingRulePattern rulePattern1 = new RoutingRulePattern("&(equals(ServiceName, " + "'" + serviceName + "'" +"),equals(ServiceType, " + "'" + serviceType + "'" + "))");
        final RoutingRulePattern rulePattern2 = new RoutingRulePattern("&(equals(ServiceName, " + "'" + serviceName + "'" +"),equals(ServiceType, " + "'" + serviceType + "'" + "))");
        rule1.setMatchClause(rulePattern1);
        rule2.setMatchClause(rulePattern2);

        final Map<String, DomainRoutingRule> ruleIdRuleMap1 = new HashMap<>();
        ruleIdRuleMap1.put(rule1.getRoutingRuleId(), rule1);
        final Map<String, DomainRoutingRule> ruleIdRuleMap2 = new HashMap<>();
        ruleIdRuleMap2.put(rule2.getRoutingRuleId(), rule2);

        when(dcDomainRoutingManager.getDomainRoutingRules(new DC5BusinessDomain.BusinessDomainId(testDomain1))).thenReturn(ruleIdRuleMap1);
        when(dcDomainRoutingManager.getDomainRoutingRules(new DC5BusinessDomain.BusinessDomainId(testDomain2))).thenReturn(ruleIdRuleMap1);

        final DC5Message incomingMsg = DC5Message.builder()
                .ebmsData(DC5Ebms.builder()
                        .service(DC5Service.builder()
                                .service(serviceName)
                                .serviceType(serviceType)
                                .build())
                        .conversationId("test1")
                        .build())
                .build();

        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act & Assert
        assertThatExceptionOfType(DomainMatchingException.class)
                .as("There are 2 matching domains")
                .isThrownBy(() -> sut.lookupDomain(incomingMsg));
    }

    // 5. If in the end the message could not be associated with any domain   => Error!
    @Test
    void if_in_the_end_a_message_can_not_be_associated_with_a_domain_then_a_particular_exception_should_be_thrown() {

        // Arrange
        when(msgService.findBusinessMsgByRefToMsgId(any())).thenReturn(Optional.empty());
        when(msgService.findBusinessMsgByConversationId(any())).thenReturn(new ArrayList<>());

        final String nonMatchingDomainId1 = "foo";
        when(domainManager.getValidDomains())
                .thenReturn(Arrays.asList(createDomain(nonMatchingDomainId1)));
        when(dcDomainRoutingManager.getDomainRoutingRules(createDomain(nonMatchingDomainId1).getId())).thenReturn(new HashMap<>());
        final DC5Message incomingMsg = DC5Message.builder().ebmsData(DC5Ebms.builder()
                        .conversationId("test1")
                .build()).build();
        final DC5LookupDomainStep sut = new DC5LookupDomainStep(domainManager, msgService, dcDomainRoutingManager);

        // Act & Assert
        assertThatExceptionOfType(DomainMatchingException.class).isThrownBy(() -> sut.lookupDomain(incomingMsg));
    }

    private DC5BusinessDomain createDomain(String s) {
        return DC5BusinessDomain.builder()
                .id(DC5BusinessDomain.BusinessDomainId.of(s))
                .build();
    }
}