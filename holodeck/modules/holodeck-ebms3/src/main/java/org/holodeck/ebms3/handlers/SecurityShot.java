package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import org.holodeck.ebms3.module.Configuration;
import org.holodeck.ebms3.config.Leg;

/**
 *  This handler runs only at the server side when a message is received
 *  and its purpose is just to examine the SOAP Header in order to determine
 *  the PMode being used, and then which leg of the PMode, and finally
 *  what is the security quality used on that leg (This security quality
 *  will then be store in the MessageContext, so that at later time,
 *  the Holodeck-Security Module would know what WS-Security Policy
 *  to attach to the service the message is going to).
 *
 * @author Hamid Ben Malek
 */
public class SecurityShot extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(SecurityShot.class.getName());
  private static final Logger log = Logger.getLogger(SecurityShot.class.getName());

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW  ||
         !msgCtx.isServerSide() )
         return InvocationResponse.CONTINUE;

    Leg requestLeg = Configuration.getLegFromServerSideReq(msgCtx);
    if ( requestLeg == null ) return InvocationResponse.CONTINUE;
    String security = requestLeg.getSecurity();
    if ( security != null )
    { 
      msgCtx.setProperty("SECURITY", security);
      log.debug("Received Request Message is using security " + security);
    }
    return InvocationResponse.CONTINUE;
  }
}