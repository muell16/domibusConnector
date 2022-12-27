package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.routing.DCRoutingRulesManagerImpl;
import eu.domibus.connector.controller.routing.LinkPartnerRoutingRule;
import eu.domibus.connector.controller.routing.RoutingRulePattern;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.DC5Action;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Service;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class LookupBackendNameStepTest {


    //regel 1: ist ein backend in der msg gesetzt? (dann lasse es wie es ist)
    @Test
    void given_a_message_that_has_a_specified_backend_then_the_message_should_be_sent_there() {
        DomibusConnectorLinkPartner.LinkPartnerName backendName = DomibusConnectorLinkPartner.LinkPartnerName.of("BACKEND_ON_THE_MESSAGE");
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final LinkPartnerRoutingRule linkPartnerRoutingRule1 = new LinkPartnerRoutingRule();
        linkPartnerRoutingRule1.setLinkName("fooLink");
        linkPartnerRoutingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, LinkPartnerRoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", linkPartnerRoutingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(true);

//        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DC5Message message = DomainEntityCreator.createMessage();
        message.getEbmsData().setService(DomainEntityCreator.createServiceEPO());
        message.setBackendLinkName(backendName);

        // Act
//        sut.executeStep(message);

        // Assert
        assertThat(message.getBackendLinkName()).isEqualTo(backendName);
    }


    //regel4: setze das default backend
    @Test
    void given_a_message_without_a_specified_backend_and_no_conversation_id_when_backend_routing_is_enabled_but_not_route_matches_then_the_message_is_sent_to_the_default_backend() {
        DomibusConnectorLinkPartner.LinkPartnerName backendName = DomibusConnectorLinkPartner.LinkPartnerName.of("DEFAULT_BACKEND");
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final LinkPartnerRoutingRule linkPartnerRoutingRule1 = new LinkPartnerRoutingRule();
        linkPartnerRoutingRule1.setLinkName("fooLink");
        linkPartnerRoutingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, LinkPartnerRoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", linkPartnerRoutingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn(backendName.getLinkName());
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(true);

//        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DC5Message message = DomainEntityCreator.createMessage();
        message.getEbmsData().setService(DomainEntityCreator.createServiceEPO());
//        message.getMessageDetails().setConnectorBackendClientName("EPO_backend");

        // Act
//        sut.executeStep(message);

        // Assert
        assertThat(message.getBackendLinkName()).isEqualTo(backendName);
    }

    //regel4: setze das default backend
    @Test
    void given_a_message_without_a_specified_backend_and_no_conversation_id_when_backend_routing_is_disabled_then_the_message_is_sent_to_the_default_backend() {
        DomibusConnectorLinkPartner.LinkPartnerName backendName = DomibusConnectorLinkPartner.LinkPartnerName.of("DEFAULT_BACKEND");
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final LinkPartnerRoutingRule linkPartnerRoutingRule1 = new LinkPartnerRoutingRule();
        linkPartnerRoutingRule1.setLinkName("fooLink");
        linkPartnerRoutingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, LinkPartnerRoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", linkPartnerRoutingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn(backendName.getLinkName());
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(false);

//        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DC5Message message = DomainEntityCreator.createMessage();
        message.getEbmsData().setService(DomainEntityCreator.createServiceEPO());
//        message.getMessageDetails().setConnectorBackendClientName("EPO_backend");

        // Act
//        sut.executeStep(message);

        // Assert
        assertThat(message.getBackendLinkName()).isEqualTo(backendName);
    }

    //regel2: gibt es eine Nachricht mit der gleichen ConversationId, die ein Backend gesetzt hat? Wenn ja nimm das.
    @Test
    void given_a_message_without_a_specified_backend_but_with_a_conversation_id_when_processed_then_the_message_is_sent_to_the_backend_that_was_configured_for_that_conversation_id() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);

        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        List<DC5Message> messagesByConversationId = new ArrayList<>();
        final DC5Message otherMessageWithConvId = DomainEntityCreator.createMessage();
        otherMessageWithConvId.getEbmsData().setConversationId("fooConvId");
        otherMessageWithConvId.setBackendLinkName(DomibusConnectorLinkPartner.LinkPartnerName.of("BACKEND_OF_ANOTHER_MSG_WITH_SAME_CONV_ID"));
        messagesByConversationId.add(otherMessageWithConvId);
        Mockito.when(peristenceMock.findMessagesByConversationId(any())).thenReturn(messagesByConversationId);

        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final LinkPartnerRoutingRule linkPartnerRoutingRule1 = new LinkPartnerRoutingRule();
        linkPartnerRoutingRule1.setLinkName("fooLink");
        linkPartnerRoutingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, LinkPartnerRoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", linkPartnerRoutingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(false);

