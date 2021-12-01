package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.routing.DCRoutingRulesManagerImpl;
import eu.domibus.connector.controller.routing.RoutingRule;
import eu.domibus.connector.controller.routing.RoutingRulePattern;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class LookupBackendNameStepTest {

    @Test
    void executeStep() {
        // Arrange
        final ConfigurationPropertyManagerService configMock = Mockito.mock(ConfigurationPropertyManagerService.class);
        final DCMessagePersistenceService peristenceMock = Mockito.mock(DCMessagePersistenceService.class);
        final DCRoutingRulesManagerImpl routingMock = Mockito.mock(DCRoutingRulesManagerImpl.class);

        Mockito.when(routingMock.isBackendRoutingEnabled(any())).thenReturn(true);

        final RoutingRule routingRule1 = new RoutingRule();
        routingRule1.setLinkName("fooLink");
        routingRule1.setMatchClause(new RoutingRulePattern("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))"));
        final HashMap<String, RoutingRule> routingRules = new HashMap<>();
        routingRules.put("foobarId", routingRule1);

        Mockito.when(routingMock.getBackendRoutingRules(any())).thenReturn(routingRules);

        final LookupBackendNameStep sut = new LookupBackendNameStep(routingMock, peristenceMock, configMock);

        final DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.getMessageDetails().setService(DomainEntityCreator.createServiceEPO());
//        message.getMessageDetails().setConnectorBackendClientName("EPO_backend");

        // Act
        sut.executeStep(message);

        // Assert
        assertThat(message.getMessageDetails().getConnectorBackendClientName()).isEqualTo("EPO_backend");
    }
}