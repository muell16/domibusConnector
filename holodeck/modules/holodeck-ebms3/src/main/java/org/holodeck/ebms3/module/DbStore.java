package org.holodeck.ebms3.module;

import org.holodeck.common.store.JpaUtil;
import org.holodeck.ebms3.persistent.*;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class DbStore extends JpaUtil
{
   public DbStore() {}
   public DbStore(String pUnit) { super(pUnit); }

   public UserMsgToPull getNextUserMsgToPull(String mpc)
   {
     String query = "SELECT c FROM UserMsgToPull c WHERE c.mpc = '" +
            mpc + "' AND c.pulled = FALSE ORDER BY c.timeInMillis ASC";
     List res = this.findAll(query);
     if ( res != null && res.size() > 0 )
          return (UserMsgToPull)res.get(0);
     else return null;
   }

   public MsgIdCallback getMsgIdCallback(String refToMessageId)
   {
     String query = "SELECT c FROM MsgIdCallback c WHERE c.messageId = '" +
            refToMessageId + "'";
     List res = findAll(query);
     if ( res != null && res.size() > 0 )
          return (MsgIdCallback)res.get(0);
     else return null;
   }

   public List<UserMsgToPush> getMessagesToPush()
   {
     String query1 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
        Constants.ONE_WAY_PUSH + "' AND c.pushed = FALSE ORDER BY c.timeInMillis ASC";

     String query2 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
        Constants.TWO_WAY_SYNC + "' AND c.pushed = FALSE AND c.legNumber = 1 ORDER BY c.timeInMillis ASC";

     String query3 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
      Constants.TWO_WAY_PUSH_AND_PUSH + "' AND c.pushed = FALSE AND c.legNumber = 1 ORDER BY c.timeInMillis ASC";

     String query4 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
        Constants.TWO_WAY_PUSH_AND_PULL + "' AND c.pushed = FALSE AND c.legNumber = 1 ORDER BY c.timeInMillis ASC";

     List res1 = findAll(query1);
     List res2 = findAll(query2);
     List res3 = findAll(query3);
     List res4 = findAll(query4);

     List<UserMsgToPush> res = new ArrayList<UserMsgToPush>();

     if ( res1 != null && res1.size() > 0 )
          res.add( (UserMsgToPush)res1.get(0) ) ;
     if ( res2 != null && res2.size() > 0 )
          res.add( (UserMsgToPush)res2.get(0) ) ;
     if ( res3 != null && res3.size() > 0 )
          res.add( (UserMsgToPush)res3.get(0) ) ;
     if ( res4 != null && res4.size() > 0 )
          res.add( (UserMsgToPush)res4.get(0) ) ;

     return res;
   }

   public int setReceiptReceived(String messageId)
   {
     String updateQuery =
        "update ReceiptTracking set receiptReceived = TRUE where messageId = '" +
         messageId + "'";
     return update(updateQuery);
   }

   public int setReceipt(String messageId, String receipt)
   {
     String updateQuery =
        "update ReceiptTracking set receiptSignal = '" + receipt +
         "' where messageId = '" + messageId + "'";
     return update(updateQuery);
   }

   public ReceiptData getReceiptData()
   {
     String query =
       "SELECT c FROM ReceiptData c WHERE c.sent = FALSE ORDER BY c.timestamp ASC";
     List res = findAll(query);
     if ( res == null || res.size() == 0 ) return null;
     else return (ReceiptData)res.get(0);
   }

   public List<ReceivedUserMsg> getReceivedUserMsg()
   {
     List res = findAll(ReceivedUserMsg.class);
     if ( res == null || res.size() == 0 ) return null;
     List<ReceivedUserMsg> rm = new ArrayList<ReceivedUserMsg>();
     for (Object obj : res) rm.add( (ReceivedUserMsg)obj );
     return rm;
   }

   public List<UserMsgToPush> getAllOutgoingMessages()
   {
     String query1 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
        Constants.ONE_WAY_PUSH + "'";

     String query2 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
        Constants.TWO_WAY_SYNC + "' AND c.legNumber = 1";

     String query3 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
      Constants.TWO_WAY_PUSH_AND_PUSH + "' AND c.legNumber = 1";

     String query4 = "SELECT c FROM UserMsgToPush c WHERE c.mep = '" +
        Constants.TWO_WAY_PUSH_AND_PULL + "' AND c.legNumber = 1";

     List res1 = findAll(query1);
     List res2 = findAll(query2);
     List res3 = findAll(query3);
     List res4 = findAll(query4);

     List<UserMsgToPush> res = new ArrayList<UserMsgToPush>();
     for ( Object msg : res1 ) res.add( (UserMsgToPush)msg ) ;
     for ( Object msg : res2 ) res.add( (UserMsgToPush)msg ) ;
     for ( Object msg : res3 ) res.add( (UserMsgToPush)msg ) ;
     for ( Object msg : res4 ) res.add( (UserMsgToPush)msg ) ;

     return res;
   }
}