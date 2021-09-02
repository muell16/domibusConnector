package eu.domibus.connector.controller.routing;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import org.springframework.core.style.ToStringCreator;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class RoutingRule {

    public static int HIGH_PRIORITY = -2000;
    public static int LOW_PRIORITY = 2000;

    private ConfigurationSource configurationSource = ConfigurationSource.ENV;

    @NotBlank
    private String linkName;

    @NotBlank
    private RoutingRulePattern matchClause;

    private String description;

    /**
     * higher numbers mean higher priority
     */
    private int priority = 0;

    private String routingRuleId = UUID.randomUUID().toString().substring(0, 8);

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public RoutingRulePattern getMatchClause() {
        return matchClause;
    }

    public void setMatchClause(RoutingRulePattern matchClause) {
        this.matchClause = matchClause;
    }

    public ConfigurationSource getConfigurationSource() {
        return configurationSource;
    }

    public void setConfigurationSource(ConfigurationSource configurationSource) {
        this.configurationSource = configurationSource;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoutingRuleId() {
        return routingRuleId;
    }

    public void setRoutingRuleId(String routingRuleId) {
        this.routingRuleId = routingRuleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoutingRule)) return false;

        RoutingRule that = (RoutingRule) o;

        return routingRuleId != null ? routingRuleId.equals(that.routingRuleId) : that.routingRuleId == null;
    }

    @Override
    public int hashCode() {
        return routingRuleId != null ? routingRuleId.hashCode() : 0;
    }

    public String toString() {
        return new ToStringCreator(this)
                .append("ruleId", routingRuleId)
                .append("linkPartnerName", linkName)
                .append("priority", priority)
                .append("rule", matchClause)
                .toString();
    }
}