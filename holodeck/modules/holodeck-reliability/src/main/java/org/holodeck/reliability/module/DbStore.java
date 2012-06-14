package org.holodeck.reliability.module;

import org.holodeck.common.store.JpaUtil;
import org.holodeck.reliability.persistent.*;
import org.holodeck.common.persistent.*;

import java.util.*;
import javax.persistence.*;

/**
 * @author Hamid Ben Malek
 */
public class DbStore extends JpaUtil
{
   public DbStore() {}
   public DbStore(String pUnit) { super(pUnit); }

   public SenderGroup getGroupByName(String name)
   {
      if ( name == null ) return null;
      return (SenderGroup)findByUP(SenderGroup.class, "name", name);
   }

   public SenderGroup getGroupByGroupId(String groupId)
   {
      if ( groupId == null ) return null;
      return (SenderGroup)findByUP(SenderGroup.class, "groupId", groupId);
   }

   public ReceiverGroup getReceiverGroupByGroupId(String groupId)
   {
      if ( groupId == null ) return null;
      return (ReceiverGroup)findByUP(ReceiverGroup.class, "groupId", groupId);
   }

  public int markAsAcknowledged(String groupId)
  {
    if ( groupId == null || groupId.trim().equals("") ) return 0;
    String updateQuery =
        "UPDATE GMessage m SET m.acknowledged = TRUE WHERE m.groupId = '" +
        groupId + "'";
    return update(updateQuery);
  }

  public int markAsAcknowledged(String groupId, int from, int to)
  {
    if (groupId == null || groupId.trim().equals("")) return 0;
    String updateQuery =
        "UPDATE GMessage m SET m.acknowledged = TRUE WHERE m.groupId = '" +
        groupId + "' AND m.seqNumber >= " + from + " AND m.seqNumber <= " + to;
    return update(updateQuery);
  }

  public List getAcknowledgments(String groupId, String seq)
  {
    if ( groupId == null ) return null;
    List result = null;
    if ( seq == null || seq.trim().equals("") )
    {
      result = findAll(CallbackAck.class, "groupId", groupId);
    }
    else
    {
      String query = "SELECT c FROM CallbackAck c WHERE c.groupId = '" +
                      groupId  + "' AND c.seqNumber = " + seq;
      result = findAll(query);
    }
    return result;
  }

  public CallbackAck getCallbackAck(String groupId, String seq)
  {
    if ( groupId == null ) return null;
    List result = null;
    String query = "SELECT c FROM CallbackAck c WHERE c.groupId = '" +
               groupId + "' AND c.sent = FALSE";
    if ( seq == null || seq.trim().equals("") || seq.equals("-1") )
         result = findAll(query);
    else
    {
      query = query  + " AND c.seqNumber = " + seq;
      result = findAll(query);
    }
    if ( result == null || result.size() == 0 ) return null;
    else return (CallbackAck)result.get(0);
  }

  public List getCallbackAcks()
  {
    String query = "SELECT c FROM CallbackAck c WHERE c.sent = FALSE";
    return findAll(query);
  }

  public BusinessResponse getBusinessResponse(String groupId, String seq)
  {
    if ( groupId == null ) return null;
    List result = null;
    if ( seq == null || seq.trim().equals("") )
    {
      result = findAll(BusinessResponse.class, "groupId", groupId);
    }
    else
    {
      String query = "SELECT c FROM BusinessResponse c WHERE c.groupId = '" +
                      groupId  + "' AND c.seqNumber = " + seq;
      result = findAll(query);
    }
    if ( result == null || result.size() == 0 ) return null;
    else return (BusinessResponse)result.get(0);
  }

  /**
   * Whether a request with the specificed groupId and sequence number has been
   * previously delivered by the Receiver RMP to the application
   */
  public boolean isDelivered(String groupId, String seq)
  {
    if ( groupId == null ) return false;
    int seqNumber = -1;
    List result = null;
    if ( seq != null && !seq.trim().equals("") )
    {
      seqNumber = Integer.parseInt(seq.trim());
      result = findAll(DeliveredRange.class,
                       new String[]{"groupId"},
                       new String[]{groupId}, "minSeq", "maxSeq", seqNumber);
      }
      else result = findAll(DeliveredRange.class, "groupId", groupId);

      if ( result == null || result.size() == 0 ) return false;
      else return true;
   }

