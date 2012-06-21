package org.holodeck.reliability.handlers;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.module.Constants;

//import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class WsrmHeadersDetacher extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(WsrmHeadersDetacher.class.getName());
  private static final Logger log = Logger.getLogger(WsrmHeadersDetacher.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix + msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
    if ( !isPureWsrm(msgCtx) )
    {
      removeWsrmHeaders(header);
      log.info(logPrefix + "Removed wsrm headers: " +
                msgCtx.getEnvelope().getHeader());
      return InvocationResponse.CONTINUE;
    }
    else
    {
      // this condition checks that we are receiving a pure PollRequest
      // for which we need to respond asynchronsously
      if ( msgCtx.isServerSide() && containsRMPollReq(msgCtx) &&
           msgCtx.getFLOW() == MessageContext.IN_FLOW  &&
           !isPollReqReplyToPresent(msgCtx) )
      {
        SOAPEnvelope env = Util.createEnvelope(msgCtx.isSOAP11() ? 1.1 : 1.2);
        Util.sendResponse(env, msgCtx);
      }
      return InvocationResponse.ABORT;
    }
  }

  private void removeWsrmHeaders(SOAPHeader header)
  {
    if ( header == null ) return;
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    OMElement wsrmResp =
       Util.getGrandChildNameNS(header, Constants.RESPONSE, Constants.NS);
    OMElement wsrmPoll =
       Util.getGrandChildNameNS(header, Constants.POLL_REQUEST, Constants.NS);
    if (wsrmReq != null) wsrmReq.detach();
    if (wsrmResp != null) wsrmResp.detach();
    if (wsrmPoll != null) wsrmPoll.detach();
  }

  private boolean containsRMPollReq(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    OMElement wsrmPoll =
       Util.getGrandChildNameNS(header, Constants.POLL_REQUEST, Constants.NS);
    if (wsrmPoll != null) return true;
    else return false;
  }

  private boolean isPollReqReplyToPresent(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    OMElement wsrmPoll =
       Util.getGrandChildNameNS(header, Constants.POLL_REQUEST, Constants.NS);
    if (wsrmPoll == null) return false;
    OMElement replyTo =
       Util.getGrandChildNameNS(wsrmPoll, Constants.REPLY_TO, Constants.NS);
    if ( replyTo != null ) return true;
    else return false;
  }

  // A Pure wsrm message is a message destined only for the RMP Receiver
  // and should not propagate beyond the RMP
  private boolean isPureWsrm(MessageContext msgCtx)
  {
    if ( msgCtx.isServerSide() && msgCtx.getFLOW() == MessageContext.IN_FLOW )
    {
      EndpointReference epr = msgCtx.getTo();
      if ( epr == null ) return false;
      String to = epr.getAddress();
      if ( to == null ) return false;
      if ( to.endsWith("/wsrm") || to.endsWith("/wsrm/") ) return true;
      else return false;
    }
    else return false;
/*
    if ( isBodyNotEmpty(msgCtx) ) return false;
    if ( isAttachmentPresent(msgCtx) ) return false;
    if ( isRMReqPresent(msgCtx) ) return false;
    if ( isNonRMHeaderPresent(msgCtx) ) return false;
    return true;
*/
  }
  /*
  private boolean isBodyNotEmpty(MessageContext msgCtx)
  {
    SOAPBody body = msgCtx.getEnvelope().getBody();
    Iterator it = body.getChildElements();
    if (it != null && it.hasNext() && it.next() != null) return true;
    else return false;
  }

  private boolean isAttachmentPresent(MessageContext msgCtx)
  {
    Attachments att = msgCtx.getAttachmentMap();
    if (att == null ) return false;
    String[] cids = att.getAllContentIDs();
    if (cids == null || cids.length <= 1) return false;
    else return true;
  }

  private boolean isRMReqPresent(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    OMElement wsrmReq =
         Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    if (wsrmReq != null) return true;
    else return false;
  }

  private boolean isNonRMHeaderPresent(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    Iterator it = header.getChildElements();
    while ( it != null && it.hasNext() )
    {
      OMElement e = (OMElement)it.next();
      if ( e != null && !isWsAddressing(e) &&
           ( !e.getLocalName().equals(Constants.RESPONSE) &&
             !e.getLocalName().equals(Constants.POLL_REQUEST)
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
}