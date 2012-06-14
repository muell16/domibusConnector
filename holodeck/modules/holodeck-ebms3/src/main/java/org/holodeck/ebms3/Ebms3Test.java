package org.holodeck.ebms3;

//import org.holodeck.common.persistent.*;
//import org.holodeck.common.client.Client;
import org.holodeck.common.soap.Util;
import org.apache.axis2.context.ConfigurationContext;

import org.apache.axis2.context.*;
import org.apache.axis2.transport.TransportUtils;
import org.apache.axis2.transport.http.SOAPMessageFormatter;
import org.apache.axiom.attachments.Attachments;

import javax.activation.FileDataSource;
import javax.activation.DataHandler;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamReader;
import java.io.*;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
//import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;

/**
 * @author Hamid Ben Malek
 */
public class Ebms3Test
{
   //static String repoDir =
   //   "F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck-temp\\WEB-INF";
   //static EbMessage client = new EbMessage(repoDir, repoDir + "\\conf\\axis2.xml");

   public static void main(String[] args)
   {
     String inFile = "F:\\Downloads\\we-had-enough.mp4";
     Util.doCompressFile(inFile);

     //createUserMsgToPush();
     //testAttachment();
     //testMsgCtxExternalize();
//     File file = new File("D:\\message");
//     String ct = writeMessage(createMessageContext(), file);
//     System.out.println("======= message written to D:\\message");
//     System.out.println("======= ct=" + ct);

     //String boundary = "boundary_A1D71728EEF8753E4A1199842685954";
//     MessageContext mc = readMessage(file, ct);
//     System.out.println("=========== soap envelope is:");
//     if ( mc.getEnvelope() != null )
//          System.out.println(mc.getEnvelope().toString());
//     else System.out.println("======== mc.getEnvelope() is null");
     /*
     Attachments atts = mc.getAttachmentMap();
     String[] cids = atts.getAllContentIDs();
     System.out.println("============== cids.length=" + cids.length);
     if ( cids != null && cids.length >= 1 )
     {
       System.out.println("====== there is one attachment which is:");
       try
       {
         writeToFile(atts.getDataHandler(cids[cids.length - 1]),
                     new File("D:\\attachment.jpg") );
         System.out.println("========= wrote read attachment to D:\\attachment.jpg");
       }
       catch(Exception ex) { ex.printStackTrace(); }
     }
     */
//     System.out.println("======= now writing the read message back to another file...");
//     writeMessage(mc, new File("D:\\message-read"));
   }
   /*
   public static void createUserMsgToPush()
   {
      ConfigurationContext configCtx = client.getConfigurationContext();
      if ( configCtx == null )
      {
        System.out.println("configCtx is null");
        return;
      }
      else System.out.println("Ebms3Test: configCtx is not null");
      UserMsgToPush msg = new UserMsgToPush(configCtx);
      System.out.println("Ebms3Test: just constructed UserMsgToPush");
      msg.addToBody("F:\\Projects\\BPM\\Engine\\bin\\Debug\\Engine.dll.config");
      System.out.println("Ebms3Test: added payload to body");

      //DbStore store = (DbStore)configCtx.getAxisConfiguration()
      //                                .getParameter(Constants.STORE).getValue();
      DbStore store = new DbStore("ebms3-mysql");
      if ( store != null )
      {
        //StoredMessageBean bean = msg.getBean();
        //store.save(bean);
        //msg.setBean(bean);
        store.save(msg);
        System.out.println("msg saved to database");
      }
      else System.out.println("store is null");
   }
   */

