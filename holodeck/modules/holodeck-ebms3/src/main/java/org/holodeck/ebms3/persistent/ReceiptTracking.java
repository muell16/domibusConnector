package org.holodeck.ebms3.persistent;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.*;

/**
 *  This table keeps track of the messages that were sent out and whether a
 *  receipts have been received for them
 *
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Receipt_Tracking")
public class ReceiptTracking implements Serializable
{
  private static final long serialVersionUID = 1061327109897654528L;

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "ID")
  protected String id = null;

  @Column(name = "MessageId", unique=true)
  protected String messageId;

  @Column(name = "toURL")
  protected String toURL;

  @Column(name = "PMode")
  protected String pmode;

  @Column(name = "Receipt_Received")
  protected boolean receiptReceived = false;

  @Lob
  @Column(name = "Receipt", length = 1999999999)
  protected String receiptSignal;

  @ManyToOne(cascade = CascadeType.MERGE)
  protected UserMsgToPush request;

  public ReceiptTracking() {}

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getMessageId() { return messageId; }
  public void setMessageId(String messageId) { this.messageId = messageId; }

  public String getToURL() { return toURL; }
  public void setToURL(String toURL) { this.toURL = toURL; }

  public String getPmode() { return pmode; }
  public void setPmode(String pmode) { this.pmode = pmode; }

  public String getReceiptSignal() { return receiptSignal; }
  public void setReceiptSignal(String receiptSignal)
  {
    this.receiptSignal = receiptSignal;
  }

  public boolean isReceiptReceived() { return receiptReceived; }
  public void setReceiptReceived(boolean receiptReceived)
  {
    this.receiptReceived = receiptReceived;
  }

  public UserMsgToPush getRequest() { return request; }
  public void setRequest(UserMsgToPush request) { this.request = request; }
}