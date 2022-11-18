
package eu.domibus.connector.testutil.matcher;

import eu.ecodex.dc5.message.model.DC5Message;
import org.mockito.ArgumentMatcher;

/**
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class MockitoDomainMatcher {


    public static ArgumentMatcher<DC5Message> eqToRefToMessageId(String refToMessageId) {
        return new RefToMessageIdMatcher(refToMessageId);
    }

    private static class RefToMessageIdMatcher implements ArgumentMatcher<DC5Message> {

        private final String messageReference;

        public RefToMessageIdMatcher(String messageReference) {
            if (messageReference == null) {
                throw new IllegalArgumentException("Message Reference cannot be null!");
            }
            this.messageReference = messageReference;
        }

        @Override
        public boolean matches(DC5Message message) {
            if (message == null) {
                return false;
            }
            return messageReference.equals(message.getEbmsData().getRefToEbmsMessageId());
        }

    }

}
