package eu.domibus.connector.controller.routing;

import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;

@SpringBootTest(classes = {ITCaseTestContext.class},
        properties = {
                "spring.jta.enabled=false"
//                "logging.level.eu.domibus=TRACE"
        }
)
@Commit
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test"})
@Disabled
public class DCLinkPartnerRoutingRulesManagerImplTest {

    @Autowired
    private DCRoutingRulesManager routingRulesManager;



    @Test
    void addBackendRoutingRule() {
        LinkPartnerRoutingRule rr = new LinkPartnerRoutingRule();
        rr.setRoutingRuleId("abc1");
        rr.setConfigurationSource(ConfigurationSource.ENV);
        rr.setLinkName("backend_bob");
        rr.setMatchClause(new RoutingRulePattern("equals(ServiceName, 'test')"));

        routingRulesManager.addBackendRoutingRule(DC5BusinessDomain.getDefaultBusinessDomainId(), rr);

        
    }

    @Test
    @Disabled("not implemented yet")
    void getBackendRoutingRules() {
    }

    @Test
    @Disabled("not implemented yet")
    void getDefaultBackendName() {
    }

    @Test
    @Disabled("not implemented yet")
    void isBackendRoutingEnabled() {
    }


}