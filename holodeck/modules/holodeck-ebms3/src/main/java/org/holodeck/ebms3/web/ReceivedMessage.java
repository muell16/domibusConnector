package org.holodeck.ebms3.web;

import org.holodeck.ebms3.persistent.ReceivedUserMsg;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.common.soap.Util;

/**
 * @author Hamid Ben Malek
 */
public class ReceivedMessage implements java.io.Serializable
{
  private static final long serialVersionUID = 7109238746582871436L;  

  protected String messageId = null;
  protected String refToMessageId = null;
  protected String mpc = null;
  protected String fromParty = null;
  protected String toParty = null;
  protected String service = null;
  protected String action = null;
  protected String envelope;

  public ReceivedMessage() {}

  public ReceivedMessage(ReceivedUserMsg msg)
  {
    if ( msg == null ) return;
    this.messageId = msg.getMessageId();
    this.refToMessageId = msg.getRefToMessageId();
    this.mpc = msg.getMpc();
    this.fromParty = msg.getFromParty();
    this.toParty = msg.getToParty();
    this.service = msg.getService();
    this.action = msg.getAction();
    msg.setConfigurationContext(Constants.configContext);
    this.envelope = Util.prettyToString(msg.getMessageContext().getEnvelope());
  }

  public String getMessageId() { return messageId; }
  public void setMessageId(String messageId) { this.messageId = messageId; }

  public String getRefToMessageId() { return refToMessageId; }
  public void setRefToMessageId(String refToMessageId)
  {
    this.refToMessageId = refToMessageId;
  }

  public String getMpc() { return mpc; }
  public void setMpc(String mpc) { this.mpc = mpc; }

  public String getFromParty() { return fromParty; }
  public void setFromParty(String fromParty) { this.fromParty = fromParty; }

  public String getToParty() { return toParty; }
  public void setToParty(String toParty) { this.toParty = toParty; }

  public String getService() { return service; }
  public void setService(String service) { this.service = service; }

  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }

  public String getEnvelope() { return envelope; }
  public void setEnvelope(String envelope) { this.envelope = envelope; }
}