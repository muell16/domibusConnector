package org.holodeck.ebms3.workers.impl;

import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.persistent.UserMsgToPush;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.module.Constants;

import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.io.File;

/**
 * @author Hamid Ben Malek
 */
public class SenderWorker implements Runnable
{
  private static final Log log =
             LogFactory.getLog(SenderWorker.class.getName());

  public void run()
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

  private void send(UserMsgToPush message)
  {
    AxisCallback cb = null;
    if ( message.getCallbackClass() != null &&
         !message.getCallbackClass().trim().equals("") )
    {
      try
      {
        cb = (AxisCallback)Util.createInstance( message.getCallbackClass() );
      }
      catch(Exception ex) { log.debug(ex.getMessage()); }
    }
    MsgInfoSet metadata = message.getMsgInfoSetBean();

    log.debug("SenderWorker: about to send to " + message.getToURL());
    message.send(metadata, cb);
  }
}