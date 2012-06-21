package org.holodeck.reliability.handlers;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;

/**
 * This handler processes messages containing the RM Response element
 * (i.e acknoledgements).
 * For example, these messages could be the response to a Poll Request,
 * or simply an acknowledgment message sent as a callback or bundled with
 * a business response. This handler updates the "GMessages" table by setting
 * the column "Acknowledged" to true for those messages referred to by the
 * Acknowledgment within the RM Response element.
 *
 * @author Hamid Ben Malek
 */
public class ResponseProcessing extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(ResponseProcessing.class.getName());
  private static final Logger log = Logger.getLogger(ResponseProcessing.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
    OMElement wsrmResp =
       Util.getGrandChildNameNS(header, Constants.RESPONSE, Constants.NS);
    if (wsrmResp == null) return InvocationResponse.CONTINUE;
    List<OMElement> nsr =
        Util.getGrandChildrenNameNS(wsrmResp,
                                    Constants.NON_SEQUENCE_REPLY, Constants.NS);
    List<OMElement> sr =
        Util.getGrandChildrenNameNS(wsrmResp,
                                    Constants.SEQUENCE_REPLIES, Constants.NS);
    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    if ( nsr != null && nsr.size() > 0 )
    {
      String groupId = null;
      String fault = null;
      for (OMElement aNsr : nsr)
      {
        groupId = Util.getAttributeValue(aNsr, "groupId");
        fault = Util.getAttributeValue(aNsr, "fault");
        log.info(logPrefix + "Received Ack for groupId=" + groupId);
        if (fault != null) log.info(logPrefix + "Ack has a fault=" + fault);
        if (fault == null || fault.trim().equals(""))
        {
          store.markAsAcknowledged(groupId);
          log.debug(logPrefix + "Marked message in DB as acknowledged ");
        }
      }
    }
    if ( sr != null && sr.size() > 0 )
    {
      String groupId = null;
      String fault = null;
      String from = null;
      String to = null;
      for (OMElement aSr : sr)
      {
        groupId = Util.getAttributeValue(aSr, "groupId");
        List<OMElement> replyRanges =
               Util.getGrandChildrenNameNS(aSr, "ReplyRange", Constants.NS);
        if (replyRanges != null && replyRanges.size() > 0)
        {
          for (OMElement replyRange : replyRanges)
          {
            fault = Util.getAttributeValue(replyRange, "fault");
            from = Util.getAttributeValue(replyRange, "from");
            to = Util.getAttributeValue(replyRange, "to");
            log.info(logPrefix + "Received Acks for groupId=" + groupId);
            log.info(logPrefix + "sequence from=" + from);
            log.info(logPrefix + "sequence to=" + to);
            if (fault != null) log.debug(logPrefix + "fault=" + fault);
            if (fault == null || fault.trim().equals(""))
            {
              store.markAsAcknowledged(groupId, Integer.parseInt(from),
                                     Integer.parseInt(to));
              log.info(logPrefix + "Marked message in DB as acknowledged ");
            }
          }
        }
      }
    }
    return InvocationResponse.CONTINUE;
  }
}