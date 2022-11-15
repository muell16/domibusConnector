package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DomibusConnectorMessage;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageDetails;
import eu.ecodex.dc5.message.model.DC5Action;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EqualsExpressionTest {

    @Test
    public void equalsExpressionTest_shouldMatch() {
        String ACTION = "Action1";
        EqualsExpression equalsExpression = new EqualsExpression(TokenType.AS4_ACTION, ACTION);

        DomibusConnectorMessage message = new DomibusConnectorMessage();
        message.setMessageDetails(new DomibusConnectorMessageDetails());
        message.getMessageDetails().setAction(new DC5Action());
        message.getMessageDetails().getAction().setAction(ACTION);

        boolean result = equalsExpression.evaluate(message);
        assertThat(result).isTrue();
    }

    @Test
    public void equalsExpressionTest_shouldNotMatch() {
        String ACTION = "Action1";
        EqualsExpression equalsExpression = new EqualsExpression(TokenType.AS4_ACTION, ACTION);

        DomibusConnectorMessage message = new DomibusConnectorMessage();
        message.setMessageDetails(new DomibusConnectorMessageDetails());
        message.getMessageDetails().setAction(new DC5Action());
        message.getMessageDetails().getAction().setAction("OtherAction");

        boolean result = equalsExpression.evaluate(message);
        assertThat(result).isFalse();
    }

}