  public List getMessagesToResend()
  {
    EntityManager entityMang = createEntityManager();
    String query =
        "SELECT g FROM GMessage g WHERE g.acknowledged = FALSE AND " +
        "g.resendCount < g.maximumRetransmissionCount AND g.timeToSend < :now ";

    Query q = entityMang.createQuery(query);
    long now = System.currentTimeMillis();
    q.setParameter ("now", now);
    return select(entityMang, q);
  }

  public void save(Object obj)
  {
    if ( obj == null ) return;
    if ( !(obj instanceof MessageRange) )
    {
      super.save(obj);
      return;
    }
    if ( obj instanceof ReceivedRange ) saveReceivedRange((ReceivedRange)obj);
    else if ( obj instanceof DeliveredRange )
              saveDeliveredRange((DeliveredRange)obj);
  }

  public void saveReceivedRange(String groupId, String seq)
  {
    if ( groupId == null ) return;
    ReceivedRange rg = new ReceivedRange();
    rg.setGroupId(groupId);
    if ( seq != null && !seq.trim().equals("") )
    {
      rg.setMinSeq( Integer.parseInt(seq.trim()) );
      rg.setMaxSeq( Integer.parseInt(seq.trim()) );
    }
    saveReceivedRange(rg);
  }

  public void saveDeliveredRange(String groupId, String seq)
  {
    if ( groupId == null ) return;
    DeliveredRange rg = new DeliveredRange();
    rg.setGroupId(groupId);
    if ( seq != null && !seq.trim().equals("") )
    {
      rg.setMinSeq( Integer.parseInt(seq.trim()) );
      rg.setMaxSeq( Integer.parseInt(seq.trim()) );
    }
    saveDeliveredRange(rg);
  }

  /**
   * @return true if the received message with specified groupId and seq is a duplicate
   *  (that is was already received).
   */
  public boolean isDuplicate(String groupId, String seq)
  {
    if ( groupId == null || groupId.trim().equals("") ) return false;
    List receivedList = null;
    if ( seq == null || seq.trim().equals("") )
    {
//    receivedList = findAll(ReceivedRange.class, "groupId", groupId);
      String query =
        "SELECT c FROM ReceivedRange c WHERE c.groupId = '" + groupId + "'";
      receivedList = findAll(query);
    }
    else
    {
      int seqValue = Integer.parseInt(seq);
      receivedList = findAll(ReceivedRange.class,
                             new String[]{"groupId"},
                             new String[]{groupId},
                             "minSeq", "maxSeq", seqValue);
    }
    if ( receivedList != null && receivedList.size() > 0 ) return true;
    else return false;
  }

  public void deleteReceiverGroup(ReceiverGroup g)
  {
    if ( g == null ) return;
    delete(g);
    delete(ReceivedRange.class, "groupId", g.getGroupId());
    delete(DeliveredRange.class, "groupId", g.getGroupId());
    delete(OMessage.class, "groupId", g.getGroupId());
    delete(CallbackAck.class, "groupId", g.getGroupId());
    delete(BusinessResponse.class, "groupId", g.getGroupId());
  }

  public void deleteSenderGroup(String groupId)
  {
    if ( groupId == null || groupId.trim().equals("") ) return;
    delete(SenderGroup.class, "groupId", groupId);
    delete(GMessage.class, "groupId", groupId);
  }

  public int deleteExpiredSenderGroups()
  {
    List groups = findAll(SenderGroup.class);
    if ( groups == null || groups.size() == 0 ) return 0;
    SenderGroup gr = null;
    int res = 0;
    for (Object group : groups)
    {
      gr = (SenderGroup)group;
      if ( gr.isExpired() )
      {
        deleteSenderGroup(gr.getGroupId());
        res++;
      }
    }
    return res;
  }

