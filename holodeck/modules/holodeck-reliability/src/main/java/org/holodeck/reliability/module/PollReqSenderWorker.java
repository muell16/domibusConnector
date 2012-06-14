package org.holodeck.reliability.module;

//import java.util.concurrent.*;
//import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.*;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.holodeck.reliability.persistent.GMessage;
import org.holodeck.reliability.packaging.PollRequest;
import org.holodeck.common.soap.Util;

/**
 * This worker sends poll request messages (it reads the “GMessages”
 * table, find out which messages have their replyPattern set to "Poll",
 * have not been acknowledged yet, and send poll requests to inquire
 * about their status).
 *
 * @author Hamid Ben Malek
 */
public class PollReqSenderWorker extends PeriodicWorker //implements Runnable
{
  //protected ScheduledThreadPoolExecutor executor;
  protected ConfigurationContext configCtx;
  protected DbStore store;

  private static final Log log =
             LogFactory.getLog(PollReqSenderWorker.class.getName());

  public PollReqSenderWorker(ConfigurationContext confCtx) { init(confCtx); }

  //public void run()
  protected void task()
  {
    List messages = store.getMessagesToPoll();
    if ( messages == null || messages.size() == 0 ) return;
    Map<String, PollRequest> pReqs = new HashMap<String, PollRequest>();
    Map<String, String> actionMap = new HashMap<String, String>();
      
    GMessage gm = null;
    for (Object message : messages)
    {
      gm = (GMessage)message;
      PollRequest pReq = pReqs.get(gm.getRemoteServiceURL());
      if ( pReq == null )
      {
        pReq = new PollRequest(OMAbstractFactory.getSOAP11Factory());
        pReqs.put(gm.getRemoteServiceURL(), pReq);
      }
      int seq = gm.getSeqNumber();
      if ( seq == -1 ) pReq.addRefToMessageIds(gm.getGroupId());
      else pReq.addRefToMessageIds(gm.getGroupId(), seq, seq);

      //if ( actionMap.get(gm.getRemoteServiceURL() ) == null )
      //     actionMap.put(gm.getRemoteServiceURL(), gm.getBean().getAction());
    }
    for ( String toURL : pReqs.keySet() )
    {
      PollRequest pollRequest = pReqs.get(toURL);
      String[] modules = new String[]{Constants.getAxisModule().getName()};
      MessageContext mc =
          sendPollRequest(pollRequest, toURL, null, modules);
          //sendPollRequest(pollRequest, toURL, actionMap.get(toURL), modules);

      log.debug("Received response to PollRequest Signal:" +
                 mc.getEnvelope().toString());
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

  private MessageContext sendPollRequest(PollRequest pollReq, String toURL,
                                         String action, String[] modules)
  {
    if (pollReq == null) return null;
    SOAPEnvelope env = Util.createEnvelope(1.1);
    //env.getHeader().addChild(pollReq.getRoot());
    pollReq.addToHeader(env);
    MessageContext msgContext = new MessageContext();
    try
    {
      msgContext.setEnvelope(env);
      msgContext.setSoapAction(action);
      log.debug("About to send a PollRequest Signal");
      return Util.invoke(msgContext, toURL, action, modules);
    }
    catch(Exception e) { e.printStackTrace(); return null; }
  }
}