package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;
import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class SequenceReplies extends Element
{
  public SequenceReplies(String groupId, ReplyRange[] ranges)
  {
    super(Constants.SEQUENCE_REPLIES, Constants.NS, Constants.PREFIX);
    addAttribute("groupId", "" + groupId);
    if (ranges != null && ranges.length > 0)
    {
      for (ReplyRange range : ranges)
      {
        if (range != null) addChild(range);
      }
    }
  }
  public SequenceReplies(String groupId, List<ReplyRange> ranges)
  {
    super(Constants.SEQUENCE_REPLIES, Constants.NS, Constants.PREFIX);
    addAttribute("groupId", "" + groupId);
    if (ranges != null && ranges.size() > 0)
    {
      for (ReplyRange range : ranges)
      {
        if (range != null) addChild(range);
      }
    }
  }

  public void addReplyRange(ReplyRange range)
  {
    if (range != null) addChild(range);
  }

  public String getGroupId() { return getAttributeValue("groupId"); }
}