//        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DC5Message message = DomainEntityCreator.createMessage();
        message.getEbmsData().setService(DomainEntityCreator.createServiceEPO());

        // Act
//        sut.executeStep(message);

        // Assert
        assertThat(message.getBackendLinkName()).isEqualTo(DomibusConnectorLinkPartner.LinkPartnerName.of("BACKEND_OF_ANOTHER_MSG_WITH_SAME_CONV_ID"));
    }

    //regel3: Werte die Routing Rules aus, die erste  Routing Rule die matched gewinnt. Matching geht nach routing rule priorität.
    @Test
    void given_a_message_without_a_specified_backend_when_backend_routing_is_enabled_and_a_rule_matches_then_the_message_is_sent_to_the_backend_associated_with_that_rule() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final LinkPartnerRoutingRule linkPartnerRoutingRule1 = new LinkPartnerRoutingRule();
        linkPartnerRoutingRule1.setLinkName("BACKEND_ASSOCIATED_WITH_RULE");
        linkPartnerRoutingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, LinkPartnerRoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", linkPartnerRoutingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(true);

//        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DC5Message message = DomainEntityCreator.createMessage();
        message.getEbmsData().setAction(DC5Action.builder().action("ConTest_Form").build());
        message.getEbmsData().setService(DC5Service.builder().service("Connector-TEST").serviceType("urn:e-codex:services:").build());

        // Act
//        sut.executeStep(message);

        // Assert
        assertThat(message.getBackendLinkName()).isEqualTo(DomibusConnectorLinkPartner.LinkPartnerName.of("BACKEND_ASSOCIATED_WITH_RULE"));
    }

    //regel3: Werte die Routing Rules aus, die erste  Routing Rule die matched gewinnt. Matching geht nach routing rule priorität.
    @Test
    @DisplayName("given_a_message_without_a_specified_backend_when_backend_routing_is_enabled_and_a_rule_matches_then_the_message_is_sent_to_the_backend_associated_with_that_rule")
    void given_a_message_without_a_specified_backend_when_backend_routing_is_enabled_and_multiple_matching_rules_then_the_message_is_sent_to_the_backend_associated_with_that_rule() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final HashMap<String, LinkPartnerRoutingRule> routingRules = new HashMap<>();
        final LinkPartnerRoutingRule linkPartnerRoutingRule1 = new LinkPartnerRoutingRule();
        linkPartnerRoutingRule1.setLinkName("BACKEND_ASSOCIATED_WITH_RULE_LOWER_PRIORITY");
        linkPartnerRoutingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        linkPartnerRoutingRule1.setPriority(-2000);
        routingRules.put("rule1", linkPartnerRoutingRule1);

        final LinkPartnerRoutingRule linkPartnerRoutingRule2 = new LinkPartnerRoutingRule();
        linkPartnerRoutingRule2.setLinkName("BACKEND_ASSOCIATED_WITH_RULE_HIGHER_PRIORITY");
        linkPartnerRoutingRule2.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        linkPartnerRoutingRule2.setPriority(0);
        routingRules.put("rule2", linkPartnerRoutingRule2);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(true);

//        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DC5Message message = DomainEntityCreator.createMessage();
        message.getEbmsData().setAction(DC5Action.builder().action("ConTest_Form").build());
        message.getEbmsData().setService(DC5Service.builder().service("Connector-TEST").serviceType("urn:e-codex:services:").build());

        // Act
//        sut.executeStep(message);

        // Assert
        assertThat(message.getBackendLinkName()).isEqualTo(DomibusConnectorLinkPartner.LinkPartnerName.of("BACKEND_ASSOCIATED_WITH_RULE_HIGHER_PRIORITY"));
    }
}

