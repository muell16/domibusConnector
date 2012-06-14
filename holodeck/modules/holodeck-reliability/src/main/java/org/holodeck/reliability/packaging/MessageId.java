package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

/**
 * @author Hamid Ben Malek
 */
public class MessageId extends Element
{
   public MessageId(String groupId, String seqNumber, String groupExpiryTime)
   {
     super(Constants.MESSAGE_ID, Constants.NS, Constants.PREFIX);
     if (groupId != null) addAttribute("groupId", groupId);
     if (seqNumber != null)
     {
       Element seq = addElement(Constants.SEQUENCE_NUM, Constants.PREFIX);
       seq.addAttribute("number", seqNumber);
       if (groupExpiryTime != null)
           seq.addAttribute("groupExpiryTime", groupExpiryTime);
     }
   }

   public String getGroupId() { return getAttributeValue("groupId"); }

   public SequenceNum getSequenceNum()
   {
      return (SequenceNum)getChild(Constants.SEQUENCE_NUM,
                                   getNamespace().getPrefix());
   }
}