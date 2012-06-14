package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.Constants;

/**
 * @author Hamid Ben Malek
 */
public class SequenceNum extends Element
{
  public SequenceNum()
  {
    super(Constants.SEQUENCE_NUM, Constants.NS, Constants.PREFIX);
  }

  public SequenceNum(int seq, String groupExpiryTime,
                     String groupMaxIdleDuration, boolean last)
  {
    super(Constants.SEQUENCE_NUM, Constants.NS, Constants.PREFIX);
    setAttribute("number", "" + seq);
    if ( groupExpiryTime != null && !groupExpiryTime.trim().equals("") )
         setAttribute("groupExpiryTime", groupExpiryTime);
    if ( groupMaxIdleDuration != null && !groupMaxIdleDuration.trim().equals("") )
         setAttribute("groupMaxIdleDuration", groupMaxIdleDuration);
    if ( last ) setAttribute("last", "true");
  }

  public String getNumber() { return getAttributeValue("number"); }

  public String getGroupExpiryTime()
  {
    return getAttributeValue("groupExpiryTime");
  }

  public String getGroupMaxIdleDuration()
  {
    return getAttributeValue("groupMaxIdleDuration");
  }

  public boolean isLast()
  {
    String last = getAttributeValue("last");
    if ( last == null || last.trim().equals("") ) return false;
    if ( last.equals("true") ) return true;
    else return false;
  }
}