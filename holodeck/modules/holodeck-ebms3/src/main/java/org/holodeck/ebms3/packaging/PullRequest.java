package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;

/**
 * @author Hamid Ben Malek
 */
public class PullRequest extends Element
{
  private static final long serialVersionUID = 5027936549906007202L;  

  public PullRequest()
  {
    super(Constants.PULL_REQUEST, Constants.NS, Constants.PREFIX);
  }

  public PullRequest(String mpc)
  {
    this();
    addAttribute("mpc", mpc);
  }

  public String getPartition() { return getAttributeValue("mpc"); }
  public void setPartition(String mpc) { setAttribute("mpc", mpc); }
}