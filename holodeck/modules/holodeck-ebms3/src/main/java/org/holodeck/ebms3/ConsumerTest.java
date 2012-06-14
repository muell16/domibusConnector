package org.holodeck.ebms3;

import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.consumers.EbConsumer;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.module.PartInfo;
import org.apache.axis2.context.MessageContext;
import java.io.*;
import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class ConsumerTest implements EbConsumer
{
  protected Map<String, String> parameters;

  public void setParameters(Map<String, String> properties)
  {
    this.parameters = properties;
  }

  public void push(MsgInfo msgInfo, MessageContext outMsgCtx)
  {
    System.out.println("======================= ConsumerTest Application ===================");
    MessageContext msgCtx = MessageContext.getCurrentMessageContext();
    writeAttachments(msgCtx, msgInfo.getParts());
    System.out.println("====================================================================");
  }

  public void pull(MsgInfo msgInfo, MessageContext outMsgCtx)
  {
  }

  private String getSaveLocation()
  {
    String dir = parameters.get("directory");
    File location  = new File(dir);
    if ( location.exists() && location.isDirectory() ) return dir;
    String receivedMsgsFolder =
          org.holodeck.ebms3.module.Constants.getReceivedFolder();
    String path = receivedMsgsFolder + File.separator + dir;
    new File(path).mkdirs();
    return path;
  }

  private void writeAttachments(MessageContext msgCtx, List<PartInfo> parts)
  {
    if ( msgCtx == null || parts == null || parts.size() <= 0 ) return;
    List<String> cids = new ArrayList<String>();
    for (PartInfo part : parts) cids.add(part.getCid());
    Util.writeAttachments(msgCtx, cids, new File(getSaveLocation()));
  }
}