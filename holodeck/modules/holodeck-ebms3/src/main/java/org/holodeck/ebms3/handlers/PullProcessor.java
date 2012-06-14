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
//import org.holodeck.ebms3.pmodes.*;
import org.holodeck.ebms3.config.*;
import org.holodeck.ebms3.packaging.*;
import org.holodeck.ebms3.submit.MsgInfoSet;

import java.util.*;

/**
 *  This handler runs only on the server side, and it processes an incoming
 *  PullRequest message
 *
 * @author Hamid Ben Malek
 */
public class PullProcessor extends AbstractHandler
{
  private static final Log log =
                  LogFactory.getLog(PullProcessor.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() ) return InvocationResponse.CONTINUE;
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    if (msgCtx.getFLOW() == MessageContext.IN_FLOW)
    {
      OMElement ebms3Pull =
       Util.getGrandChildNameNS(header, Constants.PULL_REQUEST, Constants.NS);
      OMElement ebMess =
       Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
      if (ebms3Pull == null) return InvocationResponse.CONTINUE;
      else
      {
        String mpc = Util.getAttributeValue(ebms3Pull, "mpc");
        String msgId = Util.getGrandChildValue(ebMess, Constants.MESSAGE_ID);
        String address = msgCtx.getTo().getAddress();
        PMode pmode =
            Configuration.getPMode(Constants.ONE_WAY_PULL, mpc, address);
        log.info(logPrefix + "Received a PullRequest message on mpc: " + mpc);
        if ( pmode == null )
        {
          // generate an ebms3 mismatch pmode error and send it:
          org.holodeck.ebms3.packaging.Error pmodeMismatch =
            org.holodeck.ebms3.packaging.Error.getProcessingModeMismatchError(msgId);
          SignalMessage modeMismatch =
             new SignalMessage(new MessageInfo(msgId), pmodeMismatch);
          SOAPFactory factory = (SOAPFactory)msgCtx.getEnvelope().getOMFactory();
          SOAPEnvelope env = factory.getDefaultEnvelope();
          new Messaging(env, modeMismatch, null);
          Util.sendResponse(env, msgCtx);
          log.info(logPrefix + "Mode Mismatch Error is being sent as response to the pull");
          return InvocationResponse.ABORT;
        }

        // ToDo: need to verify if the pull request is authorized.
        // Only when it is authorized, then proceed ...

        // Retrieve the user message from the mpc queue and send it as response.
        // If the queue is empty, then let the request propagate to the service
        // who may send back a user message as a response to the pull request:
        DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
        UserMsgToPull message = store.getNextUserMsgToPull(mpc);
        if ( message != null )
        {
          //MessageContext resp =
          //    message.getMessageContext(msgCtx.getConfigurationContext());
          message.setConfigurationContext(msgCtx.getConfigurationContext());
          MessageContext resp = message.getMessageContext();

          log.info(logPrefix + "Found UserMessage to pull from DB for mpc " + mpc);
          MsgInfoSet mis = message.getMsgInfoSetBean();
          resp.setProperty(Constants.MESSAGE_INFO_SET, mis);
          resp.setProperty("attachments", message.getAttachments());
          Messaging ebM = PackagingFactory.createMessagingElement(resp);
          ebM.addToHeader(resp.getEnvelope());
          log.info(logPrefix + "ebms headers have been added to pulled user message");
          Util.sendResponse(resp, msgCtx);
          log.info(logPrefix +
                  "A UserMessage is being sent as response to the pull request");
          Util.debug(log, logPrefix, resp.getEnvelope().getHeader());
          return InvocationResponse.ABORT;
        }
        else log.info(logPrefix + "No UserMessage found in DB to pull, " +
        "so continuing until the final partyId decides what message to be pulled");  

        msgCtx.setProperty(Constants.IN_PULL_REQUEST, ebms3Pull);
        msgCtx.setProperty(Constants.IN_MESSAGING, ebMess);
        msgCtx.setProperty(Constants.TO_ADDRESS, msgCtx.getTo().getAddress());
        // ToDo: try to read the wss:Security actor="ebms" element and store it
        // as well in the message context in case the MPC requires
        // authorization ..

        return InvocationResponse.CONTINUE;
      }
    }

    if ( msgCtx.getFLOW() != MessageContext.OUT_FLOW )
         return InvocationResponse.CONTINUE;

    OMElement pullReq =
     (OMElement)Util.getPropertyFromInMsgCtx(msgCtx, Constants.IN_PULL_REQUEST);
    if (pullReq == null) return InvocationResponse.CONTINUE;

    OMElement ebMess =
     (OMElement)Util.getPropertyFromInMsgCtx(msgCtx, Constants.IN_MESSAGING);

    // Check if the SOAP body is empty. In this case, we should send
    // an ebms3 warning of type "Empty MPC":
    SOAPBody body = msgCtx.getEnvelope().getBody();
    Iterator it = body.getChildElements();
    if ( it == null || !it.hasNext() )
    {
      String ref = Util.getGrandChildValue(ebMess, Constants.MESSAGE_ID);
      org.holodeck.ebms3.packaging.Error emptyMPC =
          org.holodeck.ebms3.packaging.Error.getEmptyPartitionError(ref);
      SignalMessage ebWarn =
              new SignalMessage(new MessageInfo(ref), emptyMPC);
      new Messaging(msgCtx.getEnvelope(), ebWarn, null);
      return InvocationResponse.CONTINUE;
    }

    String mpc = Util.getAttributeValue(pullReq, "mpc");
    //log.debug(logPrefix + "mpc is " + mpc);
    String msgId = Util.getGrandChildValue(ebMess, Constants.MESSAGE_ID);
    String address =
        (String)Util.getPropertyFromInMsgCtx(msgCtx, Constants.TO_ADDRESS);

    PMode pm = Configuration.getPMode(Constants.ONE_WAY_PULL, mpc, address);
    Producer producer = Configuration.getLeg(pm.getName(), 2).getProducer();
    UserService us = Configuration.getLeg(pm.getName(), 2).getUserService();
    String pmode = pm.getName();

    UserMessage userMsg =
          PackagingFactory.createUserMessage(mpc, msgId, producer, pmode, us,
                                   msgCtx.getAttachmentMap());
    //log.debug(logPrefix + userMsg.toString());
    new Messaging(msgCtx.getEnvelope(), null, userMsg);

    return InvocationResponse.CONTINUE;
  }
}