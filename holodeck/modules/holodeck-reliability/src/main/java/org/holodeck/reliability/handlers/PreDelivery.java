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

/**
 * @author Hamid Ben Malek
 */
public class PreDelivery extends AbstractHandler
{
  private static final Log log =
          LogFactory.getLog(PreDelivery.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() ) return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    if (wsrmReq == null) return InvocationResponse.CONTINUE;
    String groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    ReceiverGroup group = store.getReceiverGroupByGroupId(groupId);
    if ( seq != null && !seq.trim().equals("") && group != null )
             group.setLastDeliveredSeq(Integer.parseInt(seq));
    if ( group != null )
         group.setDeliveredCount( 1 + group.getDeliveredCount());
    log.info(logPrefix + "About to save deliveredRange to DB");
    store.saveDeliveredRange(groupId, seq);
    if ( group != null ) store.save(group);

    return InvocationResponse.CONTINUE;
  }
}