package org.holodeck.reliability.persistent;

import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import javax.xml.datatype.Duration;
import javax.xml.datatype.DatatypeFactory;

import org.apache.axiom.om.*;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.module.Constants;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Receiver_Groups")
public class ReceiverGroup implements Serializable
{
  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "Group_ID")
  protected String id = null;

  @Column(name = "GroupId")
  protected String groupId;

  @Column(name = "Closed")
  protected boolean closed = false;

  @Column(name = "Removed")
  protected boolean removed = false;

  @Column(name = "Last_Msg_timestamp")
  protected Date lastMessageTimestamp = new Date();

  @Column(name = "Max_Msg_ExpiryTime")
  protected Date maxMessageExpiryTime;

  @Column(name = "Highest_Seq_Received")
  protected int highestSeqReceived = -1;

  @Column(name = "Delivered_Count")
  protected int deliveredCount = 0;

  @Column(name = "Max_Idle_Duration")
  protected String groupMaxIdleDuration;

  @Column(name = "Group_Expritytime_UTC")
  protected String groupExpiryTimeUTC;

  @Column(name = "Ordered")
  protected boolean ordered = false;

  @Column(name = "Last_Delivered_Seq")
  protected int lastDeliveredSeq = -1;

  @Column(name = "Complete")
  protected boolean complete = false;

  public ReceiverGroup() {}
  public ReceiverGroup(OMElement wsrmReq, Date dateReceived)
  {
    if (wsrmReq == null) return;
    groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    if (seq != null && !seq.trim().equals(""))
    setHighestSeqReceived(Integer.parseInt(seq));
    String seqStatus =
     Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "last");
    if (seqStatus != null && seqStatus.equalsIgnoreCase("true"))
        setComplete(true);
    String groupExpiryTime =
     Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "groupExpiryTime");
    if (groupExpiryTime != null) setGroupExpiryTimeUTC(groupExpiryTime);
    String maxIdleDuration =
     Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "groupMaxIdleDuration");
    if (maxIdleDuration != null) setGroupMaxIdleDuration(maxIdleDuration);
    String mesgExpiryTime =
        Util.getGrandChildValue(wsrmReq, Constants.EXPIRY_TIME);
    if (mesgExpiryTime != null)
    {
      Date exp = Util.utcToDate(mesgExpiryTime);
      setMaxMessageExpiryTime(exp);
    }
    setLastMessageTimestamp(dateReceived);
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getGroupId() { return groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public boolean isClosed() { return closed; }
  public void setClosed(boolean closed) { this.closed = closed; }

  public Date getLastMessageTimestamp() { return lastMessageTimestamp; }
  public void setLastMessageTimestamp(Date lastMesgTimestamp)
  {
    if (lastMesgTimestamp == null) return;
    if ( isMaxExpiryTimeReached() ) setClosed(true);
    this.lastMessageTimestamp = lastMesgTimestamp;
  }

  public Date getMaxMessageExpiryTime() { return maxMessageExpiryTime; }
  public void setMaxMessageExpiryTime(Date maxMesgExpiryTime)
  {
    if (maxMesgExpiryTime == null) return;
    if ( maxMessageExpiryTime != null &&
         maxMesgExpiryTime.before(maxMessageExpiryTime) ) return;
    this.maxMessageExpiryTime = maxMesgExpiryTime;
  }

  public String getGroupMaxIdleDuration() { return groupMaxIdleDuration; }
  public void setGroupMaxIdleDuration(String groupMaxIdleDuration)
  {
    this.groupMaxIdleDuration = groupMaxIdleDuration;
  }

  public String getGroupExpiryTimeUTC() { return groupExpiryTimeUTC; }
  public void setGroupExpiryTimeUTC(String groupExpiryTimeUTC)
  {
    this.groupExpiryTimeUTC = groupExpiryTimeUTC;
  }

  public boolean isRemoved() { return removed; }
  public void setRemoved(boolean removed) { this.removed = removed; }

  public int getHighestSeqReceived() { return highestSeqReceived; }
  public void setHighestSeqReceived(int seqReceived)
  {
    if ( seqReceived <= highestSeqReceived ) return;
    this.highestSeqReceived = seqReceived;
  }

  public int getDeliveredCount() { return deliveredCount; }
  public void setDeliveredCount(int deliveredCount)
  {
    this.deliveredCount = deliveredCount;
  }

  public boolean isOrdered() { return ordered; }
  public void setOrdered(boolean ordered) { this.ordered = ordered; }

  public int getLastDeliveredSeq() { return lastDeliveredSeq; }
  public void setLastDeliveredSeq(int lastDeliveredSeq)
  {
    this.lastDeliveredSeq = lastDeliveredSeq;
  }

  public boolean isComplete() { return complete; }
  public void setComplete(boolean complete) { this.complete = complete; }

  public boolean isExpired()
  {
    if ( getGroupExpiryTimeUTC() == null ) return false;
    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      Date date = formatter.parse(getGroupExpiryTimeUTC());
      if ( date == null ) return false;
      Calendar cal = GregorianCalendar.getInstance();
      long now = cal.getTimeInMillis();
      long exp = date.getTime();
      if ( exp <= now ) return true;
      else return false;
    }
    catch(Exception e) { e.printStackTrace(); return false; }
  }

  public boolean maxIdleDurationReached()
  {
    if ( groupMaxIdleDuration == null ||
         groupMaxIdleDuration.trim().equals("") ) return false;
    if ( getLastMessageTimestamp() == null ) return false;
    Calendar cal = GregorianCalendar.getInstance();
    long now = cal.getTimeInMillis();
    long origin = getLastMessageTimestamp().getTime();
    DatatypeFactory df = null;
    try { df = DatatypeFactory.newInstance(); }
    catch(Exception ex) { ex.printStackTrace(); return false; }
    Duration duration = df.newDuration(groupMaxIdleDuration);
    Duration idle = df.newDuration(now - origin);
    if ( duration.isShorterThan(idle) ) return true;
    else return false;
  }

  public boolean isMaxExpiryTimeReached()
  {
    Calendar cal = GregorianCalendar.getInstance();
    long now = cal.getTimeInMillis();
    if ( getMaxMessageExpiryTime() == null ) return false;
    long exp = getMaxMessageExpiryTime().getTime();
    if ( exp <= now ) return true;
    else return false;
  }
}