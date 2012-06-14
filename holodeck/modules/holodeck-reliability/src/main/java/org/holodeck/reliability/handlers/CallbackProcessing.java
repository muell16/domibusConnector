package org.holodeck.reliability.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.reliability.persistent.CallbackAck;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.common.soap.Util;

/**
 * if the replyPattern is "Callback", construct an Ack message
 * and store it in the "Acknowledgments" table. A worker thread will
 * send it at a later time.
 *
 * @author Hamid Ben Malek
 */
public class CallbackProcessing extends AbstractHandler
{
  private static final Log log =
          LogFactory.getLog(CallbackProcessing.class.getName());
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

    OMElement value =
          Util.getGrandChildNameNS(wsrmReq, Constants.VALUE, Constants.NS);
    String replyPattern = value.getText();
    if ( replyPattern == null || !replyPattern.equalsIgnoreCase("Callback"))
         return InvocationResponse.CONTINUE;

    String groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    String ackTo = Util.getGrandChildValue(wsrmReq, Constants.BARE_URI);
    CallbackAck ackMsg = new CallbackAck(groupId, seq, ackTo);
//    SOAPEnvelope env = null;
//    if ( msgCtx.isSOAP11() ) env = Util.createEnvelope(1.1);
//    else env = Util.createEnvelope(1.2);
//    ackMsg.setEnvelope(env.toString());
    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    store.save(ackMsg);
    log.info(logPrefix + "An ack was saved in DB to be sent later");

    return InvocationResponse.CONTINUE;
  }
}