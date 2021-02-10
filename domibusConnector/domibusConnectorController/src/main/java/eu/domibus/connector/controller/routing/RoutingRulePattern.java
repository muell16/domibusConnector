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
        parse(parsing);
    }

    BooleanExpression parse(String parsing) {
        for (String o : operands) {
            if (parsing.startsWith(o)) {
                parsing = parsing.substring(o.length());
            }
        }
        return null;
    }

    public boolean matches(DomibusConnectorMessage message) {
        return false;
    }

    private static abstract class BooleanExpression {
        abstract boolean evaluate(DomibusConnectorMessage message);
        abstract List<String> canHandle();
    }

    private static class AndOrExpression extends BooleanExpression {

        private final String operand;
        private final BooleanExpression exp1;
        private final BooleanExpression exp2;

        public AndOrExpression(String operand, BooleanExpression exp1, BooleanExpression exp2) {
            this.operand = operand;
            this.exp1 = exp1;
            this.exp2 = exp2;
        }

        @Override
        boolean evaluate(DomibusConnectorMessage message) {
            return false;
        }

        @Override
        List<String> canHandle() {
            return Stream.of("&", "|").collect(Collectors.toList());
        }
    }

    private static class EqualsExpression extends BooleanExpression {

        @Override
        boolean evaluate(DomibusConnectorMessage message) {
            return false;
        }

        @Override
        List<String> canHandle() {
            return Stream.of("equals").collect(Collectors.toList());
        }
    }

}
