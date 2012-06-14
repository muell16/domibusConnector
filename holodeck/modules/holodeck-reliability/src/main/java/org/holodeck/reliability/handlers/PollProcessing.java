package org.holodeck.reliability.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.reliability.persistent.MessageRange;
import org.holodeck.reliability.packaging.*;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.common.soap.Util;
import java.util.*;

/**
 * This handler operates on the server side. It first puts the RM-PollRequest
 * element in the outgoing MessageContext while being in the IN_FLOW. And
 * while being in the OUT_FLOW period, it appends the acks asked for in the
 * the previous PollRequest message.
 *
 * @author Hamid Ben Malek
 */
public class PollProcessing extends AbstractHandler
{
  private static final Log log =
          LogFactory.getLog(PollProcessing.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() ) return InvocationResponse.CONTINUE;
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    if (msgCtx.getFLOW() == MessageContext.IN_FLOW)
    {
      OMElement wsrmPoll =
       Util.getGrandChildNameNS(header, Constants.POLL_REQUEST, Constants.NS);
      if (wsrmPoll == null) return InvocationResponse.CONTINUE;
      else
      {
        //Util.putElementInOutMsgContext(msgCtx, wsrmPoll, Constants.IN_POLL_REQUEST);
        msgCtx.setProperty(Constants.IN_POLL_REQUEST, wsrmPoll);
        return InvocationResponse.CONTINUE;
      }
    }

    if ( msgCtx.getFLOW() != MessageContext.OUT_FLOW )
         return InvocationResponse.CONTINUE;

    OMElement pollReq =
     (OMElement)Util.getPropertyFromInMsgCtx(msgCtx, Constants.IN_POLL_REQUEST);
    if (pollReq == null) return InvocationResponse.CONTINUE;

    // construct the acks and append them to the output header...
    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    SOAPFactory factory = (SOAPFactory)msgCtx.getEnvelope().getOMFactory();
    Response response = new Response(factory);
    List<OMElement> children =
      Util.getGrandChildrenNameNS(pollReq,
                                  Constants.REF_TO_MESSAGE_IDS, Constants.NS);
    for (OMElement aChildren : children)
    {
      OMElement ref = (OMElement) aChildren;
      String groupId = Util.getAttributeValue(ref, "groupId");
      Iterator refChildren = ref.getChildElements();
      if (refChildren == null || !refChildren.hasNext())
      {
        if ( store.isSingletonDelivered(groupId) )
             response.addNonSequenceReply(groupId, null);
      }
      else
      {
        while (refChildren != null && refChildren.hasNext())
        {
          OMElement snr = (OMElement) refChildren.next();
          String from = Util.getAttributeValue(snr, "from");
          String to = Util.getAttributeValue(snr, "to");
          int min, max = -1;
          min = Integer.parseInt(from.trim());
          max = Integer.parseInt(to.trim());
          MessageRange pollRange = new MessageRange(groupId, min, max);
          MessageRange[] acks = store.getAcks(pollRange);
          if (acks != null && acks.length > 0)
          {
            for (MessageRange ack : acks)
            {
              response.addSequenceReplies(groupId, ack.getMinSeq(),
                                          ack.getMaxSeq(), null);
            }
          }
        }
      }
    }
    header.addChild(response.getRoot());
    log.info(logPrefix + "acks were bundled with response as requested by poll request");
    return InvocationResponse.CONTINUE;
  }
}