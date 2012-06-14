package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

/**
 * @author Hamid Ben Malek
 */
public class ReplyTo extends Element
{
  public ReplyTo(String refScheme, String addressURI)
  {
    super(Constants.REPLY_TO, Constants.NS, Constants.PREFIX);
    if (refScheme != null && !refScheme.trim().equals(""))
        addAttribute("reference-scheme", refScheme);
    if (addressURI != null && !addressURI.trim().equals(""))
    {
      Element uri = addElement(Constants.BARE_URI, Constants.PREFIX);
      uri.setText(addressURI);
    }
  }

  public String getReferenceScheme()
  {
    return getAttributeValue("reference-scheme");
  }

  public String getBareURI() { return this.getGrandChildValue("BareURI"); }
}