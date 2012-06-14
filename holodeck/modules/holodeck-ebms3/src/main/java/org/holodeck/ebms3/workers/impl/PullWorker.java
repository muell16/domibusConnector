package org.holodeck.ebms3.workers.impl;


import org.apache.axis2.context.MessageContext;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.submit.EbMessage;
import org.holodeck.ebms3.workers.Task;
import org.holodeck.common.soap.Util;

import java.util.Map;

/**
 * @author Hamid Ben Malek
 */
public class PullWorker implements Task
{
  private static final Log log =
             LogFactory.getLog(PullWorker.class.getName());

  protected String pmode;
  protected String mpc;
  protected String callbackClass;

  public void run()
  {
    MsgInfoSet metadata = new MsgInfoSet();
    metadata.setLegNumber(1);
    metadata.setPmode(pmode);
    EbMessage msg = new EbMessage(metadata);
    log.debug("PullWorker: about to pull from mpc " + mpc);
    MessageContext usrMsg = msg.inOut(metadata);
    if ( usrMsg != null )
    {
      AxisCallback handler = getCallback();
      if ( handler != null ) handler.onMessage(usrMsg);
    }
  }

  public void setParameters(Map<String, String> parameters)
  {
    if ( parameters == null ) return;
    pmode = parameters.get("pmode");
    mpc = parameters.get("mpc");
    callbackClass = parameters.get("callbackClass");
  }
    
  protected AxisCallback getCallback()
  {
    if ( callbackClass == null || callbackClass.trim().equals("") )
         return null;
    return (AxisCallback) Util.createInstance(callbackClass);
  }
}