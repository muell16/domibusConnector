package org.holodeck.common.soap;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.DataSource;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.ImageIO;
import org.apache.axiom.attachments.*;
import org.apache.axiom.soap.*;
import org.apache.axiom.attachments.utils.IOUtils;
import javax.mail.internet.*;
import java.io.*;
import java.awt.*;

/**
 * @author Hamid Ben Malek
 */
public class Test
{
  private static String cid = null;
  public static void main(String[] args)
  {
    Test test = new Test();
    //Message msg = test.createMessage();
    try
    {
       test.testEnvelopeSerialization(); 

//       String file =
//         "F:\\Projects\\Jin\\src\\conf\\sample\\media\\billGlobe2.png";
//       DataHandler dh =
//          test.createDataHandler(file, "image/x-png");
//       test.writeToFile(dh, "F:\\DataHandler.png");
//       System.out.println("=== dumpted the whole DataHandler to F:\\DataHandler.png");
//       byte[] contents = test.getContents(dh);
//       test.writeToFile(contents, "F:\\DataHandler-Contents.png");
//       System.out.println("Wrote the bytes of the datahandler to ...-Contents.png");


      //FileOutputStream fos = new FileOutputStream("F:\\msg.log");
      //msg.writeTo(fos);
      //msg.init(new FileInputStream("F:\\msg.log"));
      //test.writeToFile(msg.getMessageContext(), "F:\\msg-context.log");
//      test.serialize(msg.getMessageContext(), "F:\\msg-context.log");
//      System.out.println("====== MessageContext written to file F:\\msg-context.log =====");
//      MessageContext msgContext =
//            (MessageContext)test.deserialize("F:\\msg-context.log");
//      System.out.println("==== MessageContext deserialized from file F:\\msg-context.log");
//      System.out.println("SOAP Envelope is: " + msgContext.getEnvelope());
//      DataHandler att = msgContext.getAttachment(cid);
//      System.out.println("=== Writing the attachment to a file F:\\attachment.png");
//      try { att.writeTo(new FileOutputStream("F:\\attachment.png")); }
//      catch(Exception ex) { ex.printStackTrace(); }
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public void testEnvelopeSerialization()
  {
    Message msg = createMessage();
    SOAPEnvelope env = msg.getEnvelope();
    System.out.println("====== Before serialization:");
    System.out.println(env.toString());
    serialize(env, "F:\\env.ser");
    Object env2 = deserialize("F:\\env.ser");
    System.out.println("====== After Serialization:");
    System.out.println(env2.toString());
  }

  private OMElement createHeader()
  {
    Element mess =
      new Element("Messaging", "http://www.oasis-open.org/ebxml/ebms3.xsd", "eb");
    Element info = new Element("MessageInfo", "http://www.oasis-open.org/ebxml/ebms3.xsd", "eb");
    mess.addChild(info);
    Element messId = new Element("MessageId", "http://www.oasis-open.org/ebxml/ebms3.xsd", "eb");
    info.addChild(messId);
    messId.setText("sdk@yahoo.com");
    //return mess;
    return mess.getElement();
  }

  private Message createMessage()
  {
    Message msg = new Message();
    msg.addHeaderElement(createHeader());
    String file = "F:\\Projects\\Jin\\src\\conf\\sample\\media\\amanda-peet-10.jpg";
    DataHandler dh = createDataHandler(file, "image/jpeg");
    cid = msg.addAttachment(dh);
    return msg;
  }

  private void serialize(Object obj, String fileName)
  {
    try
    {
      FileOutputStream fout = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fout);
      oos.writeObject(obj);
      oos.close();
    }
    catch (Exception e) { e.printStackTrace(); }
  }

  private Object deserialize(String fileName)
  {
    Object result = null;
    try
    {
      FileInputStream fin = new FileInputStream(fileName);
      ObjectInputStream ois = new ObjectInputStream(fin);
      result = ois.readObject();
      ois.close();
    }
    catch (Exception e) { e.printStackTrace(); }
    return result;
  }

  private void writeToFile(MessageContext context, String fileName)
  {
    SOAPMessageFormatter smf = new SOAPMessageFormatter();
    OMOutputFormat format = new OMOutputFormat();
    format.setDoingSWA(true);
    format.setCharSetEncoding("UTF-8");
    try
    {
      smf.writeTo(context, format, new FileOutputStream(fileName), true);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  private DataHandler createDataHandler(String pngFile, String type)
  {
    DataHandler dh = null;
    try
    {
      InternetHeaders headers = new InternetHeaders();
      headers.addHeader("Content-Type", type);
      FileInputStream fis = new FileInputStream(pngFile);
      MimeBodyPart mimePart =
           new MimeBodyPart(headers, IOUtils.getStreamAsByteArray(fis));
      System.out.println("Encoding is: " + mimePart.getEncoding());
      System.out.println("cid is: " + mimePart.getContentID());
      return mimePart.getDataHandler();

//      PartOnMemory  part = new PartOnMemory(fis);
//      part.addHeader("Content-Type", "image/x-png");
//      return part.getDataHandler();
    }
    catch(Exception e) { e.printStackTrace(); }

//    try
//    {
//      File file = new File("F:\\Projects\\hotels\\Hotel-Finder.png");
//      ImageInputStream fiis = new FileImageInputStream(file);
//      Image image = ImageIO.read(fiis);
//      DataSource imageDS = new DataSourceImpl("image/jpeg","test.jpg",image);
//      dh = new DataHandler(imageDS);
//
//    }
//    catch(Exception ex) { ex.printStackTrace(); }
    return dh;
  }

  private byte[] getContents(DataHandler dh)
  {
    if (dh == null) return null;
    try
    {
      InputStream is = dh.getInputStream();
      return IOUtils.getStreamAsByteArray(is);
    }
    catch(Exception e) { e.printStackTrace(); }
    return null;
  }

  private void writeToFile(byte[] contents, String toFile)
  {
    if (contents == null || toFile == null) return;
    try
    {
      FileOutputStream fos = new FileOutputStream(toFile);
      fos.write(contents);
      fos.flush();
      fos.close();
    }
    catch(Exception e) { e.printStackTrace(); }
  }

  private void writeToFile(DataHandler dh, String toFile)
  {
    if (dh == null || toFile == null) return;
    try
    {
      FileOutputStream fos = new FileOutputStream(toFile);
      dh.writeTo(fos);
      fos.flush();
      fos.close();
    }
    catch(Exception e) { e.printStackTrace(); }
  }
}
