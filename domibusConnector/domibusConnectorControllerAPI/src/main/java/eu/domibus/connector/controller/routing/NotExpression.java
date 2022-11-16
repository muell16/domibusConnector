package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DC5Message;

public class NotExpression extends Expression {

    private final Expression exp1;
    private final Token tokenTypeAndValue;

    public NotExpression(Expression exp1, Token tokenTypeAndValue) {
        this.exp1 = exp1;
        this.tokenTypeAndValue = tokenTypeAndValue;
    }

    @Override
    boolean evaluate(DC5Message message) {
        return !exp1.evaluate(message);
    }

    public Expression getExp1() {
        return exp1;
    }

    public Token getTokenTypeAndValue() {
        return tokenTypeAndValue;
    }

    public String toString() {
        return tokenTypeAndValue.tokenType.getHumanString() + "(" + exp1.toString() + ")";
    }

}
