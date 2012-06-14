package org.holodeck.ebms3.persistent;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Msg_Callback")
public class MsgIdCallback implements Serializable
{
  private static final long serialVersionUID = 1352148385354239611L;  

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "ID")
  protected String id = null;

  @Column(name = "MessageId")
  protected String messageId = null;

  @Column(name = "pmode")
  protected String pmode = null;

  @Column(name = "LegNumber")
  protected int legNumber = 1;

  @Column(name = "Callback_Class")
  protected String callbackClass;

  public MsgIdCallback() {}
  public MsgIdCallback(String messageId, String callbackClass)
  {
    this.messageId = messageId;
    this.callbackClass = callbackClass;
  }

  public MsgIdCallback(String messageId, String pmode, int leg,
                       String callbackClass)
  {
    this.messageId = messageId;
    this.pmode = pmode;
    this.legNumber = leg;
    this.callbackClass = callbackClass;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getMessageId() { return messageId; }
  public void setMessageId(String messageId) { this.messageId = messageId; }

  public String getPmode() { return pmode; }
  public void setPmode(String pmode) { this.pmode = pmode; }

  public int getLegNumber() { return legNumber; }
  public void setLegNumber(int legNumber) { this.legNumber = legNumber; }

  public String getCallbackClass() { return callbackClass; }
  public void setCallbackClass(String callbackClass)
  {
    this.callbackClass = callbackClass;
  }
}