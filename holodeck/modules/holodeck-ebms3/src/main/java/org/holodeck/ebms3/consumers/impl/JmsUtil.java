package org.holodeck.ebms3.consumers.impl;

import java.io.*;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Destination;

import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.holodeck.common.soap.Util;
import org.apache.axis2.context.MessageContext;
import org.holodeck.ebms3.module.Constants;

/**
 * @author Hamid Ben Malek
 */
public class JmsUtil
{
  protected Connection connection;
  protected Session session;
  protected Destination destination;
  protected Context initial = null;
  protected ConnectionFactory cf = null;

  protected String jndiConnectionFactory;
  protected String destinationName;

  protected boolean closed = false;
  protected boolean initialized = false;

  public JmsUtil() {}

  public JmsUtil(String jndiConnFact, String dest)
  {
    this.jndiConnectionFactory = jndiConnFact;
    this.destinationName = dest;
    init(jndiConnFact, dest);
  }

  public void init(String jndiConnFact, String dest)
  {
    try
    {
      if ( initial == null ) initial = new InitialContext();
      if ( cf == null )
           cf = (ConnectionFactory)initial.lookup(jndiConnFact);
      if ( connection == null )
      {
        connection = cf.createConnection();
        connection.start();
      }

      if ( session == null )
           session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      destination = (Destination)initial.lookup(dest);
      initialized = true;
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public void close()
  {
    try
    {
      if ( connection != null )
      {
        connection.close();
        connection = null;
      }
      if ( session != null )
      {
        session.close();
        session = null;
      }
      closed = true;
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public void publish(MessageContext msgCtx, Map<String, String> headers)
              throws Exception
  {
    if ( jndiConnectionFactory == null || destinationName == null )
    {
      System.out.println("Cannot call pulish(msgCtx) method while either jndiConnectionFactory " +
                         "or destinationName is not set");
      throw new Exception("jndiConnectionFactory and/or destinationName is not set");
    }
    if ( closed || !initialized ) init(jndiConnectionFactory, destinationName);
    MessageProducer publisher = session.createProducer(destination);
    publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
    BytesMessage msg = createMessage(msgCtx);
    if ( headers != null )
    {
      for (String key : headers.keySet() )
      {
        msg.setStringProperty( key, headers.get(key) );
      }
    }
    publisher.send(msg);
    System.out.println("Message was sent to destination " + destinationName);
  }

  public MessageConsumer createMessageConsumer(boolean durableDestination, String consumerId)
  {
    MessageConsumer consumer = null;
    if ( jndiConnectionFactory == null || destinationName == null )
    {
      System.out.println("Cannot call pulish(msgCtx) method while either jndiConnectionFactory " +
                         "or destinationName is not set");
      return consumer;
    }
    if ( closed || !initialized ) init(jndiConnectionFactory, destinationName);
    try
    {
      if ( durableDestination )
           consumer = session.createDurableSubscriber((Topic)destination, consumerId);
      else consumer = session.createConsumer(destination);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return consumer;
  }

  public MessageProducer createProducer()
  {
    if ( jndiConnectionFactory == null || destinationName == null )
    {
      System.out.println("Cannot call pulish(msgCtx) method while either jndiConnectionFactory " +
                         "or destinationName is not set");
      return null;
    }
    if ( closed || !initialized ) init(jndiConnectionFactory, destinationName);
    MessageProducer publisher = null;
    try
    {
      publisher = session.createProducer(destination);
      publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return publisher;
  }

  public BytesMessage createMessage(MessageContext msgCtx)
  {
    try
    {
      String fileName = getSaveLocation() + File.separator +
                        System.currentTimeMillis() + ".mime";
      String contentType = Util.writeMessage(msgCtx, new File(fileName));

      byte[] contents = getBytesFromFile(new File(fileName));
      BytesMessage msg = session.createBytesMessage();
      msg.writeBytes(contents);
      msg.setStringProperty("Content-Type", contentType);
      msg.setStringProperty("SOAPJMS_contentType", contentType);
      msg.setStringProperty("contentType", contentType);
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

  public static MessageContext createMsgCtx(BytesMessage message)
  {
    if ( message == null ) return null;
    try
    {
      String ct = message.getStringProperty("Content-Type");
      if ( ct == null || ct.trim().equals("") )
           ct = message.getStringProperty("contentType");
      InputStream in = Util.getInputStream(message);
      MessageContext mc = new MessageContext();
      mc.setConfigurationContext(Constants.configContext);
      return Util.readMessage(mc, in, ct);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }
}