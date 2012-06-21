package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.engine.AbstractDispatcher;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.context.MessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import org.holodeck.common.soap.Util;

import javax.xml.namespace.QName;

/**
 * @author Hamid Ben Malek
 */
public class Ebms3Dispatcher extends AbstractDispatcher
{
//  private static final Log log = LogFactory.getLog(Ebms3Dispatcher.class.getName());
  private static final Logger log = Logger.getLogger(Ebms3Dispatcher.class);
  
  private String logPrefix = "";

  public AxisOperation findOperation(AxisService service, MessageContext msgCtx)
                       throws AxisFault
  {
    if ( !isDestinedForMSH(msgCtx) ) return null;
    return service.getOperation(new QName("push"));
  }

  public AxisService findService(MessageContext msgCtx) throws AxisFault
  {
    if ( !isDestinedForMSH(msgCtx) ) return null;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    return msgCtx.getConfigurationContext().getAxisConfiguration()
                 .getService("msh");
  }

  public void initDispatcher() {}

  private boolean isDestinedForMSH(MessageContext msgCtx)
  {
    EndpointReference epr = msgCtx.getTo();
    if ( epr == null ) return false;
    String to = epr.getAddress();
    return to != null && (to.endsWith("/msh") || to.endsWith("/msh/"));
  }
}