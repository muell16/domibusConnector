package org.holodeck.reliability.persistent;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
//import org.holodeck.common.persistent.SwA;
import org.holodeck.reliability.packaging.Response;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Acks")
public class CallbackAck implements Serializable //extends SwA
{
  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "CallbackAck_ID")
  protected String id = null;

  @Column(name = "GroupId")
  protected String groupId = null;

  @Column(name = "Seq_Number")
  protected String seqNumber = null;

  @Column(name = "AckTo")
  protected String ackTo;

  @Column(name = "Sent")
  protected boolean sent = false;

  public CallbackAck() {}
  public CallbackAck(String groupId, String seq, String ackAddress)
  {
    this.groupId = groupId;
    this.seqNumber = seq;
    this.ackTo = ackAddress;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getGroupId() { return groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public String getSeqNumber() { return seqNumber; }
  public void setSeqNumber(String seqNumber) { this.seqNumber = seqNumber; }

  public String getAckTo() { return ackTo; }
  public void setAckTo(String ackTo) { this.ackTo = ackTo; }

  public boolean isSent() { return sent; }
  public void setSent(boolean sent) { this.sent = sent; }

  public SOAPHeaderBlock getResponseHeaderBlock(SOAPFactory factory)
  {
    Response resp = new Response(factory);
    if ( seqNumber == null || seqNumber.trim().equals("") ||
         seqNumber.trim().equals("-1") )
         resp.addNonSequenceReply(groupId, null);
    else
    {
      int seqN = Integer.parseInt(seqNumber);
      resp.addSequenceReplies(groupId, seqN, seqN, null);
    }
    return resp.getRoot();
  }

  public SOAPHeaderBlock addToHeader(SOAPEnvelope env)
  {
    if ( env == null ) return null;
    Response resp = new Response(env);
    if ( seqNumber == null || seqNumber.trim().equals("") ||
         seqNumber.trim().equals("-1") )
         resp.addNonSequenceReply(groupId, null);
    else
    {
      int seqN = Integer.parseInt(seqNumber);
      resp.addSequenceReplies(groupId, seqN, seqN, null);
    }
    return resp.getRoot();
  }
}