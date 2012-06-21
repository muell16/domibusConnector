package org.holodeck.ebms3.workers.impl;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.packaging.Messaging;
import org.holodeck.ebms3.persistent.ReceiptData;
import org.holodeck.ebms3.submit.EbMessage;

/**
 * @author Hamid Ben Malek
 */
public class ReceiptSender implements Runnable
{
//  private static final Log log = LogFactory.getLog(ReceiptSender.class.getName());
  private static final Logger log = Logger.getLogger(ReceiptSender.class.getName());

  public void run()
  {
    ReceiptData rd = Constants.store.getReceiptData();
    if ( rd == null || rd.getToURL() == null ) return;
    log.info("About to send an AS4 receipt as a callback to " +
             rd.getToURL());

    EbMessage message = new EbMessage(Constants.configContext);
    addReceiptHeader(message, rd);
    message.inOnly(rd.getToURL(), null, null);
    log.info("AS4 receipt was sent as callback to " + rd.getToURL());
    Util.debug(log, message.getEnvelope());

    rd.setSent(true);
    Constants.store.save(rd);
    log.info("Marked sent receipt as sent");
  }

  private void addReceiptHeader(EbMessage message, ReceiptData receiptData)
  {
    MessageContext msgCtx = message.getMessageContext();
    new Messaging(msgCtx.getEnvelope());
    OMElement messaging =
       Util.getGrandChildNameNS(msgCtx.getEnvelope().getHeader(),
                                Constants.MESSAGING, Constants.NS);
    //messaging.addChild( receiptData.getSignalMessage() );
    messaging.addChild( receiptData.getSignalMessage().getElement() );
  }
}