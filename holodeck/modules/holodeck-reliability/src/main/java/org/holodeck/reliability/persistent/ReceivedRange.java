package org.holodeck.reliability.persistent;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Received_Ranges")
public class ReceivedRange extends MessageRange
{
  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "ReceivedRange_ID")
  protected String id = null;

//  @Column(name = "Min_Seq")
//  protected int minSeq = -1;
//
//  @Column(name = "Max_Seq")
//  protected int maxSeq = -1;
//
//  @Column(name = "GroupId")
//  protected String groupId;

  public ReceivedRange() {}
  public ReceivedRange(String groupId, int min, int max)
  {
    this.groupId = groupId;
    this.minSeq = min;
    this.maxSeq = max;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

//  public int getMinSeq() { return minSeq; }
//  public void setMinSeq(int minSeq) { this.minSeq = minSeq; }
//
//  public int getMaxSeq() { return maxSeq; }
//  public void setMaxSeq(int maxSeq) { this.maxSeq = maxSeq; }
//
//  public String getGroupId() { return groupId; }
//  public void setGroupId(String groupId) { this.groupId = groupId; }
//
//  public MessageRange getIntersection(MessageRange poll)
//  {
//    if ( poll == null ) return null;
//    MessageRange intersection = null;
//    int a = poll.getMinSeq();
//    int b = poll.getMaxSeq();
//
//    int c = getMinSeq();
//    int d = getMaxSeq();
//    if ( a <= c && c <= b && b < d )
//    {
//      intersection = new MessageRange(poll.getGroupId(), c, b);
//    }
//    else if ( a <= c && c <= b && a <= d && d <= b )
//    {
//      intersection = new MessageRange(poll.getGroupId(), c, d);
//    }
//    else if ( c < a && a <= d && d <= b )
//    {
//      intersection = new MessageRange(poll.getGroupId(), a, d);
//    }
//    else if ( c < a && b < d )
//    {
//      intersection = new MessageRange(poll.getGroupId(), a, b);
//    }
//    return intersection;
//  }
}