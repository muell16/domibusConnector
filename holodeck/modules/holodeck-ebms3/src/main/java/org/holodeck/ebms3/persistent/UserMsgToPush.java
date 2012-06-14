package org.holodeck.ebms3.persistent;

import javax.persistence.*;
//import javax.activation.FileDataSource;
//import javax.activation.DataHandler;

import org.hibernate.annotations.GenericGenerator;

//import org.holodeck.common.soap.Util;
//import org.holodeck.common.persistent.Attachment;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.submit.EbMessage;

import org.holodeck.ebms3.module.Configuration;
//import org.holodeck.ebms3.pmodes.Leg;
//import org.holodeck.ebms3.pmodes.Binding;
//import org.holodeck.ebms3.module.EbUtil;
import org.holodeck.ebms3.module.Constants;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ConfigurationContext;

import java.io.*;

/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "UserMsg_Push")
public class UserMsgToPush extends EbMessage implements java.io.Serializable
{
  private static final long serialVersionUID = 1061327101374536658L;  

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "ID")
  protected String id = null;

  @Lob
  @Column(name = "Msg_InfoSet", length = 1999999999)
  protected byte[] msgInfoSet;

  @Column(name = "toURL")
  protected String toURL;

  @Column(name = "MEP")
  protected String mep;

  @Column(name = "PMode")
  protected String pmode;

  @Column(name = "Callback_Class")
  protected String callbackClass;

  @Column(name = "Time_In_Millis")
  protected long timeInMillis = 0;

  @Column(name = "LegNumber")
  protected int legNumber = 1;

  @Column(name = "Pushed")
  protected boolean pushed = false;

  @Transient
  protected MsgInfoSet bean;

  public UserMsgToPush()
  {
    timeInMillis = System.currentTimeMillis();
    setStorageFolder(Constants.getSubmitFolder());
  }
  public UserMsgToPush(ConfigurationContext configurationCtx)
  {
    super(configurationCtx);
    timeInMillis = System.currentTimeMillis();
    setStorageFolder(Constants.getSubmitFolder());
  }
  public UserMsgToPush(ConfigurationContext configurationCtx,
                       double soapVersion)
  {
    super(configurationCtx);
    setSoapVersion(soapVersion);
    timeInMillis = System.currentTimeMillis();
    setStorageFolder(Constants.getSubmitFolder());
  }
  public UserMsgToPush(MessageContext context, MsgInfoSet mis)
  {
    setStorageFolder(Constants.getSubmitFolder());
    setMessageContext(context);
    this.callbackClass = mis.getCallbackClass();
    setMsgInfoSet(mis);
    setMsgInfoSetBean(mis);
    timeInMillis = System.currentTimeMillis();

    serializeContext();
  }
  public UserMsgToPush(File folder, MsgInfoSet mis)
  {
    super(folder, mis);
    this.callbackClass = mis.getCallbackClass();
    setMsgInfoSet(mis);
    setMsgInfoSetBean(mis);
    timeInMillis = System.currentTimeMillis();
    setStorageFolder(Constants.getSubmitFolder());
    serializeContext();
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public byte[] getMsgInfoSet() { return msgInfoSet; }
  public void setMsgInfoSet(byte[] msgInfoSet)
  {
    this.msgInfoSet = msgInfoSet;
  }

  public String getToURL() { return toURL; }
  public void setToURL(String toURL) { this.toURL = toURL; }

  public String getMep() { return mep; }
  public void setMep(String mep) { this.mep = mep; }

  public String getPmode() { return pmode; }
  public void setPmode(String pmode) { this.pmode = pmode; }

  public void setCallbackClass(String callbackClass)
  {
    this.callbackClass = callbackClass;
  }
  public String getCallbackClass() { return callbackClass; }

  public long getTimeInMillis() { return timeInMillis; }
  public void setTimeInMillis(long timeInMillis)
  {
    this.timeInMillis = timeInMillis;
  }

  public int getLegNumber() { return legNumber; }
  public void setLegNumber(int legNumber) { this.legNumber = legNumber; }

  public boolean isPushed() { return pushed; }
  public void setPushed(boolean pushed) { this.pushed = pushed; }

  public void setMsgInfoSetBean(MsgInfoSet mis)
  {
    if ( mis == null ) return;
    this.pmode = mis.getPmode();
    this.bean = mis;
    this.legNumber = mis.getLegNumber();
    msgInfoSet = serialize(mis);

    this.toURL = Configuration.getAddress(mis.getPmode(), mis.getLegNumber());
//    Leg leg = EbUtil.getLeg(mis, Constants.pmodesMap);
//    if ( leg != null ) this.toURL = leg.getAddress();

    this.mep = Configuration.getMep(mis.getPmode());
//    Binding binding = EbUtil.getBinding(mis, Constants.pmodesMap);
//    if ( binding != null ) this.mep = binding.getMep();
  }
  public MsgInfoSet getMsgInfoSetBean()
  {
    if ( bean != null ) return bean;
    if ( msgInfoSet == null || msgInfoSet.length == 0 ) return null;
    bean = (MsgInfoSet)deserialize(msgInfoSet);
    return bean;
  }
  public MessageContext getMessageContext()
  {
    MessageContext ctx = super.getMessageContext();
    ctx.setProperty("attachments", attachments);
    return ctx;
  }
  /*
  public void addAttachmentFromFile(Attachment part, MessageContext context)
  {
    if ( part == null || context == null ) return;
    if ( part.getFilePath() == null || part.getFilePath().trim().equals("") )
         return;
    File att =
     new File(Constants.getSubmitFolder() + File.separator + part.getFilePath());
    if ( !att.exists() ) return;
    FileDataSource fileDataSource = new FileDataSource(att);
    fileDataSource.setFileTypeMap(Util.getMimeTypes());
    DataHandler dataHandler = new DataHandler(fileDataSource);
    if ( part.getContentID() != null )
         context.addAttachment(part.getContentID(), dataHandler);
    else context.addAttachment(dataHandler);
  } */
}