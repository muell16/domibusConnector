package org.holodeck.ebms3.workers.impl;

import java.util.List;

import org.apache.axis2.client.async.AxisCallback;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.persistent.UserMsgToPush;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.logging.level.Message;
import org.holodeck.logging.persistent.MessageInfo;

/**
 * @author Hamid Ben Malek
 */
public class SenderWorker implements Runnable
{
//  private static final Log log = LogFactory.getLog(SenderWorker.class.getName());
	private static final Logger log = Logger.getLogger(SenderWorker.class.getName());

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
//    log.debug("SenderWorker: about to send to " + message.getToURL());
    log.log(Message.MESSAGE, new MessageInfo(message.getId(), metadata.getProducer().getName(), metadata.getProducer().getRole(),message.getToURL(),"","eCodex-Test","","","IT-NRW", "statusOK"));
//    log.info(new MessageInfo(message.getId(), metadata.getProducer().getName(), metadata.getProducer().getRole(),message.getToURL(),"","eCodex-Test","","","IT-NRW"));
    message.send(metadata, cb);
  }
}