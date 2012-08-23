package org.holodeck.reliability.persistent;

//import org.holodeck.common.persistent.SwA;
import org.holodeck.common.client.Client;
import org.holodeck.reliability.module.Constants;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.apache.axis2.context.MessageContext;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Business_Responses")
public class BusinessResponse extends Client
{
  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "BR_ID")
  protected String id = null;

  @Column(name = "GroupId")
  protected String groupId = null;

  @Column(name = "Sequence_Number")
  protected String seqNumber = null;

  public BusinessResponse() {}
  public BusinessResponse(String groupId, String seq, MessageContext msgCtx)
  {
    setStorageFolder( Constants.getStorageFolder() );
    setMessageContext(msgCtx);
    this.groupId = groupId;
    this.seqNumber = seq;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getGroupId() { return groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public String getSeqNumber() { return seqNumber; }
  public void setSeqNumber(String seqNumber) { this.seqNumber = seqNumber; }
}