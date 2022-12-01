package eu.domibus.connector.controller.routing;

import eu.domibus.connector.common.annotations.MapNested;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.UUID;

@MapNested
@Getter
@Setter
public class DomainRoutingRule {

    public static int HIGH_PRIORITY = -2000;
    public static int LOW_PRIORITY = 2000;

    private ConfigurationSource configurationSource = ConfigurationSource.ENV;

    @NotNull
    private RoutingRulePattern matchClause;

    private String description = "";

    /**
     * higher numbers mean higher priority
     */
    private int priority = 0;

    private boolean deleted = false;

    private String routingRuleId = generateID();

    public static String generateID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainRoutingRule)) return false;

        DomainRoutingRule that = (DomainRoutingRule) o;

        return routingRuleId != null ? routingRuleId.equals(that.routingRuleId) : that.routingRuleId == null;
    }

    @Override
    public int hashCode() {
        return routingRuleId != null ? routingRuleId.hashCode() : 0;
    }

    public String toString() {
        return new ToStringCreator(this)
                .append("ruleId", routingRuleId)
                .append("priority", priority)
                .append("rule", matchClause)
                .toString();
    }

    // sorts by ascending priority, means 0 comes before -2000.
    public static Comparator<DomainRoutingRule> getComparator() {
        return (r1, r2) -> Integer.compare(r2.priority, r1.priority);
    }
}
