package org.holodeck.reliability.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.holodeck.reliability.persistent.GMessage;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.reliability.module.SenderWorker;
import org.holodeck.reliability.config.Reliability;
import org.holodeck.common.soap.Util;


/**
 * This handler saves the outgoing request message in the database
 * when the quality associated to this request message has the
 * "atLeastOnce" set to true.
 *
 * @author Hamid Ben Malek
 */
public class PreSend extends AbstractHandler
{
  private static final Log log = LogFactory.getLog(PreSend.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.OUT_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix + msgCtx.getEnvelope().getHeader());

    Reliability quality = (Reliability)msgCtx.getProperty(Constants.QUALITY);
    if ( quality == null || !quality.isAtLeastOnce() )
         return InvocationResponse.CONTINUE;

    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    GMessage gm = new GMessage(msgCtx, quality);
    long retransInt = (long)quality.getRetransmissionInterval();
    long now = System.currentTimeMillis();
    gm.setTimeToSend(now + retransInt);
    store.save(gm);
    //addSenderWorker(msgCtx);

    return InvocationResponse.CONTINUE;
  }

  private void addSenderWorker(MessageContext msgCtx)
  {
    new SenderWorker(msgCtx);
  }
}