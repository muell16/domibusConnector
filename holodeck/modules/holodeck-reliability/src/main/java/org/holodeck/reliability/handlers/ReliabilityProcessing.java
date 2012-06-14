package org.holodeck.reliability.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.reliability.persistent.*;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.common.soap.Util;
import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class ReliabilityProcessing extends AbstractHandler
{
  private static final Log log =
          LogFactory.getLog(ReliabilityProcessing.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() || msgCtx.getFLOW() != MessageContext.IN_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    if (wsrmReq == null) return InvocationResponse.CONTINUE;

    // continue the rest of the implementation...
    ReceiverGroup group =
            (ReceiverGroup)msgCtx.getProperty(Constants.RECEIVER_GROUP);
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    OMElement value =
          Util.getGrandChildNameNS(wsrmReq, Constants.VALUE, Constants.NS);
    String replyPattern = value.getText();
    OMElement messageOrder =
        Util.getGrandChildNameNS(wsrmReq, Constants.MESSAGE_ORDER, Constants.NS);
    OMElement dupElim =
       Util.getGrandChildNameNS(wsrmReq, Constants.DUPLICATE_ELIMINATION, Constants.NS);
    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();

    String groupId = null;
    if ( group != null ) groupId = group.getGroupId();
    else groupId =
          Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");

    boolean duplicate = checkDuplicate(groupId, seq, store);
    boolean inOrder = false;
    if ( messageOrder != null ) inOrder = isInOrder(group, seq);
    // Handling the case where callback ack was lost in the past,
    // and the message is still being sent again
    if ( duplicate && dupElim != null && replyPattern.equalsIgnoreCase("Callback") )
         resetCallbackSent(store, group, seq);

    // If ordering is required, and the message is out of order, set the
    // "blocked" property of the PMessage to true, its "continue" property
    // to false and, if not duplicate, store it in the "OMessages" table
    if ( messageOrder != null  && !inOrder && seq != null )
    {
      log.debug(logPrefix + "Message is out of order");
      //m.setBlocked(true);
      //m.setContinueProcessing(false);
      OMessage om = new OMessage(msgCtx);
      if ( !duplicate ) store.create(om);
      return InvocationResponse.SUSPEND;
    }

    // If the message is a duplicate, duplicate elimination is required,
    // and the message is either out of order or ordering not required,
    // then set the "blocked" property of the message to true and its
    // "continue" property to false
    if ( duplicate && dupElim != null )
    {
      if ( messageOrder == null || !inOrder )
      {
        log.debug(logPrefix + "Message is duplicate");
        //m.setBlocked(true);
        //m.setContinueProcessing(false);
        return InvocationResponse.SUSPEND;
      }
    }

    return InvocationResponse.CONTINUE;
  }

 /**
  *  Check the "ReceivedRange" table to see whether the received message
  *  is a duplicate or not. If it is not a duplicate, insert an entry
  *  for it.
  */
  private boolean checkDuplicate(String groupId, String seq, DbStore store)
  {
    boolean duplicate = store.isDuplicate(groupId, seq);
    if ( !duplicate ) store.saveReceivedRange(groupId, seq);
    return duplicate;
  }

 /**
  *  Checks whether the message is in order or not
  */
  private boolean isInOrder(ReceiverGroup group, String seq)
  {
    int seqN = -1;
    if ( seq != null ) seqN = Integer.parseInt(seq);
    if ( seqN == 0 ) return true;
    if ( seqN == group.getLastDeliveredSeq() + 1 ) return true;
    else return false;
  }

  // set the "Sent" column of the "CallbackAcks" to true
  private void resetCallbackSent(DbStore store, ReceiverGroup group, String seq)
  {
    CallbackAck  ack = null;
    List acks = store.getAcknowledgments(group.getGroupId(), seq);
    if ( acks != null && acks.size() > 0 )
    {
      ack = (CallbackAck)acks.get(0);
      ack.setSent(false);
      store.save(ack);
    }
  }
}