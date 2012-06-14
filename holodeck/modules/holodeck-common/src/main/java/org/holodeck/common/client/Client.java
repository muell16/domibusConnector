package org.holodeck.common.client;

import org.holodeck.common.soap.Util;

import java.io.*;
import java.util.*;
import javax.activation.*;
//import javax.xml.namespace.QName;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamReader;
import javax.persistence.*;

import org.apache.axiom.om.*;
import org.apache.axiom.om.util.UUIDGenerator;
//import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.*;
//import org.apache.axiom.attachments.Attachments;
import org.apache.axis2.*;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.*;
import org.apache.axis2.context.*;
import org.apache.axis2.client.async.*;
import org.apache.axis2.wsdl.WSDLConstants;
import org.holodeck.common.persistent.Attachment;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author Hamid Ben Malek
 */
@MappedSuperclass
public class Client
{
  @OneToMany(cascade= CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  protected List<Attachment> attachments = new ArrayList<Attachment>();

  @Lob
  @Column(name = "Msg_Context", length = 1999999999)
  protected byte[] msgContext;

  @Column(name = "Mime_File")
  protected String mimeFile = null;

  @Column(name = "Content_Type")
  protected String contentType = null;

  @Transient
  protected static ConfigurationContext configContext;
  @Transient protected double soapVersion = 1.1;
  @Transient protected String attchmentDir = "C:\\temp";
  @Transient protected MessageContext messageContext;
  @Transient protected boolean enableSWA = true;
  @Transient protected String storageFolder = null;

  public Client() {}
  public Client(String repoDir)
  {
    try
    {
      configContext =
        ConfigurationContextFactory.createConfigurationContextFromFileSystem(
              repoDir, repoDir + File.separator +
              "conf" + File.separator + "axis2.xml");
      messageContext = new MessageContext();
      messageContext.setConfigurationContext(configContext);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
  public Client(ConfigurationContext ctx)
  {
    configContext = ctx;
    messageContext = new MessageContext();
    messageContext.setConfigurationContext(configContext);
  }
  /*
  public Client(ConfigurationContext ctx, MessageContext msgCtx)
  {
    configContext = ctx;
    if ( msgCtx != null ) messageContext = msgCtx;
    else messageContext = new MessageContext();
    if ( configContext != null ) messageContext.activate(configContext);
  }
  */
  public String getMimeFile() { return mimeFile; }
  public void setMimeFile(String mimeFile) { this.mimeFile = mimeFile; }

  public boolean isEnableSWA() { return enableSWA; }
  public void setEnableSWA(boolean enableSWA)
  {
    this.enableSWA = enableSWA;
  }

  public void setConfigurationContext(ConfigurationContext confCtx)
  {
    configContext = confCtx;
    if ( messageContext != null )
    {
      messageContext.setConfigurationContext(confCtx);
      messageContext.activate(configContext);
    }
  }
  public ConfigurationContext getConfigurationContext() { return configContext; }

  public double getSoapVersion() { return soapVersion; }
  public void setSoapVersion(double soapVersion)
  {
    this.soapVersion = soapVersion;
  }

  public String getAttchmentDir() { return attchmentDir; }
  public void setAttchmentDir(String attchmentDir)
  {
    this.attchmentDir = attchmentDir;
  }

  public MessageContext getMessageContext()
  {
    if ( messageContext != null )
    {
      if ( messageContext.getEnvelope() == null )
           setEmptyEnvelope(messageContext, soapVersion);
      return messageContext;
    }
    if ( messageContext == null )
    {
      if ( msgContext != null && msgContext.length > 0 )
           messageContext = (MessageContext)deserialize(msgContext);
      else messageContext = new MessageContext();
      populateAttachments(attachments, messageContext);
      if ( mimeFile != null && !mimeFile.trim().equals("") )
      {
        File mimeF = new File(mimeFile);
        if ( mimeF.exists() )
             Util.readMessage(messageContext, mimeF, contentType);
      }
      if ( messageContext.getEnvelope() == null )
           setEmptyEnvelope(messageContext, soapVersion);
      if ( configContext != null )
      {
        messageContext.setConfigurationContext(configContext);
        messageContext.activate(configContext);
      }
    }
    try
    {
      if ( messageContext.getEnvelope() == null )
           messageContext.setEnvelope(createEnvelope(soapVersion));
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return messageContext;
  }

  public void setMessageContext(MessageContext ctx)
  {
    if ( ctx == null ) return;
    this.messageContext = ctx;
    if ( configContext != null )
    {
      messageContext.setConfigurationContext(configContext);
      messageContext.activate(configContext);
    }
    msgContext = serialize(messageContext);
    // populate attachments object from ctx ...
    //persistAttachments(ctx);
    if ( attachments != null && attachments.size() > 0 ) return;
    else if ( mimeFile != null )
             contentType = Util.writeMessage(ctx, new File(mimeFile) );
  }

  public void setMessageContext(MessageContext ctx, boolean persist)
  {
    if ( ctx == null ) return;
    if ( persist ) setMessageContext(ctx);
    else
    {
      this.messageContext = ctx;
      if ( configContext != null )
      {
        messageContext.setConfigurationContext(configContext);
        messageContext.activate(configContext);
      }
      msgContext = serialize(messageContext);
    }
  }

  public void setEnvelope(SOAPEnvelope env)
  {
    try
    {
      if ( env != null )
      {
        getMessageContext().setEnvelope(env);
        msgContext = serialize(messageContext);
      }
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public SOAPEnvelope getEnvelope()
  {
    try
    {
      if ( getMessageContext().getEnvelope() == null )
           messageContext.setEnvelope(createEnvelope(soapVersion));
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return getMessageContext().getEnvelope();
  }

  public SOAPBody getBody()
  {
    return getEnvelope().getBody();
  }

  public void addToBody(OMElement payload)
  {
    if ( payload != null )
    {
      getBody().addChild(payload);
      msgContext = serialize(messageContext);
    }
  }

  public OMElement addToBody(String xmlDocFile)
  {
    File payload = new File(xmlDocFile);
    if ( !payload.exists() ) return null;
    OMElement pLoad = Util.rootElement(payload);
    addToBody(pLoad);
    return pLoad;
  }

  public String addFileAttachment(String file)
  {
    String cid = UUIDGenerator.getUUID();
    addFileAttachment(file, cid);
    return cid;
  }

  public MessageContext inOut(ServiceClient sender)
  {
    MessageContext response = null;
    try
    {
      getMessageContext().setOptions(sender.getOptions());
      OperationClient mepClient =
            sender.createClient(ServiceClient.ANON_OUT_IN_OP);
      mepClient.addMessageContext(getMessageContext());
      mepClient.execute(true);
      response =
         mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return response;
  }
  public MessageContext inOut(String toURL, String action, String[] modules)
  {
    MessageContext response = null;
    try
    {
      ServiceClient sender = createServiceClient(toURL, action, modules);
      getMessageContext().setOptions(sender.getOptions());
      OperationClient mepClient =
            sender.createClient(ServiceClient.ANON_OUT_IN_OP);
      mepClient.addMessageContext(getMessageContext());
      mepClient.execute(true);
      response =
         mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return response;
  }

  public void inOut(String toURL, String action, String[] modules,
                    AxisCallback callback)
  {
    //MessageContext response = null;
    try
    {
      ServiceClient sender = createServiceClient(toURL, action, modules);
      getMessageContext().setOptions(sender.getOptions());
      OperationClient mepClient =
            sender.createClient(ServiceClient.ANON_OUT_IN_OP);
      mepClient.addMessageContext(getMessageContext());
      if ( callback != null )
      {
        mepClient.setCallback(callback);
        mepClient.execute(false);
      }
      else mepClient.execute(true);
      //response =
      //   mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    //return response;
  }

  public void inOnly(String toURL, String action, String[] modules)
  {
    try
    {
      ServiceClient sender = createServiceClient(toURL, action, modules);
      getMessageContext().setOptions(sender.getOptions());
      OperationClient mepClient =
            sender.createClient(ServiceClient.ANON_OUT_ONLY_OP);
      mepClient.addMessageContext(getMessageContext());
      mepClient.execute(false);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public void terminate()
  {
    try { configContext.terminate(); }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  protected ServiceClient createServiceClient(String toURL,
                                            String action, String[] modules)
  {
    EndpointReference targetEPR = new EndpointReference(toURL);
    Options options = new Options();
    options.setTo(targetEPR);
    if ( enableSWA )
    {
      options.setProperty(Constants.Configuration.ENABLE_SWA,
                          Constants.VALUE_TRUE);
      options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS,
                          Constants.VALUE_TRUE);
      options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD, "4000");
      //options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,
      //                  attchmentDir);
      String attachDir = getAttachmentDirectory();
      //System.out.println("[Client: attachDir=" + attachDir);
      if ( attachDir != null )
           options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,
                        attchmentDir);
    }
    if ( soapVersion <= 1.1 )
         options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
    else options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
    options.setTimeOutInMilliSeconds(200000);
    options.setTo(targetEPR);
    options.setAction(action);
    ServiceClient sender = null;
    try
    {
      sender = new ServiceClient(configContext, null);
      sender.setOptions(options);
      if ( modules != null && modules.length > 0 )
      {
        for (int i = 0; i < modules.length; i++)
             //sender.engageModule(new QName(modules[i]));
             sender.engageModule(modules[i]);
      }
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return sender;
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
  /*
  public static OMElement rootElement(File xmlFile)
  {
    if ( xmlFile == null || !xmlFile.exists() ) return null;
    try
    {
      FileInputStream fis= new FileInputStream(xmlFile);
      XMLInputFactory xif= XMLInputFactory.newInstance();
      XMLStreamReader reader= xif.createXMLStreamReader(fis);
      StAXOMBuilder builder= new StAXOMBuilder(reader);
      return builder.getDocumentElement();
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }
  */
  public String addFileAttachment(String file, String cid)
  {
    if ( file == null || file.trim().equals("") ) return cid;
    File data = new File(file);
    if ( !data.exists() ) return cid;
    if ( cid == null || cid.trim().equals("") )
         cid = UUIDGenerator.getUUID();

    FileDataSource fileDataSource = new FileDataSource(data);
    fileDataSource.setFileTypeMap(Util.getMimeTypes());
    DataHandler dataHandler = new DataHandler(fileDataSource);
    Attachment part = new Attachment(file, cid);
    attachments.add(part);
    getMessageContext().addAttachment(cid, dataHandler);
    return cid;
  }

  public void addAttachment(Attachment att)
  {
    if (att == null) return;
    attachments.add(att);
    DataHandler dh = att.getDataHandler(getStorageFolder());
    if ( dh != null ) getMessageContext().addAttachment(dh);
  }

  public List<Attachment> getAttachments() { return attachments; }
  public void setAttachments(List<Attachment> attachments)
  {
    this.attachments = attachments;
  }

 /**
  * This method needs to be called first before calling
  * getMessageContext() when the message is being resurrected from
  * a persistent media
  */
  public void setStorageFolder(String storageFolder)
  {
    this.storageFolder = storageFolder;
  }
  public String getStorageFolder() { return storageFolder; }

  protected byte[] serialize(Object obj)
  {
     if (obj == null) return null;
     try
     {
       ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
       ObjectOutput out = new ObjectOutputStream(bos) ;
       out.writeObject(obj);
       out.close();
       return bos.toByteArray();
     }
     catch(Exception ex) { ex.printStackTrace(); }
     return null;
  }

  public void serializeContext()
  {
    msgContext = serialize(messageContext);
  }

  protected Object deserialize(byte[] data)
  {
     if ( data == null  || data.length <= 0 ) return null;
     Object result = null;
     ByteArrayInputStream stream = new ByteArrayInputStream(data);
     try
     {
       ObjectInputStream is = new ObjectInputStream(stream);
       result = is.readObject();
     }
     catch(Exception ex) { ex.printStackTrace(); }
     return result;
  }

  private void setEmptyEnvelope(MessageContext msgCtx, double soapVer)
  {
    if ( msgCtx == null ) return;
    try
    {
      if ( msgCtx.getEnvelope() == null )
           msgCtx.setEnvelope(createEnvelope(soapVer));
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  private String getAttachmentDirectory()
  {
    if ( configContext == null ) return null;
    return (String)configContext.getAxisConfiguration()
                                .getParameter(org.apache.axis2.Constants.Configuration.ATTACHMENT_TEMP_DIR).getValue();
  }

  private void populateAttachments(List<Attachment> atts, MessageContext ctx)
  {
    if ( atts == null || atts.size() <= 0 || ctx == null ) return;
    for (Attachment att : atts)
    {
      String cid = att.getContentID();
      if ( cid == null || cid.trim().equals("") )
           cid = UUIDGenerator.getUUID();
      DataHandler dh = att.getDataHandler(getStorageFolder());
      if ( dh != null ) getMessageContext().addAttachment(cid, dh);
      /*
      else if ( getStorageFolder() != null )
      {
        String fullPath = getStorageFolder() + File.separator +
                          att.getFilePath();
        File file = new File(fullPath);
        if ( file.exists() )
        {
          FileDataSource fileDataSource = new FileDataSource(file);
          fileDataSource.setFileTypeMap(Util.getMimeTypes());
          DataHandler dataHandler = new DataHandler(fileDataSource);
          getMessageContext().addAttachment(cid, dataHandler);
        }
      } */
    }
  }
  /*
  public void persistAttachments(MessageContext context)
  {
    if ( storageFolder == null ) return;
    try
    {
      Attachments at = context.getAttachmentMap();
      if (at == null) return;
      Set cids = at.getContentIDSet();
      //String soapPartCID = at.getSOAPPartContentID();
      if ( cids == null || cids.size() <= 1 ) return;
      DataHandler dh = null;
      String folder = UUIDGenerator.getUUID();
      if ( folder.indexOf(":") >= 0 )
           folder = folder.replaceAll(":", "-");
      for (Object id : cids)
      {
        String cid = (String)id;
        //if ( !cid.equalsIgnoreCase(soapPartCID) )
        //{
          String name = cid;
          if ( cid.indexOf(":") >= 0 ) name = name.replaceAll(":", "-");
          dh = at.getDataHandler(cid);
          String extension = Util.getFileExtension(dh.getContentType());
          String path = storageFolder + File.separator +
                        folder + File.separator + name + "." + extension;
          File att = new File(path);
          Util.writeDataHandlerToFile(dh, att);
          Attachment part = new Attachment(folder + File.separator +
                                           name + "." + extension);
          part.setContentID(cid);
          part.setContentType(dh.getContentType());
          attachments.add(part);
        //}
      }
    }
    catch(Exception ex)  { ex.printStackTrace(); }
  }
  */
}