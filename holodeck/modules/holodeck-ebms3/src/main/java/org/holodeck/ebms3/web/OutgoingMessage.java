package org.holodeck.ebms3.web;

import org.holodeck.ebms3.persistent.UserMsgToPush;
import org.holodeck.ebms3.module.Configuration;
import org.holodeck.ebms3.config.Leg;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.common.soap.Util;

import java.util.List;

/**
 * @author Hamid Ben Malek
 */
public class OutgoingMessage implements java.io.Serializable
{
  protected String fromParty;
  //protected MsgMetadata metadata;
  //protected Party[] toParties;
  protected String toParty;
  protected String mep;
  protected String mpc;
  protected String service;
  protected String action;
  protected String toURL;
  protected boolean sent;
  protected String envelope;
  protected int legNumber;
  protected String pmode;

  public OutgoingMessage() {}
  public OutgoingMessage(UserMsgToPush msg)
  {
    if ( msg == null ) return;
    toURL = msg.getToURL();
    mep = msg.getMep();
    sent = msg.isPushed();
    legNumber = msg.getLegNumber();
    pmode = msg.getPmode();
    MsgInfoSet metadata = msg.getMsgInfoSetBean();
    List<org.holodeck.ebms3.config.Party> fromParties = null;
    if ( metadata != null )
    {
      fromParties = metadata.getFromParties();
      if ( pmode == null ) pmode = metadata.getPmode();
    }
    Leg leg = Configuration.getLeg(pmode, legNumber);
    if ( fromParties != null && fromParties.size() >  0 )
         fromParty = fromParties.get(0).getPartyId();
    else if ( leg != null && leg.getProducer() != null )
              fromParty = leg.getProducer().getParties().get(0).getPartyId();
    if ( leg != null && leg.getUserService() != null )
    {
      toParty = leg.getUserService().getToParty().getParties().get(0).getPartyId();
      service = leg.getUserService().getCollaborationInfo().getService().getValue();
      action = leg.getUserService().getCollaborationInfo().getAction();
      mpc = leg.getMpc();
    }
    envelope = Util.prettyToString(msg.getEnvelope());
  }

/*
  public MsgMetadata getMetadata() { return metadata; }
  public void setMetadata(MsgMetadata metadata)
  {
    this.metadata = metadata;
  }

  public Party[] getToParties() { return toParties; }
  public void setToParties(Party[] toParties) { this.toParties = toParties;}
*/

  public String getFromParty() { return fromParty; }
  public void setFromParty(String fromParty) { this.fromParty = fromParty; }

  public String getToParty() { return toParty; }
  public void setToParty(String toParty) { this.toParty = toParty; }

  public int getLegNumber() { return legNumber; }
  public void setLegNumber(int legNumber) { this.legNumber = legNumber; }

  public String getMep() { return mep; }
  public void setMep(String mep) { this.mep = mep; }

  public String getMpc() { return mpc; }
  public void setMpc(String mpc) { this.mpc = mpc; }

  public String getService() { return service; }
  public void setService(String service) { this.service = service; }

  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }

  public String getToURL() { return toURL; }
  public void setToURL(String toURL) { this.toURL = toURL; }

  public boolean isSent() { return sent; }
  public void setSent(boolean sent) { this.sent = sent; }

  public String getEnvelope() { return envelope; }
  public void setEnvelope(String envelope) { this.envelope = envelope; }
}