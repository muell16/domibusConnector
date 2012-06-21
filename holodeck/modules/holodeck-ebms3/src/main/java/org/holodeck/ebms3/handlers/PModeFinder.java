package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.axiom.soap.*;
import org.apache.axiom.om.OMElement;
import org.holodeck.ebms3.config.Leg;
import org.holodeck.ebms3.config.PMode;
import org.holodeck.ebms3.module.Configuration;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.module.EbUtil;
import org.holodeck.common.soap.Util;

/**
 *  This handler runs only on the server side during the IN_FLOW, and its job
 *  is to determine the PMode and Leg of the incoming request message and store
 *  such information in the message context
 *
 * @author Hamid Ben Malek
 */
public class PModeFinder extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(PModeFinder.class.getName());
  private static final Logger log = Logger.getLogger(PModeFinder.class.getName());
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() || msgCtx.getFLOW() != MessageContext.IN_FLOW )
        return InvocationResponse.CONTINUE;

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);

    Leg leg = Configuration.getLegFromServerSideReq(msgCtx);
    if ( leg == null ) log.info(logPrefix +
         "Could not determine the leg of the incoming request message");

    msgCtx.setProperty(Constants.IN_LEG, leg);
    if ( leg != null && leg.getReceiptReply()!= null &&
         leg.getReceiptReply().equalsIgnoreCase("Response") )
    {
      msgCtx.setProperty(org.holodeck.common.Constants.EXPECT_RECEIPT, true);
      log.info(logPrefix +
        "This incoming request message expects an AS4 Receipt on the back the channel");
    }
    else
    {
      log.info(logPrefix +
        "This incoming request message does not expect an AS4 Receipt on the back the channel");
      msgCtx.setProperty(org.holodeck.common.Constants.EXPECT_RECEIPT, false);
    }

    PMode pmode = getPModeFromServerSideReq(msgCtx);
    if ( pmode == null ) log.info(logPrefix +
         "Could not determine the pmode of the incoming request message");
    else log.info(logPrefix +
                   "The incoming request is using pmode " + pmode.getName());
    msgCtx.setProperty(Constants.IN_PMODE, pmode);

    return InvocationResponse.CONTINUE;
  }

  private static PMode getPModeFromServerSideReq(MessageContext requestMsgCtx)
  {
    if ( requestMsgCtx == null ) return null;
    SOAPHeader header = requestMsgCtx.getEnvelope().getHeader();
    if ( header == null ) return null;
    PMode pmode;
    String address = requestMsgCtx.getTo().getAddress();
    requestMsgCtx.setProperty(Constants.TO_ADDRESS, address);

    OMElement pullReq =
      Util.getGrandChildNameNS(header,
                               Constants.PULL_REQUEST, Constants.NS);
    if ( pullReq != null )
    {
      String mpc = Util.getAttributeValue(pullReq, "mpc");
      pmode = Configuration.matchPMode(mpc, address);
      if ( pmode != null ) return pmode;
    }
    else
    {
      OMElement userMessage =
         Util.getGrandChildNameNS(header,
                                  Constants.USER_MESSAGE, Constants.NS);
      if ( userMessage == null ) return null;
      String pm =
         Util.getGrandChildAttributeValue(userMessage,
                                          Constants.AGREEMENT_REF, "pmode");
      if ( pm != null )
      {
        pmode = Configuration.getPMode(pm);
        return pmode;
      }

      MsgInfo mi = (MsgInfo)requestMsgCtx.getProperty(Constants.IN_MSG_INFO);
      if ( mi == null )
      {
        mi = EbUtil.createMsgInfo(requestMsgCtx);
        requestMsgCtx.setProperty(Constants.IN_MSG_INFO, mi);
      }
      pmode = Configuration.match(mi, address);
      if ( pmode != null ) return pmode;
    }
    return pmode;
  }
}