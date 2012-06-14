package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;
import org.holodeck.reliability.persistent.SenderGroup;
import org.holodeck.reliability.config.Reliability;

import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;

import java.util.Date;

/**
 * @author Hamid Ben Malek
 */
public class Request extends Element
{
 /**
  * group and msgLifeTime are optional. msgLifeTime should be read from
  * module.xml, while the group object is computed from the property groupName
  * value stored in MessageContext. If the groupName is null, the message
  * would be constructed as a singleton.
  */
  public static SOAPHeaderBlock createRequest(SOAPFactory factory,
                                              Reliability quality,
                                              SenderGroup group,
                                              String msgLifeTime)
  {
    if (factory == null) return null;
    OMNamespace ns =
          factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    SOAPHeaderBlock req =
          factory.createSOAPHeaderBlock(Constants.REQUEST, ns);
    req.setMustUnderstand(true);

    MessageId id = createMessageId(group);
    String msgExpiryTime = getMessageExpiryTime(msgLifeTime, group);
    ReplyPattern rp =
         new ReplyPattern(quality.getAckReply(), quality.getAckTo());

    OMElement msgExp = factory.createOMElement(Constants.EXPIRY_TIME, ns);
    msgExp.setText(msgExpiryTime);

    //req.addChild(id);
    req.addChild(id.getElement());

    req.addChild(msgExp);
    //req.addChild(rp);
    req.addChild(rp.getElement());

    if ( quality.isAtLeastOnce() )
         req.addChild(factory.createOMElement(Constants.ACK_REQUESTED, ns));
    if ( quality.isAtMostOnce() )
         req.addChild(factory.createOMElement(Constants.DUPLICATE_ELIMINATION, ns));
    if ( quality.isInOrder() )
         req.addChild(factory.createOMElement(Constants.MESSAGE_ORDER, ns));

    return req;
  }

  private static MessageId createMessageId(SenderGroup group)
  {
    String groupId = null;
    int seqNumber = -1;
    String groupExpiryTime = null; 
    if ( group != null )
    {
      seqNumber = group.nextSequenceNumber();
      groupId = group.getGroupId();
      groupExpiryTime = group.getGroupExpiryTime();
    }
    else groupId = SenderGroup.newGroupId(null);
    String seq = (seqNumber == -1 ? null : "" + seqNumber);

    return new MessageId(groupId, seq, groupExpiryTime);
  }
  private static String getMessageExpiryTime(String msgLifeTime,
                                             SenderGroup group)
  {
    if (group != null) return group.getMessageExpiryTime(new Date());
    if (msgLifeTime == null || msgLifeTime.trim().equals(""))
        msgLifeTime = "P29D";
    return SenderGroup.getUTC(new Date(), msgLifeTime);
  }
}