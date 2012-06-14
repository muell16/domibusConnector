package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.ebms3.module.*;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.persistent.MsgIdCallback;

//import org.holodeck.ebms3.pmodes.Binding;
//import org.holodeck.ebms3.pmodes.PMode;

import org.holodeck.common.soap.Util;

//import java.util.Map;

/**
 *  This handler writes a MsgIdCallback object to the database when the outgoing
 *  UserMessage is the first leg of a Two-Way/Push-And-Push or a
 *  Two-Way/Push-And-Pull MEP and the callbackClass property of MsgInfoSet is not
 *  null. 
 *
 * @author Hamid Ben Malek
 */
public class CallbackPersist extends AbstractHandler
{
  private static final Log log =
                  LogFactory.getLog(CallbackPersist.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.OUT_FLOW  )
         return InvocationResponse.CONTINUE;
    if ( msgCtx.isServerSide() )
         return InvocationResponse.CONTINUE;

    MsgInfoSet mis =
          (MsgInfoSet)msgCtx.getProperty(Constants.MESSAGE_INFO_SET);
    if ( mis == null ) return InvocationResponse.CONTINUE;
    String callbackClass = mis.getCallbackClass();
    if ( callbackClass == null || callbackClass.trim().equals("") )
    {
      log.info(logPrefix +
        "Outgoing message has no callback, therefore MsgIdCallback will not be saved to database");
      return InvocationResponse.CONTINUE;
    }
    String pmode = mis.getPmode();
    int legNumber = mis.getLegNumber();
    // get the pmode mep, and proceed only if the mep is either
    // two-way/push-and-push with leg = 1 or mep is
    // two-way/push-and-pull with leg = 1

    String mep = Configuration.getMep(mis.getPmode());

    if ( (!mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PUSH) ||
           legNumber != 1
         ) &&
         (!mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PULL) ||
           legNumber != 1
         )
       )
       return InvocationResponse.CONTINUE;

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;

    OMElement ebMess =
       Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( ebMess == null ) return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);

    OMElement userMessage =
      Util.getGrandChildNameNS(header, Constants.USER_MESSAGE, Constants.NS);
    if ( userMessage == null ) return InvocationResponse.CONTINUE;

    String messageId =
          Util.getGrandChildValue(userMessage, Constants.MESSAGE_ID);
    if ( messageId == null || messageId.trim().equals("") )
         return InvocationResponse.CONTINUE;

    MsgIdCallback micb =
         new MsgIdCallback(messageId, pmode, legNumber, callbackClass);
    Constants.store.save(micb);
    log.info(logPrefix + " saved MsgIdCallback in database");
    return InvocationResponse.CONTINUE;
  }

  /* change
  private static Binding getBinding(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    Map<String, PMode> pmodesMap = Constants.pmodesMap;
    PMode pmode = pmodesMap.get(metadata.getPmode());
    return pmode.getBinding();
  }
  */
}