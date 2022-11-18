package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DC5Action;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Service;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutingRulePatternTest {

    @Test
    void matchesServiceName() {
        DC5Message epoMessage = DomainEntityCreator.createOutgoingEpoFormAMessage();
        DC5Service service = new DC5Service("serviceName", "serviceType");
        epoMessage.getEbmsData().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("equals(ServiceName, 'serviceName')");
        assertThat(pattern.matches(epoMessage)).isTrue();
    }

    @Test
    void matchesStartWithServiceName() {
        DC5Message epoMessage = DomainEntityCreator.createOutgoingEpoFormAMessage();
        DC5Service service = new DC5Service("serviceName", "serviceType");
        epoMessage.getEbmsData().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("startswith(ServiceName, 'serv')");
        assertThat(pattern.matches(epoMessage)).isTrue();
    }

    @Test
    void matchesStartWithServiceName_noMatch() {
        DC5Message epoMessage = DomainEntityCreator.createOutgoingEpoFormAMessage();
        DC5Service service = new DC5Service("serviceName", "serviceType");
        epoMessage.getEbmsData().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("startswith(ServiceName, 'aserv')");
        assertThat(pattern.matches(epoMessage)).isFalse();
    }

    @Test
    void matchesServiceName_shouldFail() {
        DC5Message epoMessage = DomainEntityCreator.createOutgoingEpoFormAMessage();
        DC5Service service = new DC5Service("serviceName", "serviceType");
        epoMessage.getEbmsData().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("equals(ServiceName, 'service')");
        assertThat(pattern.matches(epoMessage)).isFalse();
    }

    @Test
    void matchesServiceName_withAnd() {
        DC5Message epoMessage = DomainEntityCreator.createOutgoingEpoFormAMessage();
        DC5Service service = new DC5Service("serviceName", "s:ervice-Type");
        epoMessage.getEbmsData().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("&(equals(ServiceName, 'serviceName'),equals(ServiceType, 's:ervice-Type'))");
        assertThat(pattern.matches(epoMessage)).isTrue();
    }

    @Test
    void matchesServiceName_withOr() {
        DC5Message epoMessage = DomainEntityCreator.createOutgoingEpoFormAMessage();
        DC5Service service = new DC5Service("serviceName", "serviceType");
        epoMessage.getEbmsData().setService(service);

        RoutingRulePattern pattern = new RoutingRulePattern("|(equals(ServiceName, 'serviceName'),equals(ServiceName, 'serName'))");
        assertThat(pattern.matches(epoMessage)).isTrue();
    }

    @Test
    public void testAction() {

        DC5Message epoMessage = DomainEntityCreator.createOutgoingEpoFormAMessage();
        DC5Action action = new DC5Action("Connector-TEST");
        epoMessage.getEbmsData().setAction(action);

        RoutingRulePattern pattern = new RoutingRulePattern("equals(Action, 'Connector-TEST')");
        assertThat(pattern.matches(epoMessage)).isTrue();


    }



}