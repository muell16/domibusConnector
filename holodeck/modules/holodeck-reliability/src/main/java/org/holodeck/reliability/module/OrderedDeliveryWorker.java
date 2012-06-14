package org.holodeck.reliability.module;

import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.*;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisEngine;
//import org.apache.axis2.engine.AxisEngine;
//import org.apache.axiom.soap.SOAPHeader;
//import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.holodeck.reliability.persistent.OMessage;
import org.holodeck.reliability.persistent.ReceiverGroup;
//import org.holodeck.common.soap.Util;

/**
 * @author Hamid Ben Malek
 */
public class OrderedDeliveryWorker extends PeriodicWorker //implements Runnable
{
  //protected ScheduledThreadPoolExecutor executor;
  protected ConfigurationContext configCtx;
  protected DbStore store;

  private static final Log log =
             LogFactory.getLog(OrderedDeliveryWorker.class.getName());

  public OrderedDeliveryWorker(ConfigurationContext confCtx) { init(confCtx); }

  //public void run()
  protected void task()
  {
    List messages = store.getOMessagesToDeliver();
    if ( messages == null || messages.size() == 0 ) return;
    List<OMessage> nexp = new ArrayList<OMessage>();
    OMessage om = null;
    for (Object m : messages)
    {
      om = (OMessage)m;
      if ( !om.isExpired() ) nexp.add(om);
    }
    if ( nexp.size() == 0 ) return;

    work(nexp);
  }

  private void work(List<OMessage> nexp)
  {
    boolean loop;
    do
    {
      loop = false;
      for (OMessage aNexp : nexp)
      {
        OMessage m = (OMessage) aNexp;
        ReceiverGroup g =
          (ReceiverGroup)store.findByUP(ReceiverGroup.class, "groupId",
                                        m.getGroupId());
        if ( !g.isClosed() && !g.isExpired() )
        {
          int lastSeq = g.getLastDeliveredSeq();
          log.debug("lastSeq=" + lastSeq);
          if (m.getSeqNumber() == (lastSeq + 1) &&
              !m.isDelivered())
          {
            log.debug("----- about to deliver seq=" + m.getSeqNumber());
            MessageContext mc = m.getMessageContext();
                    //m.getMessageContext(configCtx);

            AxisEngine engine = new AxisEngine(configCtx);
            try
            {
               engine.resume(mc);
            // resume mc ...  (needs to be done...)
            //boolean sent =
            //SoapUtil.httpPost(m.getSOAPMessage(), m.getServiceURL());
            //messages.remove(m);
            // if ( sent )
            //{
                m.setDelivered(true);
                g.setLastDeliveredSeq(1 + lastSeq);
                int lds = 1 + lastSeq;
                log.debug("---- group's lastDelivSeq set to " + lds);
                store.update(m);
                loop = true;
            }
            catch(Exception ex) { ex.printStackTrace(); }
             //}
          }
        }
      }
    }
    while ( loop );
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
}