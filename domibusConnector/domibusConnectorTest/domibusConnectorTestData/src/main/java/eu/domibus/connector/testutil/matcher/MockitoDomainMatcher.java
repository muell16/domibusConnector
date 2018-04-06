
package eu.domibus.connector.testutil.matcher;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class MockitoDomainMatcher {

    
    public static BaseMatcher<DomibusConnectorMessage> eqToRefToMessageId(String refToMessageId) {
        return new RefToMessageIdMatcher(refToMessageId);
    }
    
    private static class RefToMessageIdMatcher extends BaseMatcher<DomibusConnectorMessage> {

        private final String messageReference;
                
        public RefToMessageIdMatcher(String messageReference) {
            this.messageReference = messageReference;
        }
        
        @Override
        public void describeTo(Description description) {
            
        }

        @Override
        public boolean matches(Object item) {
            DomibusConnectorMessage m = (DomibusConnectorMessage) item;
            return messageReference.equals(m.getMessageDetails().getRefToMessageId());
        }
    
    }
    
}
