package org.holodeck.reliability.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.reliability.packaging.*;
import org.holodeck.reliability.module.Constants;
import org.holodeck.common.soap.Util;

/**
 * This handler operates at the server side in the "OUT_FLOW" period, and it
 * appends an ack (RM-Response element) to the SOAP header (provided that the
 * corresponding request message had a "Response" reply pattern.
 *
 * @author Hamid Ben Malek
 */
public class ResponseAckAppender extends AbstractHandler
{
  private static final Log log =
          LogFactory.getLog(ResponseAckAppender.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() || msgCtx.getFLOW() != MessageContext.OUT_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    OMElement wsrmReq =
        (OMElement)Util.getPropertyFromInMsgCtx(msgCtx, Constants.IN_REQUEST);

    if ( wsrmReq == null || !wsrmReq.getLocalName().equals(Constants.REQUEST) )
         return InvocationResponse.CONTINUE;

    String replyPattern = Util.getGrandChildValue(wsrmReq, Constants.VALUE);
    log.info(logPrefix + "replyPattern from previous request is " +
              replyPattern);
    if ( replyPattern == null || !replyPattern.equals("Response") )
         return InvocationResponse.CONTINUE;
    String groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");

    Response.createResponse(msgCtx.getEnvelope(), groupId, seq);
    log.info(logPrefix +
             "An ack has been bundled with the outgoing business response");

    return InvocationResponse.CONTINUE;
  }
}