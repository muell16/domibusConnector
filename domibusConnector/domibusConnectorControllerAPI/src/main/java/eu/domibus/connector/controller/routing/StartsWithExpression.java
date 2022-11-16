package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DC5Message;

public class StartsWithExpression extends MatchExpression {

    private final TokenType as4Attribute;
    private final String startsWithString;

    StartsWithExpression(TokenType as4Attribute, String startsWithString) {
        this.as4Attribute = as4Attribute;
        this.startsWithString = startsWithString;
    }

    @Override
    boolean evaluate(DC5Message message) {
        return RoutingRulePattern.extractAs4Value(message, as4Attribute).startsWith(startsWithString);
    }


    @Override
    public TokenType getMatchOperator() {
        return TokenType.EQUALS;
    }

    @Override
    public TokenType getAs4Attribute() {
        return as4Attribute;
    }

    @Override
    public String getValueString() {
        return startsWithString;
    }

    public String toString() {
        return String.format("%s(%s, '%s')", TokenType.STARTSWITH, as4Attribute, startsWithString);
    }
}
