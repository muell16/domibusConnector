package org.holodeck.reliability.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.*;

import org.holodeck.reliability.config.Reliability;
import org.holodeck.reliability.persistent.SenderGroup;
import org.holodeck.reliability.packaging.*;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.common.soap.Util;

import java.util.Date;

/**
 * @author Hamid Ben Malek
 */
public class Packager extends AbstractHandler
{
  private static final Log log =
                         LogFactory.getLog(Packager.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if (msgCtx.isServerSide() || msgCtx.getFLOW() != MessageContext.OUT_FLOW)
        return InvocationResponse.CONTINUE;

    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix + msgCtx.getEnvelope().getHeader());

    //if ( wsrmReqExists(msgCtx) ) return InvocationResponse.CONTINUE;
    if ( shouldNotProcess(msgCtx) ) return InvocationResponse.CONTINUE;
    createReliabilityHeader(msgCtx);

    //log.debug(logPrefix + "End of invoke: " + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    return InvocationResponse.CONTINUE;
  }

  public void createReliabilityHeader(MessageContext msgCtx)
  {
    Reliability quality = getReliability(msgCtx);
    SOAPFactory factory = getFactory(msgCtx);
    DbStore store = getStore(msgCtx);
    SOAPEnvelope env = msgCtx.getEnvelope();
    SenderGroup group = getGroup(getGroupName(msgCtx), store);
    if ( group != null && group.getQuality() == null )
         group.setQuality(quality);
    Reliability quality2 = (group == null ? quality : group.getQuality());

    OMNamespace ns = factory.createOMNamespace(Constants.NS, "wsrm");
    SOAPHeader soapHeader = env.getHeader();
    if (soapHeader == null) soapHeader = factory.createSOAPHeader(env);
    env.declareNamespace(ns);
    SOAPHeaderBlock req = soapHeader.addHeaderBlock(Constants.REQUEST, ns);
    req.setMustUnderstand(true);

    // continue filling the req element from the reliablity object....
    MessageId id = createMessageId(group, store);
    String msgExpiryTime = getMessageExpiryTime(group);
    if ( group != null )
         group.setMaxMessageExpiryTime(Util.utcToDate(msgExpiryTime));
    ReplyPattern rp = new ReplyPattern(quality2.getAckReply(), quality2.getAckTo());

    OMElement msgExp = factory.createOMElement(Constants.EXPIRY_TIME, ns);
    msgExp.setText(msgExpiryTime);

    //req.addChild(id);
    req.addChild(id.getElement());

    req.addChild(msgExp);
    //req.addChild(rp);
    req.addChild(rp.getElement());

    if ( quality2.isAtLeastOnce() )
         req.addChild(factory.createOMElement(Constants.ACK_REQUESTED, ns));
    if ( quality2.isAtMostOnce() )
         req.addChild(factory.createOMElement(Constants.DUPLICATE_ELIMINATION, ns));
    if ( quality2.isInOrder() && group != null )
         req.addChild(factory.createOMElement(Constants.MESSAGE_ORDER, ns));

    // Put the quality as a property in the message context (next handler)
    // would need it:
    msgCtx.setProperty(Constants.QUALITY, quality2);
  }

  private MessageId createMessageId(SenderGroup group, DbStore store)
  {
    String groupId = null;
    int seqNumber = -1;
    String groupExpiryTime = null;
    if ( group != null )
    {
      seqNumber = group.nextSequenceNumber();
      groupId = group.getGroupId();
      groupExpiryTime = group.getGroupExpiryTime();
      group.setLastMessageTimestamp(new Date());
      store.save(group);
    }
    else groupId = SenderGroup.newGroupId(this);
    String seq = (seqNumber == -1 ? null : "" + seqNumber);

    return new MessageId(groupId, seq, groupExpiryTime);
  }
  private SenderGroup getGroup(String groupName, DbStore store)
  {
    SenderGroup group = null;
    if ( groupName != null && !groupName.trim().equals("") )
    {
      group = store.getGroupByName(groupName);
      if ( group != null && group.isClosed() )
      {
        // According to the spec, we should notify the Producer that
        // the group is closed and the submitted message should not be sent.
        // For now, we will just use a singleton instead and let the message
        // go... (to be corrected once we have a Producer-notification
        // mechanism).
        return null;
      }
      if (group == null) group = new SenderGroup(groupName);
    }
    return group;
  }
  private Reliability getReliability(MessageContext msgCtx)
  {
    Reliability reliability = null;
    Object value = msgCtx.getProperty(Constants.QUALITY);
    if ( value instanceof String )
    {
      String qualityValue = (String)value;
      Parameter parameter = msgCtx.getParameter(qualityValue);
      if (parameter != null) reliability = (Reliability)parameter.getValue();
    }
    else if ( value instanceof Reliability )
              reliability = (Reliability)value;

    if (reliability == null) reliability = new Reliability();
    return reliability;
  }

  private String getGroupName(MessageContext msgCtx)
  {
    return (String)msgCtx.getProperty(Constants.GROUP_NAME);
  }
  private DbStore getStore(MessageContext msgCtx)
  {
    return (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
  }
  private SOAPFactory getFactory(MessageContext msgCtx)
  {
    return (SOAPFactory)msgCtx.getEnvelope().getOMFactory();
  }
  private String getMessageExpiryTime(SenderGroup group)
  {
    if (group != null) return group.getMessageExpiryTime(new Date());
    Parameter msgLifeTime =
            Constants.getAxisModule().getParameter("MessageLifetime");
    return SenderGroup.getUTC(new Date(), (String)msgLifeTime.getValue());
  }

  private boolean shouldNotProcess(MessageContext msgCtx)
  {
    if (msgCtx == null) return true;
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return false;
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    if ( wsrmReq != null ) return true;

    OMElement pollReq =
       Util.getGrandChildNameNS(header, Constants.POLL_REQUEST, Constants.NS);
    OMElement wsrmResp =
       Util.getGrandChildNameNS(header, Constants.RESPONSE, Constants.NS);
    if ( pollReq != null || wsrmResp != null )
    {
      if ( msgCtx.getProperty(Constants.QUALITY) == null &&
           msgCtx.getProperty(Constants.GROUP_NAME) == null )
           return true;
    }
    return false;
  }
}