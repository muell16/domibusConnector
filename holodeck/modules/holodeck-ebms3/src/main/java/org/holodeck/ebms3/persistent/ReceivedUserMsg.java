package org.holodeck.ebms3.persistent;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import org.holodeck.ebms3.submit.EbMessage;
import org.holodeck.ebms3.config.Party;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.module.Constants;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ConfigurationContext;

import java.util.*;
import java.io.Serializable;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Received_UserMsg")
public class ReceivedUserMsg extends EbMessage implements Serializable
{
  private static final long serialVersionUID = -1765117661137572845L;  

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "ID")
  protected String id = null;

  @Lob
  @Column(name = "Msg_InfoSet", length = 1999999999)
  protected byte[] msgInfo;

  @Column(name = "MessageId")
  protected String messageId = null;

  @Column(name = "RefToMessageId")
  protected String refToMessageId = null;

  @Column(name = "MPC")
  protected String mpc = null;

  @Column(name = "From_Party")
  protected String fromParty = null;

  @Column(name = "To_Party")
  protected String toParty = null;

  @Column(name = "ebms3_Service")
  protected String service = null;

  @Column(name = "ebms3_Action")
  protected String action = null;

  public ReceivedUserMsg()
  {
    setStorageFolder( Constants.getReceivedFolder() );
  }
  public ReceivedUserMsg(ConfigurationContext confgCtx)
  {
    super(confgCtx);
    setStorageFolder( Constants.getReceivedFolder() );
  }
  public ReceivedUserMsg(MessageContext context, MsgInfo mi)
  {
    configContext = context.getConfigurationContext();
    setStorageFolder( Constants.getReceivedFolder() );
    msgInfo = serialize(mi);
    if ( mi != null )
    {
      this.messageId = mi.getMessageId();
      this.refToMessageId = mi.getRefToMessageId();
      this.mpc = mi.getMpc();
      this.service = mi.getService();
      this.action = mi.getAction();
      List<Party> from = mi.getFromParties();
      if ( from != null && from.size() > 0 )
           this.fromParty = from.get(0).getPartyId();
      List<Party> to = mi.getToParties();
      if ( to != null && to.size() > 0 )
           this.toParty = to.get(0).getPartyId();
    }  
    setMessageContext(context, false);
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public byte[] getMsgInfo() { return msgInfo; }
  public void setMsgInfo(byte[] msgInfo) { this.msgInfo = msgInfo; }

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
}