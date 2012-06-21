package org.holodeck.reliability.module;

//import java.util.concurrent.*;
//import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.List;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.persistent.CallbackAck;

/**
 * This worker reads the acknolegedment messages from the "CallbackAck" table
 * and resend them as separate requests. Only the Acks who have non-null ackTo
 * address are sent.
 *
 * @author Hamid Ben Malek
 */
public class CallbackAckWorker extends PeriodicWorker //implements Runnable
{
  //protected ScheduledThreadPoolExecutor executor;
  protected ConfigurationContext configCtx;
  protected DbStore store;

//  private static final Log log = LogFactory.getLog(CallbackAckWorker.class.getName());
  private static final Logger log = Logger.getLogger(CallbackAckWorker.class.getName());

  public CallbackAckWorker(ConfigurationContext confCtx) { init(confCtx); }

  //public void run()
  protected void task()
  {
    List acks = store.getCallbackAcks();
    if ( acks == null || acks.size() == 0 ) return;
    log.debug("About to send a callback ack...");
    for (Object ack : acks)
    {
      CallbackAck ca = (CallbackAck)ack;
      if ( ca.getAckTo() != null && !ca.getAckTo().trim().equals("") &&
           !ca.isSent() )
      {
        sendAck(ca);
        ca.setSent(true);
        store.save(ca);
        return;
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

  private void sendAck(CallbackAck ack)
  {
    if ( ack == null ) return;
    try
    {
      MessageContext mc = new MessageContext();
      SOAPEnvelope env = Util.createEnvelope(1.1);
      SOAPFactory factory = (SOAPFactory)env.getOMFactory();
      SOAPHeader header = env.getHeader();
      if (header == null) header = factory.createSOAPHeader(env);
      //header.addChild(ack.getResponseHeaderBlock(factory));
      ack.addToHeader(env);
      mc.setEnvelope(env);

      Util.send(mc, ack.getAckTo(), "wsrm", null);

      log.debug("callback ack for groupId=" +
                ack.getGroupId() + " and seq=" + ack.getSeqNumber() +
                " has been sent:" + env.toStringWithConsume());
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
}