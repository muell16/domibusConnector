package org.holodeck.ebms3.consumers;

import org.apache.axis2.context.MessageContext;
import org.holodeck.ebms3.module.MsgInfo;
import java.util.*;

/**
 *  This interface is implemented by any type of consumer party behind an MSH.
 *
 * @author Hamid Ben Malek
 */
public interface EbConsumer
{
  public void push(MsgInfo msgInfo, MessageContext outMsgCtx);

  public void pull(MsgInfo msgInfo, MessageContext outMsgCtx);

  public void setParameters(Map<String, String> parameters);
}