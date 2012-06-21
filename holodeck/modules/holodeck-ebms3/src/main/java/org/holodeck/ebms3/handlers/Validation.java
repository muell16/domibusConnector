package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.ebms3.persistent.*;
import org.holodeck.ebms3.module.*;
import org.holodeck.common.soap.Util;

/**
 * @author Hamid Ben Malek
 */
public class Validation extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(Validation.class.getName());
  private static final Logger log = Logger.getLogger(Validation.class.getName());
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() ||
         msgCtx.getFLOW() != MessageContext.IN_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix + msgCtx.getEnvelope().getHeader());

    return InvocationResponse.CONTINUE;
  }
}