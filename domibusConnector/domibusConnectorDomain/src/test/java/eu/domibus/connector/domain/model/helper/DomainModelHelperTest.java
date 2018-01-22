/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
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