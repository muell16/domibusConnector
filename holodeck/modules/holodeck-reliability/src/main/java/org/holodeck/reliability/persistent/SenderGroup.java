package org.holodeck.reliability.persistent;

import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.xml.datatype.Duration;
import javax.xml.datatype.DatatypeFactory;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import org.holodeck.reliability.config.Reliability;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Sender_Groups")
public class SenderGroup implements Serializable
{
  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "Group_ID")
  protected String id = null;

  @Column(name = "Name")
  protected String name;

  @Column(name = "GroupId")
  protected String groupId;

  @Column(name = "Group_Lifetime")
  protected String groupLifeTime = "P29D";

  @Column(name = "Mesg_Lifetime")
  protected String messageLifeTime = "P29D";

  @Column(name = "Max_Idle_Duration")
  protected String groupMaxIdleDuration = null;

  @Column(name = "Current_Seq")
  protected int currentSeqNumber = -1;

  @Column(name = "Closed")
  protected boolean closed = false;

  @Column(name = "Last_Msg_timestamp")
  protected Date lastMessageTimestamp;

  @Column(name = "Max_Msg_ExpiryTime")
  protected Date maxMessageExpiryTime;

  @Column(name = "Acknowledged_Count")
  protected int acknowledgedCount = 0;

  @Column(name = "Failed_Count")
  protected int failedCount = 0;

  @Column(name = "Date_Started")
  protected Date dateStarted;

  @Column(name = "Capacity")
  protected int capacity = -1;

  @ManyToOne(cascade = CascadeType.ALL)
  protected Reliability quality = null;

  public SenderGroup()
  {
    groupId = newGroupId(this);
    dateStarted = new Date();
  }

  public SenderGroup(String groupName)
  {
    this();
    if (groupName != null) name = groupName;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getGroupId() { return groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public String getGroupLifeTime() { return groupLifeTime; }
  public void setGroupLifeTime(String groupLifeTime)
  {
    this.groupLifeTime = groupLifeTime;
    if ( groupLifeTime != null && !groupLifeTime.trim().equals("") )
         groupMaxIdleDuration = null;
  }

  public String getMessageLifeTime() { return messageLifeTime; }
  public void setMessageLifeTime(String messageLifeTime)
  {
    this.messageLifeTime = messageLifeTime;
  }

  public String getGroupExpiryTime()
  {
    Date result = getDate(dateStarted, groupLifeTime);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return formatter.format(result);
  }

  public String getGroupMaxIdleDuration() { return groupMaxIdleDuration; }
  public void setGroupMaxIdleDuration(String groupMaxIdleDuration)
  {
    this.groupMaxIdleDuration = groupMaxIdleDuration;
    if ( groupMaxIdleDuration != null &&
         !groupMaxIdleDuration.trim().equals("") )
         groupLifeTime = null;
  }

  public int getCurrentSeqNumber() { return currentSeqNumber; }
  public void setCurrentSeqNumber(int currentSeqNumber)
  {
    this.currentSeqNumber = currentSeqNumber;
  }

  public boolean isClosed() { return closed; }
  public void setClosed(boolean closed) { this.closed = closed; }

  public Date getLastMessageTimestamp() { return lastMessageTimestamp; }
  public void setLastMessageTimestamp(Date lastMessageTimestamp)
  {
    this.lastMessageTimestamp = lastMessageTimestamp;
  }

  public Date getMaxMessageExpiryTime() { return maxMessageExpiryTime; }
  public void setMaxMessageExpiryTime(Date maxMsgExpiryTime)
  {
    if (maxMsgExpiryTime == null) return;
    if ( maxMsgExpiryTime.before(maxMessageExpiryTime) ) return;
    this.maxMessageExpiryTime = maxMsgExpiryTime;
  }

  public int getAcknowledgedCount() { return acknowledgedCount; }
  public void setAcknowledgedCount(int acknowledgedCount)
  {
    this.acknowledgedCount = acknowledgedCount;
  }

   public int getFailedCount() { return failedCount; }
   public void setFailedCount(int failedCount) { this.failedCount = failedCount; }

   public Date getDateStarted() { return dateStarted; }
   public void setDateStarted(Date dateStarted) { this.dateStarted = dateStarted; }

   public int getCapacity() { return capacity; }
   public void setCapacity(int capacity) { this.capacity = capacity; }

   public Reliability getQuality() { return quality; }
   public void setQuality(Reliability quality) { this.quality = quality; }

  // when this method is called, it is given the "now" date which represents
  // the moment a given message instance is created and is about to be sent out.
  public String getMessageExpiryTime(Date since)
  {
    Date result = getDate(since, messageLifeTime);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return formatter.format(result);
  }

  public boolean maxIdleDurationReached(Date since)
  {
    if ( groupMaxIdleDuration == null || groupMaxIdleDuration.trim().equals("") ) return false;
    if ( since == null ) return false;
    Calendar cal = GregorianCalendar.getInstance();
    long now = cal.getTimeInMillis();
    long origin = since.getTime();
    DatatypeFactory factory = null;
    try { factory = DatatypeFactory.newInstance(); }
    catch(Exception ex) { ex.printStackTrace(); return false; }
    Duration duration = factory.newDuration(groupMaxIdleDuration);
    Duration elapsed = factory.newDuration(now - origin);
    if ( duration.isShorterThan(elapsed) ) return true;
    else return false;
  }

  public boolean maxIdleDurationReached()
  {
    return maxIdleDurationReached(lastMessageTimestamp);
  }

  public boolean isMaxMessageExpiryTimeReached()
  {
    Calendar cal = GregorianCalendar.getInstance();
    long now = cal.getTimeInMillis();
    long exp = getMaxMessageExpiryTime().getTime();
    if ( exp <= now ) return true;
    else return false;
  }

  public boolean isExpired()
  {
    Date date = getGroupExpiryDate();
    if ( date == null ) return false;
    Calendar cal = GregorianCalendar.getInstance();
    long now = cal.getTimeInMillis();
    long exp = date.getTime();
    if ( exp <= now ) return true;
    else return false;
  }

  public Date getGroupExpiryDate()
  {
    return getDate(dateStarted, groupLifeTime);
  }

  public int nextSequenceNumber()
  {
    currentSeqNumber = currentSeqNumber + 1;
    return currentSeqNumber;
  }

  public static String newGroupId(Object obj)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("mid://");

    int hc = 349857989;
    if ( obj != null ) hc = obj.hashCode();
    java.security.SecureRandom  sr = new java.security.SecureRandom();
    long l = sr.nextLong();
    Random random = new Random();
    long uniqueId =
         ((System.currentTimeMillis()>>>16)<<hc) + random.nextLong() + hc + l;
    if ( uniqueId < 0 ) uniqueId = 0 - uniqueId;
    String computerName = System.getProperty("computername");
    if ( computerName == null )
    {
      try
      {
        computerName = java.net.InetAddress.getLocalHost().getHostName();
      }
      catch(Exception ex) { computerName = "oasis-open.org"; }
    }
    sb.append(uniqueId);
    sb.append("@");
    sb.append(computerName);

    return sb.toString();
  }

  public static Date getDate(Date since, String lifeTime)
  {
    Date expiryTime = null;
    if (lifeTime == null || lifeTime.trim().equals("")) return expiryTime;
    DatatypeFactory factory = null;
    try { factory = DatatypeFactory.newInstance(); }
    catch(Exception ex) { ex.printStackTrace(); return expiryTime; }

    Duration duration = factory.newDuration(lifeTime);
    long expiration = since.getTime() + duration.getTimeInMillis(since);

    Calendar cal = GregorianCalendar.getInstance();
    cal.setTimeInMillis(expiration);
    expiryTime = cal.getTime();

    return expiryTime;
  }

  public static String getUTC(Date start, String duration)
  {
    Date end = getDate(start, duration);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return formatter.format(end);
  }
}