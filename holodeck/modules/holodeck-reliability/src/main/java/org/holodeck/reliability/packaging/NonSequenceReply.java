package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

/**
 * @author Hamid Ben Malek
 */
public class NonSequenceReply extends Element
{
  public NonSequenceReply(String groupId, String fault)
  {
    super(Constants.NON_SEQUENCE_REPLY, Constants.NS, Constants.PREFIX);
    addAttribute("groupId", "" + groupId);
    if (fault != null && !fault.trim().equals(""))
        addAttribute("fault", fault);
  }

  public String getGroupId() { return getAttributeValue("groupId"); }
  public String getFault() { return getAttributeValue("fault"); }
}