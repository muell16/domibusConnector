package org.holodeck.reliability.persistent;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
//import org.holodeck.common.persistent.SwA;
import org.holodeck.common.client.Client;
import org.apache.axis2.context.MessageContext;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.om.OMElement;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.module.Constants;

import java.util.Date;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Ordered_Msg")
public class OMessage extends Client
{
  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "OMessage_ID")
  protected String id = null;

  @Column(name = "GroupId")
  protected String groupId = null;

  @Column(name = "Seq_Number")
  protected int seqNumber;

  @Column(name = "Delivered")
  protected boolean delivered = false;

  @Column(name = "Service_URL")
  protected String serviceURL;

  @Column(name = "Expirytime")
  protected Date expiryTime;

  // indicates whether the message has an error
  @Column(name = "Faulted")
  protected boolean faulted = false;

  public OMessage()
  {
    setStorageFolder( Constants.getStorageFolder() );
  }
  public OMessage(MessageContext msgCtx)
  {
    setStorageFolder( Constants.getStorageFolder() );
    setMessageContext(msgCtx);
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return;
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    if ( seq != null && !seq.trim().equals("") ) seqNumber = Integer.parseInt(seq);
    String expTime = Util.getGrandChildValue(wsrmReq, Constants.EXPIRY_TIME);
    if ( expTime != null && !expTime.trim().equals("") )
         expiryTime = Util.utcToDate(expTime);
    serviceURL = msgCtx.getAxisService().getEndpointURL();
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getGroupId() { return groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public int getSeqNumber() { return seqNumber; }
  public void setSeqNumber(int seqNumber) { this.seqNumber = seqNumber; }

  public boolean isDelivered() { return delivered; }
  public void setDelivered(boolean delivered) { this.delivered = delivered; }

  public String getServiceURL() { return serviceURL; }
  public void setServiceURL(String serviceURL) { this.serviceURL = serviceURL; }

  public Date getExpiryTime() { return expiryTime; }
  public void setExpiryTime(Date expiryTime) { this.expiryTime = expiryTime; }

  public boolean isFaulted() { return faulted; }
  public void setFaulted(boolean faulted) { this.faulted = faulted; }

  public boolean isExpired()
  {
    if ( expiryTime == null ) return false;
    if ( expiryTime.before(new Date()) ) return true;
    else return false;
  }
}