package org.holodeck.ebms3.consumers.impl;

//import org.springframework.jms.core.MessageCreator;
import org.holodeck.common.soap.Util;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.BytesMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import org.apache.axis2.context.MessageContext;

/**
 * @author Hamid Ben Malek
 */
public class MsgCreator // implements MessageCreator
{
  protected MessageContext msgCtx;
  protected boolean saveMessageToDisk = true;

  public MsgCreator(MessageContext ctx)
  {
    this.msgCtx = ctx;
  }

  public MsgCreator(MessageContext ctx, boolean saveMsg)
  {
    this.msgCtx = ctx;
    this.saveMessageToDisk = saveMsg;
  }

  public Message createMessage(Session session)
  {
    byte[] contents;
    try
    {
      if ( saveMessageToDisk )
      {
        String fileName = getSaveLocation() + File.separator +
                                 System.currentTimeMillis() + ".mime";
        Util.writeMessage(msgCtx, new File(fileName));
        contents = getBytesFromFile(new File(fileName));
      }
      else
      {
        contents = Util.getMessageBytes(msgCtx);
      }
      BytesMessage msg = session.createBytesMessage();
      msg.writeBytes(contents);
      return msg;
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  private static byte[] getBytesFromFile(File file) throws IOException
  {
    InputStream is = new FileInputStream(file);
    long length = file.length();
    if ( length > Integer.MAX_VALUE )
         throw new IOException("File is too large: " + file.getName());

    byte[] bytes = new byte[(int)length];
    int offset = 0;
    int numRead;
    while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)
    {
      offset += numRead;
    }

    if ( offset < bytes.length )
          throw new IOException("Could not completely read file " + file.getName());

    is.close();
    return bytes;
  }

  private String getSaveLocation()
  {
    String receivedMsgsFolder =
          org.holodeck.ebms3.module.Constants.getReceivedFolder();
    String path = receivedMsgsFolder + File.separator + "Messages_mq";
    new File(path).mkdirs();
    return path;
  }
}