   public static void testMsgCtxExternalize()
   { /*
     MessageContext msgCtx = new MessageContext();
     try
     {
       msgCtx.setEnvelope(createEnvelope(1.1));
       File att =
         new File("F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Submitted_Messages\\Upload-1\\MVI_3250.AVI");
       FileDataSource fileDataSource = new FileDataSource(att);
       //fileDataSource.setFileTypeMap(getMimeTypes());
       DataHandler dataHandler = new DataHandler(fileDataSource);
       String cid = msgCtx.addAttachment(dataHandler);
       System.out.println("==== attachment added with cid=" + cid);

       FileOutputStream fos = new FileOutputStream("F:\\message");
       ObjectOutput out = new ObjectOutputStream(fos);
       OMOutputFormat format = new OMOutputFormat();
       format.setDoingSWA(true);
       format.setSOAP11(true);
       format.setCharSetEncoding("UTF-8");
       System.out.println("===== about to write message to F:\\message");
       MessageExternalize.writeExternal(out, msgCtx, null, format);
       System.out.println("===== message written successfully");
     }
     catch(Exception ex) { ex.printStackTrace(); }
     */
     try
     {
       ObjectInput in =
            new ObjectInputStream(new FileInputStream("F:\\message"));
       MessageContext ctx = new MessageContext();
       //MessageExternalize.readExternal(in, ctx, null);
       System.out.println("==== message was read from file F:\\message");

       Attachments attachments = ctx.getAttachmentMap();
       if ( attachments == null ) return;
       String[] cids = attachments.getAllContentIDs();
       System.out.println("==== cids.length=" + cids.length);
       if ( cids == null || cids.length <= 1 ) return;

       DataHandler dh = attachments.getDataHandler(cids[1]);
       File file = new File("F:\\attachment.avi");
       System.out.println("about to write attachment to file F:\\attachment.avi");
       writeToFile(dh, file);
     }
     catch(Exception ex) { ex.printStackTrace(); }
   }

   public static void testAttachment()
   {
      MessageContext msgCtx = new MessageContext();
      File att =
       new File("F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Submitted_Messages\\Upload-1\\add.png");
      FileDataSource fileDataSource = new FileDataSource(att);
      //fileDataSource.setFileTypeMap(getMimeTypes());
      DataHandler dataHandler = new DataHandler(fileDataSource);
      msgCtx.addAttachment(dataHandler);
      System.out.println("added attachment add.png");

      Attachments attachments = msgCtx.getAttachmentMap();
      if ( attachments == null ) return;
      String[] cids = attachments.getAllContentIDs();
      if ( cids == null || cids.length == 0 ) return;

      DataHandler dh = attachments.getDataHandler(cids[0]);
      File file = new File("D:\\attachment.png");
      System.out.println("about to write attachment to file D:\\attachment.png");
      writeToFile(dataHandler, file);
   }

   private static void writeToFile(DataHandler dataHandler, File file)
   {
     if ( dataHandler == null || file == null ) return;
     try
     {
       FileOutputStream fileOutputStream = new FileOutputStream(file);
       dataHandler.writeTo(fileOutputStream);
       fileOutputStream.flush();
       fileOutputStream.close();
     }
     catch(Exception ex) { ex.printStackTrace(); }
   }

  public static SOAPEnvelope createEnvelope(double soapVersion)
  {
    SOAPFactory omFactory = null;
    if ( soapVersion < 1.2 ) omFactory = OMAbstractFactory.getSOAP11Factory();
    else omFactory = OMAbstractFactory.getSOAP12Factory();

    SOAPEnvelope envelope = omFactory.getDefaultEnvelope();
    envelope.declareNamespace("http://www.w3.org/1999/XMLSchema-instance/", "xsi");
    envelope.declareNamespace("http://www.w3.org/1999/XMLSchema", "xsd");
    return envelope;
  }

