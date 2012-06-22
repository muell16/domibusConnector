package org.holodeck.security.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.apache.neethi.Policy;
import org.holodeck.security.module.Configuration;
import org.holodeck.security.module.SecurityUtil;

/**
 *  This handler (which is part of the Holodeck-Security Module) runs before
 *  rampart module on the server side, and its purpose is to on the fly attach
 *  a policy to the service the message is going (the security policy depends
 *  on which PMode it is being used for that particular received request)
 * 
 * @author Hamid Ben Malek
 */
public class RecipientHandler extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(RecipientHandler.class.getName());
  private static final Logger log = Logger.getLogger(RecipientHandler.class.getName());

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW  ||
         !msgCtx.isServerSide() )
         return InvocationResponse.CONTINUE;

    String security = (String)msgCtx.getProperty(SecurityUtil.SECURITY);
    if ( security == null || security.trim().equals("") )
         return InvocationResponse.CONTINUE;
    Policy policy = Configuration.getRecipientPolicy(security);
    if ( policy == null ) return InvocationResponse.CONTINUE;

    AxisService srv =
       msgCtx.getConfigurationContext().getAxisConfiguration().getService("msh");
     srv.getPolicySubject().attachPolicy(policy);
    log.debug("Recipient policy attached to the request message");
    return InvocationResponse.CONTINUE;
  }
}