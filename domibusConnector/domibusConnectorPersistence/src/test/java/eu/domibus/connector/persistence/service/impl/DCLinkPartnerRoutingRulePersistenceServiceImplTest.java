package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.controller.routing.LinkPartnerRoutingRule;
import eu.domibus.connector.controller.routing.RoutingRulePattern;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.domibus.connector.persistence.service.DCRoutingRulePersistenceService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CommonPersistenceTest

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@Disabled
public class DCLinkPartnerRoutingRulePersistenceServiceImplTest {

    @Autowired
    DCRoutingRulePersistenceService routingRulePersistenceService;

    @Test
    @Order(100)
    void createRoutingRule() {

        LinkPartnerRoutingRule rr = new LinkPartnerRoutingRule();
        rr.setRoutingRuleId("abc");
        rr.setLinkName("backend_bob");
        rr.setMatchClause(new RoutingRulePattern("equals(ServiceName, 'test')"));
        routingRulePersistenceService.createRoutingRule(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), rr);
    }



    @Test
    void updateRoutingRule() {
        LinkPartnerRoutingRule rr = new LinkPartnerRoutingRule();
        rr.setRoutingRuleId("abcd");
        rr.setLinkName("backend_bob");
        rr.setMatchClause(new RoutingRulePattern("equals(ServiceName, 'test')"));
        routingRulePersistenceService.createRoutingRule(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), rr);

        List<LinkPartnerRoutingRule> allLinkPartnerRoutingRules = routingRulePersistenceService.getAllRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
        LinkPartnerRoutingRule abcd = allLinkPartnerRoutingRules.stream().filter(r -> r.getRoutingRuleId().equals("abcd"))
                .findFirst()
                .get();
        assertThat(abcd.getLinkName()).isEqualTo("backend_bob");

        LinkPartnerRoutingRule rrU = new LinkPartnerRoutingRule();
        rrU.setRoutingRuleId("abcd");
        rrU.setLinkName("backend_alice");
        rrU.setMatchClause(new RoutingRulePattern("equals(ServiceName, 'test')"));

        routingRulePersistenceService.updateRoutingRule(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), rrU);

        allLinkPartnerRoutingRules = routingRulePersistenceService.getAllRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
        abcd = allLinkPartnerRoutingRules.stream().filter(r -> r.getRoutingRuleId().equals("abcd"))
                .findFirst()
                .get();
        assertThat(abcd.getLinkName()).isEqualTo("backend_alice");
    }

    @Test
    @Order(200)
    void getAllRoutingRules() {
        List<LinkPartnerRoutingRule> allLinkPartnerRoutingRules = routingRulePersistenceService.getAllRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
        assertThat(allLinkPartnerRoutingRules).hasSize(1);
    }

    @Test
    @Order(300)
    void deleteRoutingRule() {
        LinkPartnerRoutingRule rr = new LinkPartnerRoutingRule();
        rr.setRoutingRuleId("xyz");
        rr.setLinkName("backend_bob");
        rr.setMatchClause(new RoutingRulePattern("equals(ServiceName, 'test')"));
        routingRulePersistenceService.createRoutingRule(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), rr);

        List<LinkPartnerRoutingRule> allLinkPartnerRoutingRules = routingRulePersistenceService.getAllRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
        assertThat(allLinkPartnerRoutingRules).hasSize(2);

        routingRulePersistenceService.deleteRoutingRule(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), "xyz");

        allLinkPartnerRoutingRules = routingRulePersistenceService.getAllRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
        assertThat(allLinkPartnerRoutingRules).hasSize(1);
    }
}