  public int deleteExpiredReceiverGroups()
  {
    List groups = findAll(ReceiverGroup.class);
    if ( groups == null || groups.size() == 0 ) return 0;
    ReceiverGroup gr = null;
    int result = 0;
    for (Object group : groups)
    {
      gr = (ReceiverGroup)group;
      if ( gr.isExpired() )
      {
        deleteReceiverGroup(gr);
        result++;
      }
    }
    return result;
  }

  /* ---- Private Utility methods to optimize the "ReceivedRange" table ---- */
  private List getLeftReceivedRange(MessageRange rr)
  {
    if ( rr == null ) return null;
    String groupId = rr.getGroupId();
    int minSeq = rr.getMinSeq();
    if ( minSeq == -1 ) return null;
    String query =
      "SELECT c FROM ReceivedRange c WHERE c.groupId = '" +
      groupId + "' AND c.maxSeq + 1 = " + minSeq;
    return findAll(query);
  }

  private List getRightReceivedRange(MessageRange rr)
  {
    if ( rr == null ) return null;
    String groupId = rr.getGroupId();
    int maxSeq = rr.getMaxSeq();
    if ( maxSeq == -1 ) return null;
    String query =
      "SELECT c FROM ReceivedRange c WHERE c.groupId = '" +
      groupId + "' AND c.minSeq - 1 = " + maxSeq;
    return findAll(query);
  }

  public void saveReceivedRange(ReceivedRange rr)
  {
    if ( rr == null  ) return;
    if ( receivedSingletonExists(rr) ) return;

    List left = getLeftReceivedRange(rr);
    List right = getRightReceivedRange(rr);

    if ( left == null || left.size() == 0 )
    {
      if ( right == null || right.size() == 0 ) super.save(rr);
      else
      {
        ReceivedRange r = (ReceivedRange)right.get(0);
        r.setMinSeq(rr.getMinSeq());
        save(r);
      }
    }
    else
    {
      ReceivedRange l = (ReceivedRange)left.get(0);
      if ( right == null || right.size() == 0 )
      {
        l.setMaxSeq(rr.getMaxSeq());
        save(l);
      }
      else
      {
        ReceivedRange r = (ReceivedRange)right.get(0);
        l.setMaxSeq(r.getMaxSeq());
        save(l);
        delete(r);
      }
    }
  }

  /* ---- Private Utility methods to optimize the "ReceivedRange" table ---- */
  private List getLeftDeliveredRange(MessageRange dr)
  {
    if ( dr == null ) return null;
    String groupId = dr.getGroupId();
    int minSeq = dr.getMinSeq();
    if ( minSeq == -1 ) return null;
    String query =
      "SELECT c FROM DeliveredRange c WHERE c.groupId = '" +
      groupId + "' AND c.maxSeq + 1 = " + minSeq;
    return findAll(query);
  }

  private List getRightDeliveredRange(MessageRange dr)
  {
    if ( dr == null ) return null;
    String groupId = dr.getGroupId();
    int maxSeq = dr.getMaxSeq();
    if ( maxSeq == -1 ) return null;

    String query =
      "SELECT c FROM DeliveredRange c WHERE c.groupId = '" +
      groupId + "' AND c.minSeq - 1 = " + maxSeq;
    return findAll(query);
  }

  private void saveDeliveredRange(DeliveredRange dr)
  {
    if ( dr == null  ) return;
    if ( singletonExists(dr) ) return;
    List left = getLeftDeliveredRange(dr);
    List right = getRightDeliveredRange(dr);
    if ( left == null || left.size() == 0 )
    {
      if ( right == null || right.size() == 0 ) super.save(dr);
      else
      {
        DeliveredRange r = (DeliveredRange)right.get(0);
        r.setMinSeq(dr.getMinSeq());
        save(r);
      }
    }
    else
    {
      DeliveredRange l = (DeliveredRange)left.get(0);
      if ( right == null || right.size() == 0 )
      {
        l.setMaxSeq(dr.getMaxSeq());
        save(l);
      }
      else
      {
        DeliveredRange r = (DeliveredRange)right.get(0);
        l.setMaxSeq(r.getMaxSeq());
        save(l);
        delete(r);
      }
    }
  }

