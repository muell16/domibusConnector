package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;
import org.apache.axiom.om.OMElement;

/**
 * @author Hamid Ben Malek
 */
public class SignalMessage extends Element
{
  private static final long serialVersionUID = 4454856102058186344L;  

  public SignalMessage(String mpc, Error[] errors)
  {
    super(Constants.SIGNAL_MESSAGE, Constants.NS, Constants.PREFIX);
    addChild(new MessageInfo());
    if ( mpc != null && !mpc.trim().equals("") )
         addChild(new PullRequest(mpc));
    if ( errors == null || errors.length == 0 ) return;
    for (Error error : errors) addChild(error);
  }

  public SignalMessage(MessageInfo mi, String mpc, OMElement receipt,
                       Error[] errors)
  {
    super(Constants.SIGNAL_MESSAGE, Constants.NS, Constants.PREFIX);
    if ( mi != null ) addChild(mi);
    else addChild(new MessageInfo());
    if ( mpc != null && !mpc.trim().equals("") )
         addChild(new PullRequest(mpc));
    if ( receipt != null ) addChild(receipt);
    if ( errors == null || errors.length == 0 ) return;
    for (Error error : errors) addChild(error);
  }

  public SignalMessage(MessageInfo mi, Error error)
  {
    super(Constants.SIGNAL_MESSAGE, Constants.NS, Constants.PREFIX);
    if ( mi != null ) addChild(mi);
    else addChild(new MessageInfo());
    if ( error != null ) addChild(error);
  }

  public SignalMessage(Error error)
  {
    super(Constants.SIGNAL_MESSAGE, Constants.NS, Constants.PREFIX);
    addChild(new MessageInfo());
    if ( error != null ) addChild(error);
  }

  public SignalMessage(Error[] errors)
  {
    super(Constants.SIGNAL_MESSAGE, Constants.NS, Constants.PREFIX);
    addChild(new MessageInfo());
    if ( errors == null || errors.length == 0 ) return;
    for (Error error : errors) addChild(error);
  }

  public SignalMessage(String mpc)
  {
    super(Constants.SIGNAL_MESSAGE, Constants.NS, Constants.PREFIX);
    addChild(new MessageInfo());
    addChild(new PullRequest(mpc));
  }

  public SignalMessage(MessageInfo mi, String mpc)
  {
    super(Constants.SIGNAL_MESSAGE, Constants.NS, Constants.PREFIX);
    if ( mi != null ) addChild(mi);
    else addChild(new MessageInfo());
    addChild(new PullRequest(mpc));
  }
}