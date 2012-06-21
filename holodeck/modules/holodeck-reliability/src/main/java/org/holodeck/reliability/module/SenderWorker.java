package org.holodeck.reliability.module;

//import java.util.concurrent.*;
//import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.client.async.Callback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.OutInAxisOperation;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.persistent.GMessage;

/**
 * @author Hamid Ben Malek
 */
public class SenderWorker extends PeriodicWorker //implements Runnable
{
  //protected ScheduledThreadPoolExecutor executor;
  protected ConfigurationContext configCtx;

  protected DbStore store;
  //protected String groupId;
  //protected int seqNumber = -1;

//  private static final Log log = LogFactory.getLog(SenderWorker.class.getName());
  private static final Logger log = Logger.getLogger(SenderWorker.class.getName());

  public SenderWorker(MessageContext msgCtx) { init(msgCtx); }

  public SenderWorker(//ScheduledThreadPoolExecutor exec,
                      ConfigurationContext ctx//, String groupId, int seqNumber
                      )
  {
    //this.executor = exec;
    this.configCtx = ctx;
    //this.groupId = groupId;
    //this.seqNumber = seqNumber;
    if ( configCtx != null )
        store = (DbStore)configCtx.getAxisConfiguration()
                                  .getParameter(Constants.STORE)
                                  .getValue();
  }

  //public void run()
  protected void task()
  {
    // Fetch the message from DB first...
    List<GMessage> messages = getMessagesToResend();
    if ( messages == null || messages.size() <= 0 ) return;
    //GMessage message = store.getGMessage(groupId, seqNumber);
    //if ( message == null ) return;
    for (GMessage message : messages)
    {
      if ( shouldBeDeleted(message) )
      {
        //executor.remove(this);
        //return;
        continue;
      }
      long now = System.currentTimeMillis();
      if ( message.getTimeToSend() > now ) continue; // return;
      if ( configCtx != null ) message.setConfigurationContext(configCtx);
      MessageContext msgCtx = message.getMessageContext();
              //message.getMessageContext(configCtx);

      AxisOperation operation = msgCtx.getAxisOperation();
      String mep = "http://www.w3.org/ns/wsdl/in-out";
      if ( operation != null ) mep = operation.getMessageExchangePattern();
      //String mep = message.getBean().getAxisOperationMEP();

      String groupId = message.getGroupId();
      int seqNumber = message.getSeqNumber();
      log.debug("About to retransmit message with groupId=" + groupId +
              " and seq=" + seqNumber + " to " + message.getRemoteServiceURL() +
               " \n: " + Util.toString(msgCtx.getEnvelope()));

      //retransmit(msgCtx, message.getRemoteServiceURL(),
      //               message.getBean().getAction(), mep, message.getCallback());
      retransmit(msgCtx, message.getRemoteServiceURL(),
                 msgCtx.getOptions().getAction(), mep, message.getCallback());

      message = store.getGMessage(groupId, seqNumber);
      message.setResendCount(message.getResendCount() + 1);
      int period = message.getRetransmissionInterval();
      boolean expBackoff = message.isExponentialBackoff();
      long nextTime = 0;
      now = System.currentTimeMillis();
      if ( !expBackoff ) nextTime = now + period;
      else
      {
        double exp = (double)(message.getResendCount() - 1);
        nextTime = now + ( (long)period * (long)Math.pow(2, exp) );
      }
      message.setTimeToSend(nextTime);
      store.save(message);
    }
  }

  private MessageContext retransmit(MessageContext msgCtx, String toURL,
                                    String action, String mep, Callback cb)
  {
    if ( msgCtx == null ) return null;
    OperationContext opCtx =
            configCtx.getOperationContext(msgCtx.getMessageID());
    opCtx.setComplete(false);
    if ( mep != null &&
         mep.equalsIgnoreCase("http://www.w3.org/ns/wsdl/out-only") )
    {
      Util.send(msgCtx, toURL, action, null);
      return null;
    }
    else
    {
      opCtx.getMessageContexts().put(OutInAxisOperation.MESSAGE_LABEL_OUT_VALUE, null);
      return Util.invoke(msgCtx, toURL, action, null, cb);
    }
  }

  private boolean shouldBeDeleted(GMessage msg)
  {
    if ( msg.isExpired() ) return true;
    if ( msg.isAcknowledged() ) return true;
    if ( msg.getResendCount() == msg.getMaximumRetransmissionCount() )
         return true;

    return false;
  }

  private void init(MessageContext msgCtx)
  {
    if (msgCtx == null) return;
    configCtx = msgCtx.getConfigurationContext();
    store = (DbStore)configCtx.getAxisConfiguration()
                                  .getParameter(Constants.STORE)
                                  .getValue();
    //executor =
    //      (ScheduledThreadPoolExecutor)configCtx.getAxisConfiguration()
    //                              .getParameter(Constants.SCHEDULED_EXECUTOR)
    //                              .getValue();
/*
    Reliability quality = (Reliability)msgCtx.getProperty(Constants.QUALITY);
    if ( //executor != null &&
            quality != null && configCtx != null )
    {
      //SOAPHeader header = msgCtx.getEnvelope().getHeader();
      //OMElement wsrmReq =
      //   Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
      //groupId =
      //   Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
      //String seq =
      //  Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
      //if ( seq != null && !seq.trim().equals("") )
      //   seqNumber = Integer.parseInt(seq);
      long retransInt = (long)quality.getRetransmissionInterval();

      //executor.scheduleWithFixedDelay(this, 40000, retransInt, MILLISECONDS);
      this.timeInterval = (int) (retransInt / 1000);
    }
*/
  }

  private List<GMessage> getMessagesToResend()
  {
    List<GMessage> gmList = new ArrayList<GMessage>();
    List gmessages = store.getMessagesToResend();
    if ( gmessages != null && gmessages.size() > 0 )
    {
      GMessage g = null;
      for (Object gm : gmessages)
      {
        g = (GMessage)gm;
        if ( !g.isExpired() ) gmList.add(g);
      }
    }
    return gmList;
  }
}