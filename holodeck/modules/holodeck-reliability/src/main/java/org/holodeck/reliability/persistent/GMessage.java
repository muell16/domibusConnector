package org.holodeck.reliability.persistent;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

//import org.holodeck.common.persistent.SwA;
import org.holodeck.common.client.Client;
import org.holodeck.common.soap.Util;
import org.holodeck.common.persistent.Attachment;

import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.config.Reliability;

import org.apache.axis2.context.MessageContext;
import org.apache.axiom.soap.*;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.async.Callback;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Retransmit_Msg")
public class GMessage extends Client
{
  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "GMessage_ID")
  protected String id = null;

  @Column(name = "GroupId")
  protected String groupId;

  @Column(name = "Seq_Number")
  protected int seqNumber = -1;

  @Column(name = "Acknowledged")
  protected boolean acknowledged = false;

  @Column(name = "Retrans_Interval")
  protected int retransmissionInterval;

  @Column(name = "Exp_Backoff")
  protected boolean exponentialBackoff = false;

  @Column(name = "Max_Retrans_Count")
  protected int maximumRetransmissionCount;

  @Column(name = "Resend_Count")
  protected int resendCount = 0;

  @Column(name = "Time_To_Send")
  protected long timeToSend;

  @Column(name = "Delivery_Failed")
  protected boolean deliveryFailed = false;

  @Column(name = "Send_To")
  protected String remoteServiceURL;

  @Column(name = "Expirytime")
  protected Date expiryTime;

  @Column(name = "Faulted")
  protected boolean faulted = false;

  @Column(name = "Reply_Pattern")
  protected String replyPattern;

  @Column(name = "Retransmit_Callback")
  protected String retransmitCallbackClass;

  public GMessage()
  {
    setStorageFolder( Constants.getStorageFolder() );
  }
  public GMessage(MessageContext context, Reliability quality)
  {
    setStorageFolder( Constants.getStorageFolder() );
    List<Attachment> atts =
            (List<Attachment>)context.getProperty("attachments");
    if ( atts != null ) attachments = atts;
      
    setMessageContext(context);
    SOAPHeader header = context.getEnvelope().getHeader();
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    if (seq != null && !seq.trim().equals(""))
        seqNumber = Integer.parseInt(seq.trim());
    String expiry =
      Util.getGrandChildValue(wsrmReq, Constants.EXPIRY_TIME);
    expiryTime = Util.utcToDate(expiry);
    replyPattern = Util.getGrandChildValue(wsrmReq, Constants.VALUE);
    // fill the property remoteServiceURL by the endpoint address:
    remoteServiceURL = //context.getAxisService().getEndpointURL();
                       context.getTo().getAddress();

    this.retransmissionInterval = quality.getRetransmissionInterval();
    this.maximumRetransmissionCount = quality.getMaximumRetransmissionCount();
    this.exponentialBackoff = quality.isExponentialBackoff();
    this.timeToSend = Calendar.getInstance().getTimeInMillis() +
                      retransmissionInterval;

    if ( quality.getRetransmitCallbackClass() != null &&
         !quality.getRetransmitCallbackClass().equals("") )
         retransmitCallbackClass = quality.getRetransmitCallbackClass();

    if ( context.getProperty(Constants.RETRANSMIT_CALLBACK_CLASS) != null )
         retransmitCallbackClass =
              (String)context.getProperty(Constants.RETRANSMIT_CALLBACK_CLASS);
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getGroupId() { return groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public int getSeqNumber() { return seqNumber; }
  public void setSeqNumber(int seqNumber) { this.seqNumber = seqNumber; }

  public boolean isAcknowledged() { return acknowledged; }
  public void setAcknowledged(boolean acknowledged)
  {
    this.acknowledged = acknowledged;
  }

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

  public int getResendCount() { return resendCount; }
  public void setResendCount(int resendCount) { this.resendCount = resendCount; }

  public long getTimeToSend() { return timeToSend; }
  public void setTimeToSend(long timeToSend) { this.timeToSend = timeToSend; }

  public boolean isDeliveryFailed() { return deliveryFailed; }
  public void setDeliveryFailed(boolean deliveryFailed)
  {
    this.deliveryFailed = deliveryFailed;
  }

  public String getRemoteServiceURL() { return remoteServiceURL; }
  public void setRemoteServiceURL(String remoteServiceURL)
  {
    this.remoteServiceURL = remoteServiceURL;
  }

  public Date getExpiryTime() { return expiryTime; }
  public void setExpiryTime(Date expiryTime) { this.expiryTime = expiryTime; }

  public boolean isFaulted() { return faulted; }
  public void setFaulted(boolean faulted) { this.faulted = faulted; }

  public String getReplyPattern() { return replyPattern; }
  public void setReplyPattern(String replyPattern)
  {
    this.replyPattern = replyPattern;
  }

  public String getRetransmitCallbackClass() { return retransmitCallbackClass; }
  public void setRetransmitCallbackClass(String retransmitCallbackClass)
  {
    this.retransmitCallbackClass = retransmitCallbackClass;
  }

  public boolean isExpired()
  {
    if ( expiryTime == null ) return false;
    if ( expiryTime.before(new Date()) ) return true;
    else return false;
  }

  public Callback getCallback()
  {
    if ( retransmitCallbackClass == null ||
         retransmitCallbackClass.trim().equals("") ) return null;
    Callback callback = null;
    try
    {
      Class cb = Class.forName(retransmitCallbackClass);
      callback = (Callback)cb.newInstance();
    }
    catch(Exception ex) { ex.printStackTrace(); return null; }
    return callback;
  }
}