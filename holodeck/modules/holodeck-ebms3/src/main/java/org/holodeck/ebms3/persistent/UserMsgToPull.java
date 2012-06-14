package org.holodeck.ebms3.persistent;

import javax.persistence.*;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;

import org.hibernate.annotations.GenericGenerator;
import java.io.*;

import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.submit.EbMessage;
import org.holodeck.common.persistent.Attachment;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.module.Constants;
//import org.holodeck.ebms3.module.EbUtil;
//import org.holodeck.ebms3.pmodes.Leg;
import org.holodeck.ebms3.module.Configuration;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ConfigurationContext;


/**
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "UserMsg_Pull")
public class UserMsgToPull extends EbMessage implements java.io.Serializable
{
  private static final long serialVersionUID = -449053446969771499L;  

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "ID")
  protected String id = null;

  @Lob
  @Column(name = "Msg_InfoSet", length = 1999999999)
  protected byte[] msgInfoSet;

  @Column(name = "PMode")
  protected String pmode;

  @Column(name = "MPC")
  protected String mpc = null;

  @Column(name = "Time_In_Millis")
  protected long timeInMillis = 0;

  @Column(name = "Pulled")
  protected boolean pulled = false;

  @Transient
  protected MsgInfoSet bean;

  public UserMsgToPull()
  {
    timeInMillis = System.currentTimeMillis();
    setStorageFolder(Constants.getSubmitFolder());
  }
  public UserMsgToPull(ConfigurationContext configurationCtx)
  {
    super(configurationCtx);
    timeInMillis = System.currentTimeMillis();
    setStorageFolder(Constants.getSubmitFolder());
  }
  public UserMsgToPull(ConfigurationContext configurationCtx,
                       double soapVersion)
  {
    super(configurationCtx);
    setSoapVersion(soapVersion);
    timeInMillis = System.currentTimeMillis();
    setStorageFolder(Constants.getSubmitFolder());
  }
  public UserMsgToPull(MessageContext context, MsgInfoSet mis)
  {
    setStorageFolder(Constants.getSubmitFolder());
    setMessageContext(context);
    //setMsgInfoSet(mis);
    setMsgInfoSetBean(mis);
    timeInMillis = System.currentTimeMillis();
    serializeContext();
  }
  public UserMsgToPull(File folder, MsgInfoSet mis)
  {
    super(folder, mis);
    //setMsgInfoSet(mis);
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

  public String getPmode() { return pmode; }
  public void setPmode(String pmode) { this.pmode = pmode; }

  public String getMpc() { return mpc; }
  public void setMpc(String mpc) { this.mpc = mpc; }

  public long getTimeInMillis() { return timeInMillis; }
  public void setTimeInMillis(long timeInMillis)
  {
    this.timeInMillis = timeInMillis;
  }

  public boolean isPulled() { return pulled; }
  public void setPulled(boolean pulled) { this.pulled = pulled; }

  public void setMsgInfoSetBean(MsgInfoSet mis)
  {
    if ( mis == null ) return;
    this.pmode = mis.getPmode();
    this.bean = mis;
    msgInfoSet = serialize(mis);
    this.mpc = Configuration.getMpc(mis.getPmode(), mis.getLegNumber());
//    Leg leg = EbUtil.getLeg(mis, Constants.pmodesMap);
//    if ( leg != null ) this.mpc = leg.getMpc();
  }
  public MsgInfoSet getMsgInfoSetBean()
  {
    if ( bean != null ) return bean;
    if ( msgInfoSet == null || msgInfoSet.length == 0 ) return null;
    bean = (MsgInfoSet)deserialize(msgInfoSet);
    return bean;
  }

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
  }
}