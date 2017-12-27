/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.test.util;

import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageContent;
import eu.domibus.connector.domain.MessageDetails;

/**
 *  create some domain objects, which can be later used
 *  for testing
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class TestMessageCreator {

    public static Message createSimpleTestMessage() {
        MessageDetails messageDetails = new MessageDetails();
        messageDetails.setConversationId("conversation1");
        
        
        MessageContent messageContent = new MessageContent();
        
        
        Message msg = new Message(messageDetails, messageContent);
        
                
        return msg;
    }
    
}
