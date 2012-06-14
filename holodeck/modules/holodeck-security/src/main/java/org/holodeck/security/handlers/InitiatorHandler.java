package org.holodeck.security.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Policy;
import org.apache.axis2.client.Options;
import org.apache.rampart.RampartMessageData;

import org.holodeck.security.module.SecurityUtil;
import org.holodeck.security.module.Configuration;

/**
 * @author Hamid Ben Malek
 */
public class InitiatorHandler extends AbstractHandler
{
  private static final Log log =
                  LogFactory.getLog(InitiatorHandler.class.getName());

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() == MessageContext.IN_FLOW  ||
         msgCtx.isServerSide() )
         return InvocationResponse.CONTINUE;

    String security = (String)msgCtx.getProperty(SecurityUtil.SECURITY);
    if ( security == null || security.trim().equals("") )
         return InvocationResponse.CONTINUE;
    Policy policy = Configuration.getInitiatorPolicy(security);
    if ( policy == null ) return InvocationResponse.CONTINUE;
    Options options = msgCtx.getOptions();
//    if ( options == null ) options = new Options();
    options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, policy);
//    msgCtx.setOptions(options);
    // engage rampart...

    log.debug("Message going out using security " + security);
    return InvocationResponse.CONTINUE;
  }
}