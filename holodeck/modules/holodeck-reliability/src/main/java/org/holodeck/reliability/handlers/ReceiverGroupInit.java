package org.holodeck.reliability.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.reliability.persistent.ReceiverGroup;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.common.soap.Util;
import java.util.*;

/**
 * This handler, upon receiving an RM-Request message, it creates or updates
 * a corresponding ReceiverGroup based on the information contained within
 * the RM-Request element.
 * 
 * @author Hamid Ben Malek
 */
public class ReceiverGroupInit extends AbstractHandler
{
  private static final Log log =
          LogFactory.getLog(ReceiverGroupInit.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() || msgCtx.getFLOW() != MessageContext.IN_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix + msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    if (wsrmReq == null) return InvocationResponse.CONTINUE;

    // If received message is a singleton, no need to create a receiver group:
    String seq = Util.getGrandChildAttributeValue(wsrmReq,
                                             Constants.SEQUENCE_NUM, "number");
    if ( seq == null || seq.trim().equals("") )
         return InvocationResponse.CONTINUE;

    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    String groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    ReceiverGroup group =
          (ReceiverGroup)store.getReceiverGroupByGroupId(groupId);
    if (group == null) group = new ReceiverGroup(wsrmReq, new Date());
    else updateGroup(group, wsrmReq);
    store.save(group);
    log.debug(logPrefix + "ReceiverGroup updated and saved");
    // Put the group in MessageContext for later use by following handler:
    msgCtx.setProperty(Constants.RECEIVER_GROUP, group);

    return InvocationResponse.CONTINUE;
  }

  private void updateGroup(ReceiverGroup group, OMElement wsrmReq)
  {
    if (group == null || wsrmReq == null) return;
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    if ( seq != null && !seq.trim().equals("") )
         group.setHighestSeqReceived(Integer.parseInt(seq));
    String seqStatus =
     Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "last");
    if ( seqStatus != null && seqStatus.equalsIgnoreCase("true") )
         group.setComplete(true);
    String msgExpiryTime =
         Util.getGrandChildValue(wsrmReq, Constants.EXPIRY_TIME);
    if (msgExpiryTime != null)
        group.setMaxMessageExpiryTime(Util.utcToDate(msgExpiryTime));
    group.setLastMessageTimestamp(new Date());
  }
}