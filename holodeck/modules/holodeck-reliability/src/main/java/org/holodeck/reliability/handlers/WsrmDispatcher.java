package org.holodeck.reliability.handlers;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AbstractDispatcher;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.module.Constants;

/**
 * This is an axis2 dispatcher inserted in the transport phase of
 * axis2 gloabal config file. It is intented dispatch a pure ack
 * (i.e. an ack that is not mixed with something else, such as another
 *  ebms3 user/signal message, and/or having a payload) to the wsrm
 *  service.
 *
 * @author Hamid Ben Malek
 */
public class WsrmDispatcher extends AbstractDispatcher
{
//  private static final Log log = LogFactory.getLog(WsrmDispatcher.class.getName());
  private static final Logger log = Logger.getLogger(WsrmDispatcher.class.getName());

  public AxisOperation findOperation(AxisService service, MessageContext msgCtx)
                       throws AxisFault
  {
    if ( !isPureWsrm(msgCtx) ) return null;
    return service.getOperation(new QName("execute"));
  }

  public AxisService findService(MessageContext msgCtx) throws AxisFault
  {
    if ( !isPureWsrm(msgCtx) ) return null;
    return msgCtx.getConfigurationContext().getAxisConfiguration()
                 .getService("wsrm");
  }

  public void initDispatcher() {}

  private boolean isPureWsrm(MessageContext msgCtx)
  {
    EndpointReference epr = msgCtx.getTo();
    if ( epr == null ) return false;
    String to = epr.getAddress();
    if ( to == null ) return false;
    if ( to.endsWith("/wsrm") || to.endsWith("/wsrm/") ) return true;
    else return false;
    /*
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;

    if ( isBodyNotEmpty(msgCtx) ) return false;
    if ( isAttachmentPresent(msgCtx) ) return false;
    if ( isRMReqPresent(msgCtx) ) return false;
    if ( isNonRMHeaderPresent(msgCtx) ) return false;
    if ( containsPollOrResponse(msgCtx) )
    {
      log.debug("request is pure wsrm message.");
      return true;
    }
    return false;
    */
  }

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
    if ( att.getIncomingAttachmentStreams() == null ) return false;
    //String[] cids = att.getAllContentIDs();
    //if (cids == null || cids.length <= 1) return false;
    log.debug("request message contains attachments.");
    //else
    return true;
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

  private boolean containsPollOrResponse(MessageContext msgCtx)
  {
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    OMElement wsrmPoll =
      Util.getGrandChildNameNS(header, Constants.POLL_REQUEST, Constants.NS);
    OMElement wsrmResp =
      Util.getGrandChildNameNS(header, Constants.RESPONSE, Constants.NS);
    if ( wsrmPoll != null || wsrmResp != null ) return true;
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
}