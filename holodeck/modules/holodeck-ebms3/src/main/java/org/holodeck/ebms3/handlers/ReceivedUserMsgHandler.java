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
import org.holodeck.common.soap.Util;

/**
 * This handler stores a received UserMessage in the
 * "Received_UserMsg" table, so that the message can be consumed
 * later by the concerned parties, or be dispatched to a different
 * service by some background worker that listen on arrived UserMessages.
 * These received UserMessages could be received either by a push or a pull
 *
 * @author Hamid Ben Malek
 */
public class ReceivedUserMsgHandler extends AbstractHandler
{
  private static final Log log =
               LogFactory.getLog(ReceivedUserMsgHandler.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW )
         return InvocationResponse.CONTINUE;

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;

    OMElement userMessage =
      Util.getGrandChildNameNS(header, Constants.USER_MESSAGE,
                               Constants.NS);
    if ( userMessage == null ) return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    // Simply store the received UserMessage to Database:
    DbStore store =
          (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    MsgInfo msgInfo = (MsgInfo)msgCtx.getProperty(Constants.IN_MSG_INFO);
    if ( msgInfo == null )
    {
      msgInfo = EbUtil.createMsgInfo(msgCtx);
      msgCtx.setProperty(Constants.IN_MSG_INFO, msgInfo);
    }

    ReceivedUserMsg receivedUM = new ReceivedUserMsg(msgCtx, msgInfo);

    store.save(receivedUM);
    log.info(logPrefix + "Stored received UserMessage into Database");

    return InvocationResponse.CONTINUE;
  }
}