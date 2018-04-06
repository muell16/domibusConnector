package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

/**
 *
 *
 */
public class DomainModelHelperTest {
    
    public DomainModelHelperTest() {
    }

    @Test
    public void testIsEvidenceMessage() {
        DomibusConnectorMessage createSimpleTestMessage = DomainEntityCreator.createSimpleTestMessage();
        boolean isEvidenceMessage = DomainModelHelper.isEvidenceMessage(createSimpleTestMessage);
        assertThat(isEvidenceMessage).isFalse();
    }
    
}
