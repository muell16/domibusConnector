package org.holodeck.ebms3.handlers;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.module.Constants;

/**
 * @author Hamid Ben Malek
 */
public class HeaderDetacher extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(HeaderDetacher.class.getName());
  private static final Logger log = Logger.getLogger(HeaderDetacher.class);
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;

    log.info(logPrefix + "Storing the eb:Messaging header in msg context before removal");
    OMElement ebMess =
       Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    msgCtx.setProperty(Constants.IN_MESSAGING, ebMess);

    removeWSSHeaders(header);
    removeEbmsHeaders(header);
/*
    if ( !isPureEbms3(msgCtx) )
    {
      // temporary commented out until determining whether there is an ebms msg receiver behind or not.
      log.info(logPrefix + "Storing the eb:Messaging header in msg context before removal");
      OMElement ebMess =
         Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
      msgCtx.setProperty(Constants.IN_MESSAGING, ebMess);

      removeEbmsHeaders(header);
      removeWSSHeaders(header);
      log.info(logPrefix + "Removed embs3 headers: ");
      Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

      return InvocationResponse.CONTINUE;
    }
*/
    return InvocationResponse.CONTINUE;
  }

  private void removeEbmsHeaders(SOAPHeader header)
  {
    if ( header == null ) return;
    OMElement ebms3Messaging =
       Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if (ebms3Messaging != null) ebms3Messaging.detach();
  }

  // A Pure Ebms3 message is a message destined only for the MSH Receiver
  // and should not propagate beyond the MSH
/*  
  private boolean isPureEbms3(MessageContext msgCtx)
  {
    if ( isSignalOnly(msgCtx) ) return true;
    if ( isUserMessageOrPull(msgCtx) ) return false;
    if ( isNonEbms3HeaderPresent(msgCtx) ) return false;
    return true;
  }

  private boolean isUserMessageOrPull(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    OMElement ebms3Messaging =
      Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( ebms3Messaging == null ) return false;
    OMElement usrMsg =
      Util.getGrandChildNameNS(ebms3Messaging, Constants.USER_MESSAGE,
                               Constants.NS);
    OMElement pullReq =
       Util.getGrandChildNameNS(ebms3Messaging, Constants.PULL_REQUEST,
                                Constants.NS);
    if ( usrMsg != null || pullReq != null ) return true;
    else return false;
  }

  private boolean isSignalOnly(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    OMElement ebms3Messaging =
      Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( ebms3Messaging == null ) return false;
    OMElement usrMsg =
      Util.getGrandChildNameNS(ebms3Messaging, Constants.USER_MESSAGE,
                               Constants.NS);
    OMElement signal =
      Util.getGrandChildNameNS(ebms3Messaging, Constants.SIGNAL_MESSAGE,
                               Constants.NS);
    OMElement pullReq =
    Util.getGrandChildNameNS(signal, Constants.PULL_REQUEST,
                               Constants.NS);
    if ( usrMsg == null && signal != null && pullReq == null ) return true;
    else return false;
  }

  private boolean isNonEbms3HeaderPresent(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    Iterator it = header.getChildElements();
    while ( it != null && it.hasNext() )
    {
      OMElement e = (OMElement)it.next();
      if ( e != null && !isWsAddressing(e) &&
           ( !e.getLocalName().equals(Constants.MESSAGING)
           ) ||
           ( e.getNamespace() != null &&
             !e.getNamespace().getNamespaceURI().equals(Constants.NS) &&
             e.getNamespace().getNamespaceURI().indexOf("addressing") < 0
           )
         )
         return true;
    }
    return false;
  }

  private boolean isWsAddressing(OMElement e)
  {
    if ( e == null ) return false;
    if ( e.getNamespace() == null ) return false;
    if ( e.getNamespace().getNamespaceURI().indexOf("addressing") > 0 )
         return true;
    else return false;
  }
*/
  private void removeWSSHeaders(SOAPHeader header)
  {
    if ( header == null ) return;
    String ns10 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    String ns12 = "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd";
    List<OMElement> securities10 =
       Util.getGrandChildrenNameNS(header, "Security", ns10);
    if ( securities10 != null && securities10.size() > 0 )
    {
      for (OMElement e : securities10) e.detach();
    }
    List<OMElement> securities12 =
       Util.getGrandChildrenNameNS(header, "Security", ns12);
    if ( securities12 != null && securities12.size() > 0 )
    {
      for (OMElement e : securities12) e.detach();
    }
  }
}