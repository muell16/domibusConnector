package org.holodeck.ebms3.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.Configuration;
import org.holodeck.ebms3.packaging.Messaging;
import org.holodeck.ebms3.packaging.PackagingFactory;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.submit.EbMessage;
import org.holodeck.ebms3.config.Leg;
import org.holodeck.common.soap.Util;


/**
 * @author Hamid Ben Malek
 */
public class Packager extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(Packager.class.getName());
  private static final Logger log = Logger.getLogger(Packager.class);
  
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( msgCtx.getFLOW() != MessageContext.OUT_FLOW  ||
         msgCtx.isServerSide() )
         return InvocationResponse.CONTINUE;

    MsgInfoSet mis =
          (MsgInfoSet)msgCtx.getProperty(Constants.MESSAGE_INFO_SET);
    if ( mis == null )
    {
      log.info("No PMode was specified for the outgoing message");
      return InvocationResponse.CONTINUE;
    }

    if ( msgCtx.getEnvelope() == null )
    {
      Leg leg = Configuration.getLeg(mis);
      double soapVersion = 1.1;
      String ver = leg.getSoapVersion();
      if ( ver != null && !ver.trim().equals("") )
           soapVersion = Double.parseDouble(ver);
      msgCtx.setEnvelope( EbMessage.createEnvelope(soapVersion) );
    }

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null)
    {
      SOAPEnvelope env = msgCtx.getEnvelope();
      header = ((SOAPFactory)env.getOMFactory()).createSOAPHeader(env);
    }

    OMElement ebMess =
       Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( ebMess != null ) return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    Messaging mess = PackagingFactory.createMessagingElement(msgCtx);
            //EbUtil.createMessagingElement(msgCtx);

    mess.addToHeader( msgCtx.getEnvelope() );
    log.info(logPrefix + " ebms3 headers were added to outgoing message");
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    return InvocationResponse.CONTINUE;
  }
}