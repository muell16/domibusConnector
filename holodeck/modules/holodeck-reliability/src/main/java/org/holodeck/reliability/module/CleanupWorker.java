package org.holodeck.reliability.module;

//import java.util.concurrent.*;
//import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.*;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.holodeck.reliability.persistent.*;
//import org.holodeck.common.soap.Util;

/**
 * This worker cleans the database by deleting the following:
 * <li> Expired SenderGroups</li>
 * <li> Expired GMessages </li>
 * <li> Closed or Expired ReceiverGroups </li>
 * <li> MessageRange objects corresponding to closed or expired receiver groups</li>
 *
 * @author Hamid Ben Malek
 */
public class CleanupWorker extends PeriodicWorker //implements Runnable
{
  //protected ScheduledThreadPoolExecutor executor;
  protected ConfigurationContext configCtx;
  protected DbStore store;

  private static final Log log =
             LogFactory.getLog(CleanupWorker.class.getName());

  public CleanupWorker(ConfigurationContext confCtx) { init(confCtx); }

  //public void run()
  protected void task()
  {
    int erg = store.deleteExpiredReceiverGroups();
    if ( erg > 0 ) log.debug(erg + " expired ReceiverGroups were deleted");
    int esg = store.deleteExpiredSenderGroups();
    if (esg > 0) log.debug(esg + " Expired SenderGroups were deleted");
    int res = //store.delete(GMessage.class, "acknowledged", "TRUE");
              store.deleteAcknowledgedGMessages();
    if (res > 0) log.debug(res + " Acknowledged GMessages were deleted");
      
    List sGroups = store.findAll(SenderGroup.class);
    if ( sGroups == null || sGroups.size() == 0 ) return;
    SenderGroup g = null;
    for (Object sGroup : sGroups)
    {
      g = (SenderGroup) sGroup;
      if (shouldMarkAsClosed(g)) g.setClosed(true);
      if (remove2(g) || remove3(g) || remove4(g) || remove5(g) || remove6(g))
      {
        store.delete(g);
        log.debug("SenderGroup whose groupId=" +
                  g.getGroupId() + " was deleted");
      }
    }

    ReceiverGroup rg = null;
    List rGroups = store.findAll(ReceiverGroup.class);
    if ( rGroups == null || rGroups.size() == 0 ) return;
    for (Object rGroup : rGroups)
    {
      rg = (ReceiverGroup) rGroup;
      if (shouldMarkAsClosed(rg)) rg.setClosed(true);
      if (remove2(rg) || remove3(rg) || remove4(rg))
      {
        store.deleteReceiverGroup(rg);
        log.debug("ReceiverGroup whose groupId=" +
                  rg.getGroupId() + " was deleted");
      }
    }
  }

  private void init(ConfigurationContext confCtx)
  {
    if (confCtx == null) return;
    configCtx = confCtx;
    //executor =
    //      (ScheduledThreadPoolExecutor)configCtx.getAxisConfiguration()
    //                              .getParameter(Constants.SCHEDULED_EXECUTOR)
    //                              .getValue();
    store = (DbStore)configCtx.getAxisConfiguration()
                                  .getParameter(Constants.STORE)
                                  .getValue();
    //executor.scheduleWithFixedDelay(this, 20000, 15000, MILLISECONDS);
  }

  // GroupMaxIdleDuration reached and Guaranteed Delivery not required
  private boolean remove2(SenderGroup group)
  {
    return ( group.maxIdleDurationReached() &&
            !group.getQuality().isAtLeastOnce() );
  }

  // GroupMaxIdleDuration reached and Guaranteed Delivery required and
  // all messages have been acknowledged or faulted
  private boolean remove3(SenderGroup group)
  {
    int total = group.getAcknowledgedCount() + group.getFailedCount();
    int capacity = group.getCapacity();
    return ( group.maxIdleDurationReached() &&
             group.getQuality().isAtLeastOnce() &&
             group.getQuality().getMaximumRetransmissionCount() > 0 &&
             total == capacity );
  }

  // Group is complete and guaranteed delivery not required
  public boolean remove4(SenderGroup group)
  {
    int capacity = group.getCapacity();
    int currentSeq = group.getCurrentSeqNumber();
    return ( capacity > 0 && currentSeq == capacity &&
             !group.getQuality().isAtLeastOnce() );
  }

  // Group is complete and guaranteed delivery required and
  // all messages have been acknowledged or faulted
  public boolean remove5(SenderGroup group)
  {
    int capacity = group.getCapacity();
    int currentSeq = group.getCurrentSeqNumber();
    int total = group.getAcknowledgedCount() + group.getFailedCount();
    return ( capacity > 0 && currentSeq == capacity &&
             group.getQuality().isAtLeastOnce() && total == capacity );
  }

  // Group is ordered and all messages have been acknowledged or faulted
  private boolean remove6(SenderGroup group)
  {
    int total = group.getAcknowledgedCount() + group.getFailedCount();
    int capacity = group.getCapacity();
    return ( group.getQuality().isInOrder() && total == capacity );
  }

  // Whether or not to close the group
  private boolean shouldMarkAsClosed(SenderGroup group)
  {
    if ( group.isExpired() ) return true;
    if ( group.maxIdleDurationReached() ) return true;
    if ( group.getCurrentSeqNumber() == group.getCapacity() &&
         group.getCapacity() > 0 ) return true;
    List list = store.getExpiredOrFaultedUnacknowledged(group.getGroupId());
    if ( group.getQuality().isInOrder() && list != null && list.size() > 0 )
         return true;
    return false;
  }

  // ======================== method for ReceiverGroup =======================
  // GroupMaxIdleDuration reached and MaxExpiryTime reached
  private boolean remove2(ReceiverGroup group)
  {
    return ( group.maxIdleDurationReached() &&
             group.isMaxExpiryTimeReached() );
  }

  // Group is complete and MaxExpiryTime reached
  public boolean remove3(ReceiverGroup group)
  {
    return ( group.isComplete() && group.isMaxExpiryTimeReached() );
  }

  // Group is ordered and all MaxExpiryTime has passed
  private boolean remove4(ReceiverGroup group)
  {
    return ( group.isOrdered() && group.isMaxExpiryTimeReached() );
  }

  // Whether or not to close the group
  private boolean shouldMarkAsClosed(ReceiverGroup group)
  {
    if ( group.isExpired() ) return true;
    if ( group.maxIdleDurationReached() ) return true;
    if ( group.isComplete() ) return true;
    List list = store.getUndeliveredExpiredOrFaulted(group.getGroupId());
    if ( group.isOrdered() && list != null && list.size() > 0 ) return true;

    return false;
  }
}