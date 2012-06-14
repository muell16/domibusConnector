package org.holodeck.ebms3.module;

import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.persistent.UserMsgToPush;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.submit.EbMessage;
//import org.holodeck.ebms3.pmodes.Leg;
//import org.holodeck.common.client.Client;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Hamid Ben Malek
 */
public class SenderWorker extends PeriodicWorker //implements Runnable
{
  private static final Log log =
             LogFactory.getLog(SenderWorker.class.getName());

  public void init()
  {
    //Constants.executor.scheduleWithFixedDelay(this, 30, 40, TimeUnit.SECONDS);
  }

  protected void task()
  {
    List<UserMsgToPush> messages = Constants.store.getMessagesToPush();
    if ( messages == null || messages.size() == 0 ) return;
    for (UserMsgToPush message : messages)
    {
      send(message);
      message.setPushed(true);
      Constants.store.save(message);
    }
  }
  /*
  public void run()
  {
    // read from the messages from the UserMsgToPush table for those
    // who are not yet sent and have their leg number with the correct value
    // and the correct MEP, and then send them out (that is push them out).

    List<UserMsgToPush> messages = Constants.store.getMessagesToPush();
    if ( messages == null || messages.size() == 0 ) return;

    for (UserMsgToPush message : messages)
    {
      send(message);
      message.setPushed(true);
      Constants.store.save(message);
    }
  }
  */
  private void send(UserMsgToPush message)
  {
    //Callback cb = null;
    AxisCallback cb = null;
    if ( message.getCallbackClass() != null &&
         !message.getCallbackClass().trim().equals("") )
    {
      try
      {
        cb = (AxisCallback)Util.createInstance( message.getCallbackClass() );
           //(Callback)Class.forName(message.getCallbackClass()).newInstance();
      }
      catch(Exception ex) { log.debug(ex.getMessage()); }
    }
    MsgInfoSet metadata = message.getMsgInfoSetBean();

    //EbMessage sender =
    //    new EbMessage( message.getMessageContext(Constants.configContext) );
    log.debug("SenderWorker: about to send to " + message.getToURL());
    //sender.send(metadata, cb);
    message.send(metadata, cb);

    /*
    Client client =
        new Client(Constants.configContext,
                   message.getMessageContext(Constants.configContext));
    client.getMessageContext().setProperty("MESSAGE_INFO_SET", metadata);
    String[] modules = Constants.engagedModules;
    Leg leg = EbUtil.getLeg(metadata, Constants.pmodesMap);
    String toURL = message.getToURL();
    log.debug("SenderWorker: about to send to " + toURL);
    client.inOut(toURL, leg.getSoapAction(), modules, cb);
    */
  }
}
