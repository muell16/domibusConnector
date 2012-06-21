package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;
import org.apache.axiom.attachments.Attachments;

//import org.holodeck.ebms3.persistent.*;
import org.holodeck.ebms3.module.*;
import org.holodeck.ebms3.packaging.PackagingFactory;

import org.holodeck.ebms3.config.Producer;
import org.holodeck.ebms3.config.UserService;
import org.holodeck.ebms3.config.Leg;
//import org.holodeck.ebms3.pmodes.Producer;
//import org.holodeck.ebms3.pmodes.UserService;
//import org.holodeck.ebms3.pmodes.Leg;
//import org.holodeck.ebms3.pmodes.PModesLoader;

import org.holodeck.ebms3.packaging.UserMessage;
import org.holodeck.ebms3.packaging.Messaging;
import org.holodeck.common.soap.Util;

/**
 *  This handler runs only on the server side, and its main job is to populate
 *  the ebms3 header of a sync response that contains a UserMessage.
 *
 * @author Hamid Ben Malek
 */
public class ResponsePackager extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(ResponsePackager.class.getName());
  private static final Logger log = Logger.getLogger(ResponsePackager.class.getName());
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() ) return InvocationResponse.CONTINUE;
    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);

    if ( msgCtx.getFLOW() == MessageContext.IN_FLOW )
    {
      SOAPHeader header = msgCtx.getEnvelope().getHeader();
      if (header == null) return InvocationResponse.CONTINUE;

      OMElement ebMess =
         Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
      if (ebMess == null) return InvocationResponse.CONTINUE;

      //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
      Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

      MsgInfo msgInfo = (MsgInfo)msgCtx.getProperty(Constants.IN_MSG_INFO);
      if ( msgInfo == null ) msgInfo = EbUtil.createMsgInfo(msgCtx);
      msgCtx.setProperty(Constants.IN_MSG_INFO, msgInfo);
      return InvocationResponse.CONTINUE;
    }

    if ( msgCtx.getFLOW() == MessageContext.OUT_FLOW )
    {
      log.info(logPrefix + "processing out_flow");
      MsgInfo msgInfo =
         (MsgInfo)Util.getPropertyFromInMsgCtx(msgCtx, Constants.IN_MSG_INFO);
      if ( msgInfo == null )
      {
        log.info(logPrefix + "could not find msgInfo from previous in request");
        return InvocationResponse.CONTINUE;
      }
      if ( msgInfo.getService() != null )
      {
        log.debug(logPrefix + "msgInfo and its service are not null");
         // construct a UserMessage header and add it to the SOAP header...
        Leg leg = Configuration.getLeg(msgInfo, 2, Constants.TWO_WAY_SYNC);
        if ( leg == null )
        {
          log.info("this is not a two-way/sync mep");
          // check if previous request is expecting a receipt or an ack:
          boolean expectReceipt =
            (Boolean)Util.getPropertyFromInMsgCtx(msgCtx, org.holodeck.common.Constants.EXPECT_RECEIPT);
          // insert an empty eb:Messsaging header here...
          if ( expectReceipt ) new Messaging( msgCtx.getEnvelope() );
          return InvocationResponse.CONTINUE;
        }
        Producer producer = leg.getProducer();
        UserService us = leg.getUserService();
        Attachments att = msgCtx.getAttachmentMap();
        UserMessage userMessage =
          PackagingFactory.createRespUserMessage(msgInfo, producer, us, att);
        new Messaging(msgCtx.getEnvelope(), null, userMessage);
        Util.debug(log, logPrefix, msgCtx.getEnvelope());
      }
      else log.debug(logPrefix + "found msgInfo but its service is null");
    }

    return InvocationResponse.CONTINUE;
  }
}