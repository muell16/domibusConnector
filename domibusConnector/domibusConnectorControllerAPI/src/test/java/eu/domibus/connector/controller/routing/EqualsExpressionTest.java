package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Ebms;
import eu.ecodex.dc5.message.model.DC5Action;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EqualsExpressionTest {

    @Test
    public void equalsExpressionTest_shouldMatch() {
        String ACTION = "Action1";
        EqualsExpression equalsExpression = new EqualsExpression(TokenType.AS4_ACTION, ACTION);

        DC5Message message = new DC5Message();
        message.setEbmsData(new DC5Ebms());
        message.getEbmsData().setAction(DC5Action.builder().action(ACTION).build());

        boolean result = equalsExpression.evaluate(message);
        assertThat(result).isTrue();
    }

    @Test
    public void equalsExpressionTest_shouldNotMatch() {
        String ACTION = "Action1";
        EqualsExpression equalsExpression = new EqualsExpression(TokenType.AS4_ACTION, ACTION);

        DC5Message message = new DC5Message();
        message.setEbmsData(new DC5Ebms());
        message.getEbmsData().setAction(DC5Action.builder().action("OtherAction").build());

        boolean result = equalsExpression.evaluate(message);
        assertThat(result).isFalse();
    }

}