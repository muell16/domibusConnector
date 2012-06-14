package org.holodeck.ebms3.consumers.impl;

//import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Hamid Ben Malek
 */
//@Component
public class QueueListener implements MessageListener
{
   public void onMessage( final Message message )
   {
     if ( message instanceof TextMessage )
     {
       final TextMessage textMessage = (TextMessage) message;
       try
       {
         System.out.println( textMessage.getText() );
       }
       catch (final JMSException e)
       {
         e.printStackTrace();
       }
     }
   }
}
