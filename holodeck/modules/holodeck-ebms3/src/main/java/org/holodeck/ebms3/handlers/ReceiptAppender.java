package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.axiom.om.*;

import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.persistent.ReceiptData;
import org.holodeck.ebms3.packaging.Messaging;
import org.holodeck.common.soap.Util;

/**
 *  This handler runs only on the server side during the OUT_FLOW, and its
 *  job is to append a receipt to the outgoind message on the back channel
 *  when the initial request expects a receipt to be sent as a "Response".
 *  If no receipt is found in the message context of the previous incoming
 *  request message, then no receipt will be appended in the outgoing response
 *
 * @author Hamid Ben Malek
 */
public class ReceiptAppender extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(ReceiptAppender.class.getName());
  private static final Logger log = Logger.getLogger(ReceiptAppender.class.getName());
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() || msgCtx.getFLOW() != MessageContext.OUT_FLOW )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);

    ReceiptData receiptData =
        (ReceiptData)Util.getPropertyFromInMsgCtx(msgCtx, "ReceiptData");
    if ( receiptData == null )
        log.info(logPrefix +
                  " No ReceiptData found in previous message context");
    else log.info(logPrefix + " ReceiptData found in previous message context");
    if ( receiptData == null ) return InvocationResponse.CONTINUE;

    OMElement messaging =
        Util.getGrandChildNameNS(msgCtx.getEnvelope().getHeader(),
                                 Constants.MESSAGING, Constants.NS);
    boolean expectReceipt =
      (Boolean)Util.getPropertyFromInMsgCtx(msgCtx,
                                  org.holodeck.common.Constants.EXPECT_RECEIPT);
    if ( !expectReceipt )
    {  
      log.info(logPrefix +
          " Receipt will not be appended to outgoing response because no receipt is expected");
      return InvocationResponse.CONTINUE;
    }
 
    if ( messaging == null )
    {
      new Messaging(msgCtx.getEnvelope());
      messaging = Util.getGrandChildNameNS(msgCtx.getEnvelope().getHeader(),
                                 Constants.MESSAGING, Constants.NS);
    }
    OMElement signalMsg =
         Util.getGrandChildNameNS(messaging,
                                  Constants.SIGNAL_MESSAGE, Constants.NS);
    if ( signalMsg == null )
         messaging.addChild( receiptData.getSignalMessage().getElement() );
         //messaging.addChild( receiptData.getSignalMessage() );
            
    // here we are assuming that this response contains an eb:RefToMessageId
    // that is identical to the messageId of the request
    else signalMsg.addChild( receiptData.getReceipt().getElement() );
        //signalMsg.addChild( receiptData.getReceipt() );
    log.info(logPrefix +
              " receipt appended to outgoing message on the back channel: ");
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    return InvocationResponse.CONTINUE;
  }
}