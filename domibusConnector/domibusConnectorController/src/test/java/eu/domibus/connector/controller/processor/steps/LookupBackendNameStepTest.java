package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.routing.DCRoutingRulesManagerImpl;
import eu.domibus.connector.controller.routing.RoutingRule;
import eu.domibus.connector.controller.routing.RoutingRulePattern;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class LookupBackendNameStepTest {

    /*
     * 0) kommt eine nachricht rein wo schon ein backend name gesetzt ist dann darf der backend name nicht verändert werden
     * 1) kommt eine nachricht rein wo kein backend name gesetzt ist und es auch keine conv id gibt und backend routing disabled ist, dann default backend
     *
     * wenn es schon eine nachricht zur convid gibt
     * 2) lookup backend name step 47, wenn es schon eine conv id gitb, dann wird die nachricht geladen, und da steht ein backend drinnen, dann muss für alle nachrichten mit dieser conv id, das jeweilige backend verwendet werden
     * findBackendByConvId mocken
     *
     * 3) auswerten der regeln, test soll sich darauf verlassen, dass die rule engine funktioniert, priority, regel da oder nicht da, es gibt keine regel für einen bestimmten anwendungsfall -> default backend...
     *
     *
     * */

    @Test
    void given_a_message_without_a_specified_backend_and_no_conversation_id_when_backend_routing_is_enabled_but_not_route_matches_then_the_message_is_sent_to_the_default_backend() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final RoutingRule routingRule1 = new RoutingRule();
        routingRule1.setLinkName("fooLink");
        routingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, RoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", routingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(true);

        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.getMessageDetails().setService(DomainEntityCreator.createServiceEPO());
//        message.getMessageDetails().setConnectorBackendClientName("EPO_backend");

        // Act
        sut.executeStep(message);

        // Assert
        assertThat(message.getMessageDetails().getConnectorBackendClientName()).isEqualTo("DEFAULT_BACKEND");
    }

    @Test
    void given_a_message_without_a_specified_backend_and_no_conversation_id_when_backend_routing_is_disabled_then_the_message_is_sent_to_the_default_backend() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final RoutingRule routingRule1 = new RoutingRule();
        routingRule1.setLinkName("fooLink");
        routingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, RoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", routingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(false);

        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.getMessageDetails().setService(DomainEntityCreator.createServiceEPO());
//        message.getMessageDetails().setConnectorBackendClientName("EPO_backend");

        // Act
        sut.executeStep(message);

        // Assert
        assertThat(message.getMessageDetails().getConnectorBackendClientName()).isEqualTo("DEFAULT_BACKEND");
    }

    @Test
    void given_a_message_without_a_specified_backend_but_with_a_conversation_id_when_processed_then_the_message_is_sent_to_the_backend_that_was_configured_for_that_conversation_id() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);

        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        List<DomibusConnectorMessage> messagesByConversationId = new ArrayList<>();
        final DomibusConnectorMessage otherMessageWithConvId = DomainEntityCreator.createMessage();
        otherMessageWithConvId.getMessageDetails().setConversationId("fooConvId");
        otherMessageWithConvId.getMessageDetails().setConnectorBackendClientName("BACKEND_OF_ANOTHER_MSG_WITH_SAME_CONV_ID");
        messagesByConversationId.add(otherMessageWithConvId);
        Mockito.when(peristenceMock.findMessagesByConversationId(any())).thenReturn(messagesByConversationId);

        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final RoutingRule routingRule1 = new RoutingRule();
        routingRule1.setLinkName("fooLink");
        routingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, RoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", routingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(false);

        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.getMessageDetails().setService(DomainEntityCreator.createServiceEPO());

        // Act
        sut.executeStep(message);

        // Assert
        assertThat(message.getMessageDetails().getConnectorBackendClientName()).isEqualTo("BACKEND_OF_ANOTHER_MSG_WITH_SAME_CONV_ID");
    }

    @Test
    void given_a_message_without_a_specified_backend_when_backend_routing_is_enabled_and_a_rule_matches_then_the_message_is_sent_to_the_backend_associated_with_that_rule() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        final RoutingRule routingRule1 = new RoutingRule();
        routingRule1.setLinkName("BACKEND_ASSOCIATED_WITH_RULE");
        routingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, RoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", routingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);
        Mockito.when(routingMock.getDefaultBackendName(any())).thenReturn("DEFAULT_BACKEND");
        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(true);

        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.getMessageDetails().setAction(new DomibusConnectorAction("ConTest_Form"));
        message.getMessageDetails().setService(new DomibusConnectorService("Connector-TEST", "urn:e-codex:services:"));

        // Act
        sut.executeStep(message);

        // Assert
        assertThat(message.getMessageDetails().getConnectorBackendClientName()).isEqualTo("BACKEND_ASSOCIATED_WITH_RULE");
    }
}

