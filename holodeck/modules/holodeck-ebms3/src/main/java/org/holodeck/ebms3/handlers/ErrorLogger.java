package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.ebms3.module.*;
import org.holodeck.common.soap.Util;
import java.util.*;

/**
 *  This handler runs both on the client and server side but only during the
 *  IN_FLOW and its job is to simply log any received ebms3 error signals.
 *
 * @author Hamid Ben Malek
 */
public class ErrorLogger extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(ErrorLogger.class.getName());
  private static final Logger log = Logger.getLogger(ErrorLogger.class.getName());
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix + msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
      
    List<OMElement> errors =
       Util.getGrandChildrenNameNS(header, Constants.ERROR, Constants.NS);
    if ( errors == null || errors.size() == 0 )
         return InvocationResponse.CONTINUE;

    for (OMElement error : errors)
         Util.error(log, "Received the following ebms3 Error: ", error);
      
    return InvocationResponse.CONTINUE;
  }
}