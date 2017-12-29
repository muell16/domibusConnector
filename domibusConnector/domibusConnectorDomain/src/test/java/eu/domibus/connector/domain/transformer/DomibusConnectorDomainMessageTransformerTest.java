/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.domain.transformer;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.test.util.DomainCreator;
import static eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer.transformDomainToTransition;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Ignore;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusConnectorDomainMessageTransformerTest {
    
    public DomibusConnectorDomainMessageTransformerTest() {
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTransformDomainToTransition_nullProvided_shouldThrowIllegalArgumentException() {
        transformDomainToTransition(null);
    }
    
    //what should i do if transformed object does not validate wsdl spec?
    
    @Test
    public void testTransformDomainToTransition() {
        DomibusConnectorMessage domainMessage = DomainCreator.createMessage();
        
        DomibusConnectorMessageType messageType = DomibusConnectorDomainMessageTransformer.transformDomainToTransition(domainMessage);
                
        assertThat(messageType).as("transformed object is not allowed to be null").isNotNull();
        
        assertThat(messageType.getMessageDetails()).as("message details are not allowed to be null!").isNotNull();
        assertThat(messageType.getMessageContent()).as("message content is set in test entity!").isNotNull();
        assertThat(messageType.getMessageConfirmations()).isNotNull(); //description needed, is not null in test entity?
        assertThat(messageType.getMessageAttachments()).isNotNull();
        assertThat(messageType.getMessageErrors()).isNotNull();
        
        
        
        
    }

    @Test
    @Ignore //not finished yet!
    public void testTransformTransitionToDomain() {
        
    }
    
}
