package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

/**
 * @author Hamid Ben Malek
 */
public class ReplyPattern extends Element
{
  public ReplyPattern(String value, String ackTo)
  {
    super(Constants.REPLY_PATTERN, Constants.NS, Constants.PREFIX);
    Element val = addElement(Constants.VALUE, Constants.PREFIX);
    val.setText(value);
    if ( ackTo != null )
    {
      Element replyTo = addElement(Constants.REPLY_TO, Constants.PREFIX);
      Element uri = replyTo.addElement(Constants.BARE_URI, Constants.PREFIX);
      uri.setText(ackTo);
    }
  }

  public String getReplyPatternValue()
  {
    return getGrandChildValue(Constants.VALUE);
  }

  public String getAckTo()
  {
    return getGrandChildValue(Constants.BARE_URI);
  }
}