  public static MessageContext createMessageContext()
  {
    /*
    Client client = new Client();
    client.addToBody("F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Submitted_Messages\\Upload-1\\metadata.xml");
    //client.addFileAttachment("F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Submitted_Messages\\Upload-1\\MVI_3250.AVI");
    client.addFileAttachment("F:\\Serial-Number_Microsoft-Office-2007.txt");
    return client.getMessageContext();
    */
    MessageContext ctx = new MessageContext();
    try
    {
      String xml = "F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Submitted_Messages\\Upload-1\\metadata.xml";
      ctx.setEnvelope(createEnvelope(1.1));
      ctx.getEnvelope().getBody().addChild(Util.rootElement(new File(xml)));
      String brad = "F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Submitted_Messages\\Upload-1\\brad.jpg";
      String ipod = "F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Submitted_Messages\\Upload-1\\apple_ipodtouch.mov";
      FileDataSource fds = new FileDataSource(ipod);
      fds.setFileTypeMap(Util.getMimeTypes());
      DataHandler dh = new DataHandler(fds);
      ctx.addAttachment(dh);

      Attachments atts = ctx.getAttachmentMap();
      String[] cids = atts.getAllContentIDs();
      System.out.println("============== created msg context: cids.length=" + cids.length);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return ctx;
  }

  public static String writeMessage(MessageContext ctx, File file)
  {
    if ( ctx == null || file == null ) return null;
    String name = UUIDGenerator.getUUID();
    name = name.substring(name.lastIndexOf(":") + 1);
    String boundary = "boundary_" + name;
    OMOutputFormat format = new OMOutputFormat();
    format.setDoingSWA(true);
    format.setDoOptimize(false);
    format.setSOAP11(true);
    format.setCharSetEncoding("UTF-8");
    format.setMimeBoundary(boundary);
    format.setAutoCloseWriter(true);

    SOAPMessageFormatter formatter = new SOAPMessageFormatter();
    //SOAPMessageFormatterUtil formatter = new SOAPMessageFormatterUtil();
    String ct = formatter.getContentType(ctx, format, null);
    try
    {
      FileWriter fw = new FileWriter(file);
      fw.write("Content-Type: " + ct + "\n\n");
      fw.close();
      FileOutputStream fos = new FileOutputStream(file, true);
      formatter.writeTo(ctx, format, fos, true);
      //formatter.writeSwAMessage(ctx, new StringWriter(), fos, format);


      // write envelope:
      //FileWriter fw = new FileWriter("D:\\envelope.xml");
      //fw.write(ctx.getEnvelope().toString());
      //fw.close();
    }
    catch(Exception ex) { ex.printStackTrace(); }

    return ct;
  }

  public static MessageContext readMessage(File file, String ct)
  {
    if ( file == null || !file.exists() ) return null;
    MessageContext mc = new MessageContext();
    try
    {
      String repo = "F:\\Projects\\Holodeck\\test-app\\demo\\holodeck-temp\\WEB-INF";
      ConfigurationContext configContext =
        ConfigurationContextFactory.createConfigurationContextFromFileSystem(
              repo, repo + File.separator + "conf" + File.separator + "axis2.xml");
      mc.setConfigurationContext(configContext);
      FileInputStream in = new FileInputStream(file);

      SOAPEnvelope envelope = TransportUtils.createSOAPMessage(mc, in, ct);
      String startCID = getStartCID(ct);
      System.out.println("======== startCID="+ startCID);
      Attachments atts = mc.getAttachmentMap();
      atts.removeDataHandler(startCID);
      //String[] cids = atts.getAllContentIDs();
      //System.out.println("======== cid=" + cids[0]);
      //atts.removeDataHandler(cids[0]);
      mc.setEnvelope(envelope);

      // read the envelope:
      /*
      XMLInputFactory xif= XMLInputFactory.newInstance();
      XMLStreamReader reader=
        xif.createXMLStreamReader(new FileReader("D:\\envelope.xml"));
      StAXSOAPModelBuilder builder= new StAXSOAPModelBuilder(reader);
      SOAPEnvelope env = builder.getSOAPEnvelope();
      env.build();
      mc.setEnvelope(env);
      */
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return mc;
  }

  private static String getStartCID(String ct)
  {
    if ( ct == null ) return null;
    int i = ct.indexOf("start=\"");
    String temp = null;
    if ( i >= 0 ) temp = ct.substring(i + 7);
    int j = temp.indexOf("\"");
    temp = temp.substring(0, j);
    if ( temp.startsWith("<") )
         temp = temp.substring(1, temp.length() - 1);
    return temp;
  }

  private static class MessageOutputStream extends OutputStream
   {
     ObjectOutput out;
     boolean isDebug;
     MessageOutputStream(ObjectOutput out)
     {
       this.out = out;

     }

     public void close() throws IOException
     {
       // NOOP: ObjectOutput will be closed externally
     }

     public void flush() throws IOException
     {
       out.flush();
     }

     /**
      * Writes a chunk of data to the ObjectOutput
      */
     public void write(byte[] b, int off, int len) throws IOException
     {
       if (len > 0)
       {

         // Write out the length and the data chunk
         out.writeInt(len);
         out.write(b, off, len);
       }
     }

     /**
      * Writes a chunk of data to the ObjectOutput
      */
     public void write(byte[] b) throws IOException
     {
       if (b != null &&  b.length > 0)
       {

         // Write out the length and the data chunk
         out.writeInt(b.length);
         out.write(b);
       }
     }

     /**
      * Writes a single byte chunk of data to the ObjectOutput
      */
     public void write(int b) throws IOException
     {

       // Write out the length and the data chunk
       out.writeInt(1);
       out.write(b);
     }
   }

   /**
    * Provides a InputStream interface over ObjectInput.
    * MessageInputStream controls the reading of the DataBlock chunks
    */
   private static class MessageInputStream extends InputStream
    {
      ObjectInput in;
      boolean isDebug;
      int chunkAvail = 0;
      boolean isEOD = false;

      /**
       * Constructor
       * @param in
       */
      MessageInputStream(ObjectInput in)
      {
        this.in = in;

      }

      /**
       * Read a single logical byte
       */
      public int read() throws IOException
      {

        // Determine how many bytes are left in the current data chunk
        updateChunkAvail();
        int ret = 0;
        if (isEOD)
        {
          ret = -1;
        }
        else
        {
          chunkAvail--;
          ret = in.readByte();
        }

        return ret;
      }

      /**
       * Read an array of logical bytes
       */
      public int read(byte[] b, int off, int len) throws IOException
      {

        if (isEOD)
        {

          return -1;
        }
        int bytesRead = 0;
        while ((len >0 && !isEOD))
        {
          // Determine how many bytes are left in the current data chunk
          updateChunkAvail();
          if (!isEOD)
          {
            // Read the amount of bytes requested or the number of bytes available in the current chunk
            int readLength = len < chunkAvail ? len : chunkAvail;
            int br = in.read(b, off, readLength);
            if (br < 0)
            {
              throw new IOException("End of File encountered");
            }
            // Update state with the number of bytes read
            off += br;
            len -= br;
            chunkAvail -= br;
            bytesRead += br;
          }
        }

        return bytesRead;
      }

      public int read(byte[] b) throws IOException
      {
        return read(b, 0, b.length);
      }

      public void close() throws IOException
      {

        // Keep reading chunks until EOD
        if (!isEOD)
        {
          byte[] tempBuffer = new byte[4 * 1024];
          while (!isEOD)
          {
            read(tempBuffer);
          }
        }

      }

      /**
       * updateChunkAvail updates the chunkAvail field with the
       * amount of data in the chunk.
       * @throws IOException
       */
      private void updateChunkAvail() throws IOException
      {
        // If there are no more bytes in the current chunk,
        // read the size of the next datablock
        if (chunkAvail == 0 && !isEOD)
        {
          chunkAvail = in.readInt();

          if (chunkAvail <= 0)
          {

            isEOD = true;
            chunkAvail = 0;
          }
        }
      }
   }
}
