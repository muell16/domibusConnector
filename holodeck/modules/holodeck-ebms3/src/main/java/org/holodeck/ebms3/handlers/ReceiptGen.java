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
import org.holodeck.ebms3.config.Leg;
import org.holodeck.common.soap.Util;

import java.util.List;
import java.util.ArrayList;

/**
 *  This handler runs only during the In_FLOW (client or server side), and
 *  its job is to generate an AS4 Receipt when the incoming message has
 *  a pmode that asks for AS4 receipts. If the receipt is expected to be sent
 *  out on the back channel (as a response), the receipt is not saved to database,
 *  but it is only placed on the context. If, however the receipt is expected
 *  to be sent out as a callback, then the receipt is saved to database.
 *
 * @author Hamid Ben Malek
 */
public class ReceiptGen extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(ReceiptGen.class.getName());
  private static final Logger log = Logger.getLogger(ReceiptGen.class.getName());
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW  )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    OMElement header = (OMElement)msgCtx.getProperty(Constants.IN_SOAP_HEADER);
    List<OMElement> signatures =
        Util.getGrandChildrenNameNS(header, "Signature", Constants.dsigNS);

    List<OMElement> references = null;
    if ( signatures != null && signatures.size() > 0 )
    {
      for (OMElement sig : signatures)
      {
        List<OMElement> refs =
           Util.getGrandChildrenNameNS(sig, "Reference", Constants.dsigNS);
        if ( refs != null ) 
        {
          if ( references == null ) references = new ArrayList<OMElement>();
          //references.addAll(refs);
          for (OMElement e : refs) references.add(e.cloneOMElement());
        }
      }
    }
    OMElement messaging =
      Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( messaging == null ) return InvocationResponse.CONTINUE;
    String messageId =
          Util.getGrandChildValue(messaging, Constants.MESSAGE_ID);
    if ( messageId == null || messageId.trim().equals("") )
         return InvocationResponse.CONTINUE;
    OMElement userMsg =
      Util.getGrandChildNameNS(messaging, Constants.USER_MESSAGE, Constants.NS);
    if ( userMsg == null ) return InvocationResponse.CONTINUE;

    ReceiptData receiptData = new ReceiptData(messageId, references);
    if ( !msgCtx.isServerSide() )
    {
      // set the toURL before saving the receipt to database
      String receiptTo =
           (String)Util.getPropertyFromOutMsgCtx(msgCtx, Constants.RECEIPT_TO);
      receiptData.setToURL(receiptTo);
      Constants.store.save(receiptData);
      log.info(logPrefix + " A Receipt was generated and stored in database");
    }
    else
    {
      boolean expectReceipt =
        (Boolean)msgCtx.getProperty(org.holodeck.common.Constants.EXPECT_RECEIPT);
      if ( expectReceipt )
      {
        msgCtx.setProperty("ReceiptData", receiptData);
        log.info(logPrefix +
         " A receipt was created and placed in the message context so that it will be included on the back channel");
      }
      else
      {
        Leg requestLeg = (Leg)msgCtx.getProperty(Constants.IN_LEG);
        if ( requestLeg != null && requestLeg.getAs4Receipt() != null )
        {
          receiptData.setToURL( requestLeg.getReceiptTo() );
          Constants.store.save(receiptData);
          log.info(logPrefix +
                    " A Receipt was generated and stored in database");
        }
        else log.info(logPrefix +
                " No receipt generated because the request's pmode does not ask for receipts");
      }
    }
      
    return InvocationResponse.CONTINUE;
  }
}