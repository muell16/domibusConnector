package org.holodeck.ebms3.consumers.impl;

//import org.springframework.stereotype.Component;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * @author Hamid Ben Malek
 */
//@Component
public class JmsExceptionListener implements ExceptionListener
{
  public void onException( final JMSException e )
  {
    e.printStackTrace();
  }
}