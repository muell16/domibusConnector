package org.holodeck.ebms3.submit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import org.holodeck.common.persistent.Attachment;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.EbUtil;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.module.PartInfo;
import org.holodeck.ebms3.persistent.ReceivedUserMsg;

/**
 *  This is a Callback class that simply saves the received user message
 *  into database (the attachments of the received message are also
 *  persisted in the "Received_Messages" folder). This class is simply
 *  a convenience that can be used whenever a Callback class is needed.
 *
 * @author Hamid Ben Malek
 */
public class SaveReceivedMsg implements AxisCallback
{
//  private static final Log log = LogFactory.getLog(SaveReceivedMsg.class.getName());
  private static final Logger log = Logger.getLogger(SaveReceivedMsg.class.getName());

  public void onComplete() {}
  public void onError(Exception ex) { ex.printStackTrace(); }
  public void onFault(MessageContext ctx)
  {
    System.out.println(ctx.getEnvelope().toString());
  }

  public void onMessage(MessageContext msgCtx)
  {
    saveReceivedMessage(msgCtx);
  }

  public void saveReceivedMessage(MessageContext msgCtx)
  {
    log.debug("onMessage(msgCtx): receiving a pulled user message...");
    MsgInfo msgInfo = (MsgInfo)msgCtx.getProperty(Constants.IN_MSG_INFO);
    if ( msgInfo == null ) msgInfo = EbUtil.createMsgInfo(msgCtx);
    if ( msgInfo == null ) return;
    String dir = getSaveLocation(msgInfo.getMpc());
    ReceivedUserMsg receivedUM = new ReceivedUserMsg(msgCtx, msgInfo);
    // saving the attachments:
//    String receivedMessagesFolder = Constants.getReceivedFolder();
//    String folder = msgInfo.getMessageId();
//    if ( folder.indexOf(":") >= 0 ) folder = folder.replaceAll(":", "-");
    List<PartInfo> parts = msgInfo.getParts();
    if ( parts == null || parts.size() <= 0 )
    {
      log.debug("There are no attachments in received pulled message");
      return;
    }

    Options options = msgCtx.getOptions();
    if ( options != null )
    {
      String tempAttachDir =
           (String)options.getProperty(org.apache.axis2.Constants.Configuration.ATTACHMENT_TEMP_DIR);
      //System.out.println("========= tempAttachDir=" + tempAttachDir);
      String attachDir = (String)msgCtx.getConfigurationContext()
              .getAxisConfiguration()
              .getParameter(org.apache.axis2.Constants.Configuration.ATTACHMENT_TEMP_DIR).getValue();
      //System.out.println("======== attachDir=" + attachDir);
      if ( attachDir != null )
           options.setProperty(org.apache.axis2.Constants.Configuration.ATTACHMENT_TEMP_DIR,
                        attachDir);
    }
    else System.out.println("======== options is null");
    Attachments atts = msgCtx.getAttachmentMap();
    for (PartInfo part : parts)
    {
      String cid = part.getCid();
      DataHandler dh = atts.getDataHandler(cid);
      String name = cid;
      if ( cid.indexOf(":") >= 0 ) name = name.replaceAll(":", "-");
      String extension = Util.getFileExtension(dh.getContentType());
//      String path = receivedMessagesFolder + File.separator +
//                    folder + File.separator + name + "." + extension;
      String path = dir + File.separator + name + "." + extension;
      File att = new File(path);
      log.debug("about to write attachment " + name + "." + extension);
      Util.writeDataHandlerToFile(dh, att);
      Attachment attach = new Attachment(path);
      attach.setContentID(cid);
      attach.setContentType(dh.getContentType());
      receivedUM.getAttachments().add(attach);
      writeEnvelope(msgCtx, dir);
      writeSoapHeader(msgCtx, dir);
    }
    
    Constants.store.save(receivedUM);
    System.out.println("======= Received message was saved");
  }

  private String getSaveLocation(String mpc)
  {
    if ( mpc == null || mpc.trim().equals("") ) mpc = "default";
    if ( mpc.startsWith("mpc://") ) mpc = mpc.substring(6);
    else if ( mpc.startsWith("mpc//:") ) mpc = mpc.substring(6);
    else if ( mpc.startsWith("http://") ) mpc = mpc.substring(7);
    mpc = mpc.replaceAll(":", "-");
    mpc = mpc.replaceAll("/", "_");
    String dir = "Messages_" + mpc + File.separator + "Msg_" + getDate();
    File location  = new File(dir);
    if ( location.exists() && location.isDirectory() ) return dir;
    String receivedMsgsFolder =
          org.holodeck.ebms3.module.Constants.getReceivedFolder();
    String path = receivedMsgsFolder + File.separator + dir;
    boolean b = new File(path).mkdirs();
    if ( !b ) System.out.println("Unable to create direcoty " + path);
    return path;
  }
  private String getDate()
  {
    Calendar now = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd'@'HH.mm.ss");
    return sdf.format(now.getTime());
  }
  private void writeEnvelope(MessageContext msgCtx, String location)
  {
    try
    {
      String file = location + File.separator + "envelope.xml";
      Util.prettyPrint(msgCtx.getEnvelope(), file);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
  private void writeSoapHeader(MessageContext msgCtx, String location)
  {
    OMElement header =
         (OMElement)msgCtx.getProperty(Constants.IN_SOAP_HEADER);
    if ( header == null ) return;
    try
    {
      String file = location + File.separator + "SOAP-Header.xml";
      Util.prettyPrint(header, file);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
}