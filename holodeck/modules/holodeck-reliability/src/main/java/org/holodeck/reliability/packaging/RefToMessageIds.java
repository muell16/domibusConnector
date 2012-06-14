package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;
import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class RefToMessageIds extends Element
{
  public RefToMessageIds(String groupId, SequenceNumRange[] ranges)
  {
    super(Constants.REF_TO_MESSAGE_IDS, Constants.NS, Constants.PREFIX);
    addAttribute("groupId", "" + groupId);
    if (ranges != null && ranges.length > 0)
    {
      for (SequenceNumRange range : ranges)
      {
        if (range != null) addChild(range);
      }
    }
  }
  public RefToMessageIds(String groupId, List<SequenceNumRange> ranges)
  {
    super(Constants.REF_TO_MESSAGE_IDS, Constants.NS, Constants.PREFIX);
    addAttribute("groupId", "" + groupId);
    if (ranges != null && ranges.size() > 0)
    {
      for (SequenceNumRange range : ranges)
      {
        if (range != null) addChild(range);
      }
    }
  }

  public void addSequenceNumRange(int from, int to)
  {
    SequenceNumRange range = new SequenceNumRange(from, to);
    addChild(range);
  }

  public String getGroupId() { return getAttributeValue("groupId"); }
}