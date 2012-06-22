package org.holodeck.ebms3.module;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.axiom.om.OMElement;

import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.submit.EbMessage;

/**
 * @author Hamid Ben Malek
 */
public class PullWorker extends ControlledPeriodicWorker
{
//  private static final Log log = LogFactory.getLog(PullWorker.class.getName());
  private static final Logger log = Logger.getLogger(PullWorker.class.getName());

  public PullWorker(OMElement pullElement, ConfigurationContext config)
  {
    super(pullElement, config);
  }

  protected void task()
  {
    MsgInfoSet metadata = new MsgInfoSet();
    metadata.setLegNumber(1);
    metadata.setPmode(pmode);
    EbMessage msg = new EbMessage(metadata);
    log.debug("PullWorker: about to pull from mpc " + mpc);
    //msg.inOut(metadata, getCallback());
    MessageContext usrMsg = msg.inOut(metadata);
    if ( usrMsg != null ) getCallback().onMessage(usrMsg);
  }
}