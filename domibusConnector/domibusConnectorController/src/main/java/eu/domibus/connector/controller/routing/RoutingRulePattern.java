package eu.domibus.connector.controller.routing;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 *
 * The routing rule grammar:
 * {@literal
##BNF RoutingRulePattern
<ROUTING_RULE_PATTERN> ::= <OPERAND>(<ROUTING_RULE_PATTERN>, <ROUTING_RULE_PATTERN>) | <BOOLEAN_EXPRESSION>
<BOOLEAN_EXPRESSION> ::= equals(<AS4_TYPE>, '<VALUE>')
<OPERAND> ::= "&" | "|"
<AS4_TYPE> ::= ServiceType | ServiceName
<VALUE> ::= <VALUE><LETTER> | <LETTER>
<LETTER> can be every letter except ' (single quote) and \ (backslash)
##BNF}
 *
 */
public class RoutingRulePattern {

    public static final String SERVICE_TYPE = "ServiceType";
    public static final String SERVICE_NAME = "ServiceName";
    public static final List<String> operands = Stream.of("&", "|", "equals").collect(Collectors.toList());
//    public static final List<String> expressions = Stream.of("equals").collect(Collectors.toList());

    private String pattern;

    public RoutingRulePattern(String pattern) {
        createMatcher(pattern);
    }

    private void createMatcher(final String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            throw new IllegalArgumentException("Pattern is not allowed to be empty or null!");
        }
        this.pattern = pattern;

        String parsing = pattern.trim();
        for (String o : operands) {
            if (parsing.startsWith(o)) {

            }
        }
    }

    public boolean matches(DomibusConnectorMessage message) {
        return false;
    }

    private static abstract class BooleanExpression {
        abstract boolean evaluate(DomibusConnectorMessage message);
    }

    private static class EqualsExpression extends BooleanExpression {

        @Override
        boolean evaluate(DomibusConnectorMessage message) {
            return false;
        }
    }

}
