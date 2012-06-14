package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

/**
 * @author Hamid Ben Malek
 */
public class ReplyRange extends Element
{
  public ReplyRange(int from, int to, String fault)
  {
    super(Constants.REPLY_RANGE, Constants.NS, Constants.PREFIX);
    addAttribute("from", "" + from);
    addAttribute("to", "" + to);
    if (fault != null && !fault.trim().equals(""))
        addAttribute("fault", fault);
  }

  public String getFrom() { return getAttributeValue("from"); }
  public String getTo() { return getAttributeValue("to"); }
  public String getFault() { return getAttributeValue("fault"); }
}