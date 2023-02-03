package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Ebms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.CheckForNull;

/**
 *
 *
 * The routing rule grammar:
 * {@literal
##BNF RoutingRulePattern
tag::BNF[]
<ROUTING_RULE_PATTERN> ::= <BOOLEAN_EXPRESSION> | <COMPARE_EXPRESSION> | <NOT_EXPRESSION>
<BOOLEAN_EXPRESSION> ::= <OPERAND>(<ROUTING_RULE_PATTERN>, <ROUTING_RULE_PATTERN>)
<COMPARE_EXPRESSION> ::= equals(<AS4_TYPE>, '<VALUE>') | startswith(<AS4_TYPE>, '<VALUE>')
<NOT_EXPRESSION> ::= not(<ROUTING_RULE_PATTERN>)
<OPERAND> ::= "&" | "|"
<AS4_TYPE> ::= ServiceType | ServiceName | FinalRecipient | Action | FromPartyId | FromPartyRole | FromPartyIdType
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


    public RoutingRulePattern(String pattern) {
        this.matchRule = pattern;
        createMatcher(pattern);
    }


    private void createMatcher(final String pattern) {
        //TODO: error handling...
        this.expression = new ExpressionParser(pattern).getParsedExpression().get();
    }


    public boolean matches(DC5Message message) {
        return this.expression.evaluate(message);
    }

    @CheckForNull
    static String extractAs4Value(DC5Message message, TokenType as4Attribute) {
        DC5Ebms details = message.getEbmsData();
        if (details != null) {
            if (as4Attribute == TokenType.AS4_SERVICE_NAME) {
                return details.getService().getService();
            } else if (as4Attribute == TokenType.AS4_SERVICE_TYPE) {
                return details.getService().getServiceType();
            } else if (as4Attribute == TokenType.AS4_ACTION) {
                return details.getAction().getAction();
            } else if (as4Attribute == TokenType.AS4_FINAL_RECIPIENT) {
                return details.getResponder().getPartnerAddress().getEcxAddress();
            } else if (as4Attribute == TokenType.AS4_FROM_PARTY_ID_TYPE) {
                return details.getInitiator().getPartnerAddress().getParty().getPartyIdType();
            } else if (as4Attribute == TokenType.AS4_FROM_PARTY_ID) {
                return details.getInitiator().getPartnerAddress().getParty().getPartyId();
            } else if (as4Attribute == TokenType.BACKEND_NAME) {
                return message.getBackendLinkName() == null ? null : message.getBackendLinkName().getLinkName();
            } else if (as4Attribute == TokenType.GATEWAY_NAME) {
                return message.getGatewayLinkName() == null ? null : message.getGatewayLinkName().getLinkName();
            } else {
                throw new RuntimeException("Unsupported AS4 Attribute to match!");
            }
        } else {
            return null;
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
