package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.SOAPHeader;

import org.holodeck.ebms3.module.Constants;
import org.holodeck.common.soap.Util;

/**
 *  This handler runs both on the client and server side but only during the
 *  IN_FLOW, and its job is to detect if there is any AS4 receipt present in the
 *  headers and if so, update the database to mark a message as having received
 *  a receipt
 *
 * @author Hamid Ben Malek
 */
public class ReceiptProcessor extends AbstractHandler
{
  private static final Log log =
                  LogFactory.getLog(ReceiptProcessor.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.IN_FLOW  )
         return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if ( header == null ) return InvocationResponse.CONTINUE;

    OMElement messaging =
         Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( messaging == null ) return InvocationResponse.CONTINUE;

    OMElement receipt =
         Util.getGrandChildNameNS(messaging, Constants.RECEIPT, Constants.NS);
    if ( receipt == null ) return InvocationResponse.CONTINUE;

    String refToMessageId = Util.getGrandChildValue(messaging,
                                               Constants.REF_TO_MESSAGE_ID);
    log.info(logPrefix + " Received a receipt for messageId " + refToMessageId);
    System.out.println("--- Received a receipt for messageId " + refToMessageId);

    int i = Constants.store.setReceiptReceived(refToMessageId);
    log.info(logPrefix +
       " Updated " + i + " rows by setting 'Received_Receipt' column to true");
    Constants.store.setReceipt(refToMessageId, Util.toString(messaging));

    return InvocationResponse.CONTINUE;
  }
}