package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.ebms3.persistent.*;
import org.holodeck.ebms3.module.*;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.config.Leg;
import org.holodeck.common.soap.Util;

/**
 *  This handler runs only on the client side during the OUT_FLOW, and its
 *  main job is to save to the database information that a given messageId
 *  is being sent out (pushed) so that to keep track of which messages did not
 *  get any receipts. A background thread will notify the producer if finds that
 *  some these outgoing messaging did not get any receipts after some timeout.
 *
 * @author Hamid Ben Malek
 */
public class TrackReceipt extends AbstractHandler
{
  private static final Log log =
                  LogFactory.getLog(TrackReceipt.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.OUT_FLOW  ||
         msgCtx.isServerSide() )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if ( header == null ) return InvocationResponse.CONTINUE;

    OMElement messaging =
         Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( messaging == null ) return InvocationResponse.CONTINUE;
    String messageId = Util.getGrandChildValue(messaging, Constants.MESSAGE_ID);

    // we save such information to the database only if this outgoing message
    // requires receipts:
    MsgInfoSet mis =
          (MsgInfoSet)msgCtx.getProperty(Constants.MESSAGE_INFO_SET);
    if ( mis == null ) return InvocationResponse.CONTINUE;
    Leg thisRequestLeg = Configuration.getLeg(mis);
    if ( thisRequestLeg == null ) return InvocationResponse.CONTINUE;
    if ( thisRequestLeg.getAs4Receipt() == null )
    {
      log.info(logPrefix +
        "The outgoing message does not require receipts, therefore not tracking will be saved to database");
      return InvocationResponse.CONTINUE;
    }

    ReceiptTracking tracking = new ReceiptTracking();
    tracking.setMessageId(messageId);
    tracking.setToURL(msgCtx.getTo().getAddress());
    tracking.setPmode(mis.getPmode());
    // @ToDo investigate whether we should populate the request property of tracking
    // by reading it from the message context. For this, we would need to place any
    // UserMsgToPush object into the message context upon retrieval from DB before
    // being sent out. The reason for doing this, is to be able to do resending for
    // those messages that did not get any receipts.
    Constants.store.save(tracking);
    log.info(logPrefix + " saved tracking info for outgoing request");

    return InvocationResponse.CONTINUE;
  }
}