  private boolean singletonExists(MessageRange dr)
  {
    if ( dr == null ) return false;
    if ( dr.getMinSeq() == -1 && dr.getMaxSeq() == -1 )
    {
      List result = findAll(DeliveredRange.class, "groupId", dr.getGroupId());
      if ( result != null && result.size() > 0 ) return true;
    }
    return false;
  }

  private boolean receivedSingletonExists(MessageRange dr)
  {
    if ( dr == null ) return false;
    if ( dr.getMinSeq() == -1 && dr.getMaxSeq() == -1 )
    {
      List result = findAll(ReceivedRange.class, "groupId", dr.getGroupId());
      if ( result != null && result.size() > 0 ) return true;
    }
    return false;
  }

  public boolean isSingletonDelivered(String groupId)
  {
    if ( groupId == null || groupId.trim().equals("") ) return false;
    List result = findAll(DeliveredRange.class, "groupId", groupId);
    if ( result != null && result.size() > 0 ) return true;
    return false;
  }

  public MessageRange[] getAcks(MessageRange pollRange)
  {
    String query = "SELECT c from DeliveredRange c WHERE " +
                  "c.groupId = '" + pollRange.getGroupId() + "' AND " +
                  "r.minSeq <= " + pollRange.getMaxSeq() + " AND " +
                  "r.maxSeq >= " + pollRange.getMinSeq();

    List result = findAll(query);
    if ( result == null || result.size() == 0 ) return null;
    List ackRangeList = new ArrayList();
    for (Object aResult : result)
    {
      MessageRange d = (MessageRange) aResult;
      MessageRange r = d.getIntersection(pollRange);
      if (r != null) ackRangeList.add(r);
    }
    MessageRange[] intersection = new MessageRange[ackRangeList.size()];
    for (int j = 0; j < ackRangeList.size(); j++)
         intersection[j] = (MessageRange)ackRangeList.get(j);

    return intersection;
  }

  public GMessage getGMessage(String groupId, int seqNumber)
  {
    GMessage message = null;
    List result = null;
    if (seqNumber <= -1 )
        result = findAll(GMessage.class, "groupId", groupId);
    else
    {
      String query = "SELECT g FROM GMessage g WHERE g.groupId = '" +
                     groupId + " AND g.seqNumber = " + seqNumber;
      result = findAll(query);
    }
    if ( result != null && result.size() > 0 )
         message = (GMessage)result.get(0);
    return message;
  }

  public List getOMessagesToDeliver()
  {
    String query =
         "SELECT m FROM OMessage m WHERE m.delivered = FALSE";

    return findAll(query);
  }

  public List getMessagesToPoll()
  {
    String query = "SELECT m FROM GMessage m WHERE m.acknowledged = FALSE " +
           "AND m.replyPattern LIKE '%Poll%' AND m.expiryTime > :today";
    EntityManager em = createEntityManager();
    Query q = em.createQuery(query);
    q.setParameter("today", new Date());
    return select(em, q);
  }

  public List getExpiredOrFaultedUnacknowledged(String groupId)
  {
    String query = "SELECT m FROM GMessage m WHERE " +
     "m.groupId = '" + groupId +
     "' AND (m.expiryTime < :today OR m.faulted = TRUE) AND m.acknowledged = TRUE";

    EntityManager em = createEntityManager();
    Query q = em.createQuery(query);
    q.setParameter("today", new Date());
    return select(em, q);
  }

  public List getUndeliveredExpiredOrFaulted(String groupId)
  {
    String query = "SELECT m FROM OMessage m WHERE " +
            "m.groupId = '" + groupId + "' AND m.delivered = FALSE AND " +
            "( m.expiryTime < :today OR m.faulted = TRUE ) ";
    EntityManager em = createEntityManager();
    Query q = em.createQuery(query);
    q.setParameter("today", new Date());

    return select(em, q);
  }

  public int deleteAcknowledgedGMessages()
  {
    List gm = findAll(GMessage.class, "acknowledged", "TRUE");
    if ( gm == null || gm.size() == 0 ) return 0;
    for (Object m : gm) delete( (GMessage)m );
    return gm.size();
  }
}