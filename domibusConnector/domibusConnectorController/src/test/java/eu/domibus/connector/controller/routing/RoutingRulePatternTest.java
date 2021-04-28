package eu.domibus.connector.controller.routing;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoutingRulePatternTest {

    @Test
    void matchesServiceName() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        DomibusConnectorService service = new DomibusConnectorService("serviceName", "serviceType");
        epoMessage.getMessageDetails().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("equals(ServiceName, 'serviceName')");
        assertThat(pattern.matches(epoMessage)).isTrue();
    }

    @Test
    void matchesServiceName_shouldFail() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        DomibusConnectorService service = new DomibusConnectorService("serviceName", "serviceType");
        epoMessage.getMessageDetails().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("equals(ServiceName, 'service')");
        assertThat(pattern.matches(epoMessage)).isFalse();
    }

    @Test
    void matchesServiceName_withAnd() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        DomibusConnectorService service = new DomibusConnectorService("serviceName", "serviceType");
        epoMessage.getMessageDetails().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("&(equals(ServiceName, 'serviceName'),equals(ServiceName, 'serviceName'))");
        assertThat(pattern.matches(epoMessage)).isTrue();
    }

    @Test
    void matchesServiceName_withOr() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        DomibusConnectorService service = new DomibusConnectorService("serviceName", "serviceType");
        epoMessage.getMessageDetails().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("|(equals(ServiceName, 'serviceName'),equals(ServiceName, 'serName'))");
        assertThat(pattern.matches(epoMessage)).isTrue();
    }


}