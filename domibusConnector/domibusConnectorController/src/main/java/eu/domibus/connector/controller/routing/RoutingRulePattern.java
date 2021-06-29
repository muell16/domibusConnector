package eu.domibus.connector.controller.routing;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * The routing rule grammar:
 * {@literal
##BNF RoutingRulePattern
tag::BNF[]
<ROUTING_RULE_PATTERN> ::= <BOOLEAN_EXPRESSION> | <COMPARE_EXPRESSION>
<BOOLEAN_EXPRESSION> ::= <OPERAND>(<ROUTING_RULE_PATTERN>, <ROUTING_RULE_PATTERN>)
<COMPARE_EXPRESSION> ::= equals(<AS4_TYPE>, '<VALUE>') | startswith(<AS4_TYPE>, '<VALUE>')
<OPERAND> ::= "&" | "|"
<AS4_TYPE> ::= ServiceType | ServiceName | FinalRecipient | Action
<VALUE> ::= <VALUE><LETTER> | <LETTER>
<LETTER> can be every letter [a-z][A-Z][0-9] other printable characters might work, but they untested! ['\|&)( will definitiv not work!]

end::BNF[]
##BNF}
 *
 */
public class RoutingRulePattern {

    private static final Logger LOGGER = LogManager.getLogger(RoutingRulePattern.class);

    private final String matchRule;

    private Expression expression;

    private enum Token {
        AS4_SERVICE_TYPE("ServiceType"),
        AS4_SERVICE_NAME("ServiceName"),
        AS4_FINAL_RECIPIENT("FinalRecipient"),
        AS4_FROM_PARTY_ID("FromPartyId"),
        AS4_FROM_PARTY_ID_TYPE("FromPartyIdType"),
        AS4_FROM_PARTY_ROLE("FromPartyRole"),
        AS4_ACTION("Action"),
        OR("\\|"),
        AND("&"),
        EQUALS("equals"),
        STARTSWITH("startswith"),
        SEMICOLON(","),
        BRACKET_OPEN("\\("),
        BRACKET_CLOSE("\\)"),
        WHITESPACE("\\p{javaWhitespace}"),
        VALUE("'[\\w:_\\-~\\./#\\?]+'");

        private final Pattern pattern;

        private Token(String regex) {
            this.pattern = Pattern.compile("^" + regex); //match from beginning
        }

        public int lastMatchingCharacter(String s) {
            Matcher m = pattern.matcher(s);

            if (m.find()) {
                return m.end();
            }
            return -1;
        }

    }

    private static class TokenAndValue {
        Token t;
        String value;
    }




    public RoutingRulePattern(String pattern) {
        this.matchRule = pattern;
        createMatcher(pattern);
    }


    private String pattern;

    private void createMatcher(final String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            throw new IllegalArgumentException("Pattern is not allowed to be empty or null!");
        }
        this.pattern = pattern;

        StringBuilder parsing = new StringBuilder();
        parsing.append(pattern.trim());

        LinkedList<TokenAndValue> tokens = new LinkedList<>();
        TokenAndValue t = getToken(parsing);
        while( t != null) {
            if (t.t != Token.WHITESPACE) {
                tokens.add(t);
            }
            t = getToken(parsing);
        }

        //convert list of tokens into matchers
        this.expression = parseExpression(tokens);
    }

    private Expression parseExpression(LinkedList<TokenAndValue> tokens) {
        Expression exp = null;
        Token t;
        TokenAndValue tv = tokens.removeFirst();
        if (tv == null) {
            throw new RuntimeException("Parsing error, token is null!");
        }
        t = tv.t;
//        while (t != null) {
            if (t == Token.OR || t == Token.AND) {
                //parsing BOOLEAN_EXPRESSION
                exp = parseBooleanExpression(t, tokens);
            } else if (t == Token.EQUALS || t == Token.STARTSWITH) {
                exp = parseCompareExpression(t, tokens);
            } else {
                throw new RuntimeException("Parsing error!");
            }
//        }
        return exp;
    }

    private Expression parseCompareExpression(Token t, LinkedList<TokenAndValue> tokens) {
        if (isNotToken(tokens.removeFirst(), Token.BRACKET_OPEN)) {
            throw new RuntimeException(String.format("Parse error, after '%s' must follow a '%s'", Token.EQUALS, Token.BRACKET_OPEN));
        }
        TokenAndValue as4Attribute = tokens.removeFirst();
        if (isNotToken(as4Attribute, Token.AS4_SERVICE_NAME) && isNotToken(as4Attribute, Token.AS4_SERVICE_TYPE)) {
            throw new RuntimeException(String.format("Parse error, '%s' or '%s' is expected", Token.AS4_SERVICE_NAME, Token.AS4_SERVICE_TYPE));
        }

        if (isNotToken(tokens.removeFirst(), Token.SEMICOLON)) {
            throw new RuntimeException(String.format("Parse error, '%s' is expected", Token.SEMICOLON));
        }
        TokenAndValue stringValue = tokens.removeFirst();
        if (stringValue == null) {
            throw new RuntimeException("No value provided!");
        }
        String valueString = stringValue.value.substring(1, stringValue.value.length() - 1); //remove leading ' and trailing '
        Expression exp;
        if (t == Token.EQUALS) {
            exp = new EqualsExpression(as4Attribute.t, valueString);
        } else if (t == Token.STARTSWITH) {
            exp = new StartsWithExpression(as4Attribute.t, valueString);
        } else {
            throw new RuntimeException(String.format("Parse error, illegal Token '%s'", t));
        }
        if (isNotToken(tokens.removeFirst(), Token.BRACKET_CLOSE)) {
            throw new RuntimeException(String.format("Parse error, missing closing '%s'", Token.BRACKET_CLOSE));
        }
        return exp;
    }

    private Expression parseBooleanExpression(Token t, LinkedList<TokenAndValue> tokens) {
        if (isNotToken(tokens.removeFirst(), Token.BRACKET_OPEN)) {
            throw new RuntimeException(String.format("Parse error, after '%s' must follow a '%s'", t, Token.BRACKET_OPEN));
        }
        Expression exp1 = parseExpression(tokens);
        if (isNotToken(tokens.removeFirst(), Token.SEMICOLON)) {
            throw new RuntimeException(String.format("Parse error, missing '%s'", Token.SEMICOLON));
        }
        Expression exp2 = parseExpression(tokens);
        if (isNotToken(tokens.removeFirst(), Token.BRACKET_CLOSE)) {
            throw new RuntimeException(String.format("Parse error, missing closing '%s'", Token.BRACKET_CLOSE));
        }
        return new BooleanExpression(t, exp1, exp2);
    }

    private TokenAndValue getToken(StringBuilder parsing) {
        for (Token t : Token.values()) {
            int l = t.lastMatchingCharacter(parsing.toString());
            if (l != -1) {
                TokenAndValue tv = new TokenAndValue();
                tv.t = t;
                tv.value = parsing.substring(0, l);
                parsing.delete(0, l);
                return tv;
            }
        }
        return null;
    }

    private boolean isNotToken(TokenAndValue tv, Token t) {
        return !(isToken(tv, t)); //tv == null || tv.t != t;
    }

    private boolean isToken(TokenAndValue tv, Token t) {
        return tv != null && tv.t == t;
    }

    public boolean matches(DomibusConnectorMessage message) {
        return this.expression.evaluate(message);
    }


    private static abstract class Expression {
        abstract boolean evaluate(DomibusConnectorMessage message);
    }

    private static class BooleanExpression extends Expression {
        private final Token operand;
        private final Expression exp1;
        private final Expression exp2;

        public BooleanExpression(Token t, Expression exp1, Expression exp2) {
            this.operand = t;
            this.exp1 = exp1;
            this.exp2 = exp2;
        }

        @Override
        boolean evaluate(DomibusConnectorMessage message) {
            if (operand == Token.OR) {
                return exp1.evaluate(message) || exp2.evaluate(message);
            } else if (operand == Token.AND) {
                return exp1.evaluate(message) || exp2.evaluate(message);
            } else {
                throw new RuntimeException(String.format("Unsupported OPERAND %s", operand));
            }
        }

        public String toString() {
            return String.format("(%s %s %s)", exp1, operand == Token.OR ? "OR" : "AND", exp2);
        }
    }

    private static class EqualsExpression extends Expression {

        private final Token as4Attribute;
        private final String valueString;

        public EqualsExpression(Token as4Attribute, String valueString) {
            this.as4Attribute = as4Attribute;
            this.valueString = valueString;
        }

        @Override
        boolean evaluate(DomibusConnectorMessage message) {
            return valueString.equals(extractAs4Value(message, as4Attribute));
        }

        public String toString() {
            return String.format( "%s == '%s'", as4Attribute, valueString);
        }
    }

    private static class StartsWithExpression extends Expression {

        private final Token as4Attribute;
        private final String startsWithString;

        private StartsWithExpression(Token as4Attribute, String startsWithString) {
            this.as4Attribute = as4Attribute;
            this.startsWithString = startsWithString;
        }

        @Override
        boolean evaluate(DomibusConnectorMessage message) {
            return extractAs4Value(message, as4Attribute).startsWith(startsWithString);
        }

        public String toString() { return String.format("%s startsWith '%s'", as4Attribute, startsWithString); }
    }

    private static String extractAs4Value(DomibusConnectorMessage message, Token as4Attribute) {
        DomibusConnectorMessageDetails details = message.getMessageDetails();
        if (as4Attribute == Token.AS4_SERVICE_NAME) {
            return details.getService().getService();
        } else if (as4Attribute == Token.AS4_SERVICE_TYPE) {
            return details.getService().getServiceType();
        } else if (as4Attribute == Token.AS4_ACTION) {
            return details.getAction().getAction();
        } else if (as4Attribute == Token.AS4_FINAL_RECIPIENT) {
            return details.getFinalRecipient();
        } else if (as4Attribute == Token.AS4_FROM_PARTY_ID_TYPE) {
            return details.getFromParty().getPartyIdType();
        } else if (as4Attribute == Token.AS4_FROM_PARTY_ID) {
            return details.getFromParty().getPartyId();
        } else if (as4Attribute == Token.AS4_FROM_PARTY_ROLE) {
            return details.getFromParty().getRole();
        } else {
            throw new RuntimeException("Unsupported AS4 Attribute to match!");
        }
    }

    public String toString() {
        return this.expression.toString();
    }

    public String getMatchRule() {
        return matchRule;
    }

    public Expression getExpression() {
        return expression;
    }
}
