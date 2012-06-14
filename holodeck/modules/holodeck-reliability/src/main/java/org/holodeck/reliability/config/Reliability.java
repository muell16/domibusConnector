package org.holodeck.reliability.config;

import org.simpleframework.xml.*;
import javax.persistence.*;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Reliability")
@Root(name="AckReply", strict=false)
public class Reliability implements java.io.Serializable
{
  @Transient
  private static final long serialVersionUID = -8300986785940392549L;

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "Reliability_ID")
  protected String id = null;

  @Column(name = "Name")
  @Attribute
  protected String name;

  @Column(name = "AtMostOnce")
  @Element(name="AtMostOnce", required=false)
  protected boolean atMostOnce = true;

  @Column(name = "AtLeastOnce")
  @Element(name="AtLeastOnce", required=false)
  protected boolean atLeastOnce = true;

  @Column(name = "InOrder")
  @Element(name="InOrder", required=false)
  protected boolean inOrder = false;

  @Element(name="AckReply", required=false)
  protected AckReply ackReplyElement;

  @Column(name = "Retransmission_Interval")
  @Element(name="RetransmissionInterval", required=false)
  protected int retransmissionInterval = 10000;

  @Column(name = "Exponential_Backoff")
  @Element(name="ExponentialBackoff", required=false)
  protected boolean exponentialBackoff = false;

  @Column(name = "Maximum_Retransmission_Count")
  @Element(name="MaximumRetransmissionCount", required=false)
  protected int maximumRetransmissionCount = 5;

  @Column(name = "Retransmit_Callback")
  @Element(name="RetransmitCallbackClass", required=false)
  protected String retransmitCallbackClass;

  @Column(name = "AckReply")
  protected String ackReply;

  @Column(name = "AckTo")
  protected String ackTo;


  public Reliability() {}

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public boolean isAtMostOnce() { return atMostOnce; }
  public void setAtMostOnce(boolean atMostOnce)
  {
    this.atMostOnce = atMostOnce;
  }

  public boolean isAtLeastOnce() { return atLeastOnce; }
  public void setAtLeastOnce(boolean atLeastOnce)
  {
    this.atLeastOnce = atLeastOnce;
  }

  public boolean isInOrder() { return inOrder; }
  public void setInOrder(boolean inOrder) { this.inOrder = inOrder; }

  public AckReply getAckReplyElement()
  {
    if ( ackReplyElement != null ) return ackReplyElement;
    ackReplyElement = new AckReply();
    ackReplyElement.setAckTo(ackTo);
    ackReplyElement.setValue(ackReply);
    return ackReplyElement;
  }
  public void setAckReplyElement(AckReply ack)
  {
    this.ackReplyElement = ack;
    this.ackReply = ackReplyElement.getValue();
    this.ackTo = ackReplyElement.getAckTo();
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getAckReply()
  {
    if ( ackReplyElement != null )
    {
      ackReply = ackReplyElement.getValue();
      return ackReply;
    }
    return ackReply;
  }
  public void setAckReply(String ackReply) { this.ackReply = ackReply; }

  public String getAckTo()
  {
    if ( ackReplyElement != null ) ackTo = ackReplyElement.getAckTo();
    return ackTo;
  }
  public void setAckTo(String ackTo) { this.ackTo = ackTo; }

  public int getRetransmissionInterval() { return retransmissionInterval; }
  public void setRetransmissionInterval(int retransmissionInterval)
  {
    this.retransmissionInterval = retransmissionInterval;
  }

  public boolean isExponentialBackoff() { return exponentialBackoff; }
  public void setExponentialBackoff(boolean exponentialBackoff)
  {
    this.exponentialBackoff = exponentialBackoff;
  }

  public int getMaximumRetransmissionCount()
  {
    return maximumRetransmissionCount;
  }
  public void setMaximumRetransmissionCount(int maximumRetransmissionCount)
  {
    this.maximumRetransmissionCount = maximumRetransmissionCount;
  }

  public String getRetransmitCallbackClass() { return retransmitCallbackClass; }
  public void setRetransmitCallbackClass(String retransmitCallbackClass)
  {
    this.retransmitCallbackClass = retransmitCallbackClass;
  }
}