package org.holodeck.reliability.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

/**
 * @author Hamid Ben Malek
 */
public class SequenceNumRange extends Element
{
  public SequenceNumRange(int from, int to)
  {
    super(Constants.SEQUENCE_NUM_RANGE, Constants.NS, Constants.PREFIX);
    addAttribute("from", "" + from);
    addAttribute("to", "" + to);
  }
}
