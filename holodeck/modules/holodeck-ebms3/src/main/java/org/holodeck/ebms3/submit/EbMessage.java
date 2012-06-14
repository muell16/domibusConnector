package org.holodeck.ebms3.submit;

import org.holodeck.common.client.Client;
import org.holodeck.common.soap.Util;

import org.holodeck.ebms3.module.Configuration;
//import org.holodeck.ebms3.config.*;
//import org.holodeck.ebms3.pmodes.*;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.EbUtil;
import org.holodeck.ebms3.config.Leg;

import org.apache.axis2.context.*;
//import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
//import java.util.*;
import java.io.File;

/**
 *  This class is intended to be used only within the same class
 *  loader of the ebms3 module.
 *
 * @author Hamid Ben Malek
 */
@MappedSuperclass
public class EbMessage extends Client
{
  public EbMessage() { super(); }
  public EbMessage(String repoDir)
  {
    super(repoDir);
  }
  /*
  public EbMessage(MessageContext msgCtx)
  {
    super(Constants.configContext, msgCtx);
  }  */
  public EbMessage(ConfigurationContext ctx) { super(ctx); }
  /*
  public EbMessage(ConfigurationContext confCtx, MessageContext msgCtx)
  {
    super( confCtx, msgCtx);
  } */

  public EbMessage(MsgInfoSet mis)
  {
    super(Constants.configContext);
    setMsgInfoSet(mis);
  }

  public EbMessage(File folder, MsgInfoSet mis)
  {
    this(mis);
    String bodyPayload = mis.getBodyPayload();
    if ( bodyPayload != null && !bodyPayload.trim().equals("") )
    {
      String bodyFile =
            folder.getAbsolutePath() + File.separator + bodyPayload.trim();
      addToBody(bodyFile);
    }

    File[] files = folder.listFiles();
    if ( files == null || files.length == 0 ) return;
    for ( File file : files)
    {
      if ( !file.isDirectory() &&
           !file.getName().startsWith("metadata.xml") &&
           !file.getName().equalsIgnoreCase(bodyPayload) &&
           !file.getName().equalsIgnoreCase("metadata.xml") &&
           !file.getName().equalsIgnoreCase("metadata.xml.processed") &&
           !file.getName().equalsIgnoreCase("desktop.ini")
         )
      {
        String cid = mis.getCID(file.getName());
        String _cid;
        if ( mis.isCompressed(file.getName()) )
        {
          Util.doCompressFile(file.getAbsolutePath());
          _cid = addFileAttachment(file.getAbsolutePath() + ".gz", cid);
        }
        else _cid = addFileAttachment(file.getAbsolutePath(), cid);
        if ( !_cid.equals(cid) ) mis.setCID(_cid, file.getName());
      }
    }
  }

  @Transient  
  public void setMsgInfoSet(MsgInfoSet mis)
  {
    if ( mis == null ) return;

    String ver = Configuration.getSoapVersion(mis);
//    Leg leg = getLeg(mis, mis.getLegNumber());
//    String ver = leg.getSoapVersion();
    if ( ver != null && !ver.trim().equals("") )
         this.soapVersion = Double.parseDouble(ver);

    String toURL = Configuration.getAddress(mis);
    String action = Configuration.getSoapAction(mis);
//    String toURL = leg.getAddress();
//    String action = leg.getSoapAction();
    if ( action == null || action.trim().equals("") )
         action = Configuration.getWsaAction(mis);
         //action = leg.getWsaAction();
    if ( action == null || action.trim().equals("") )
         action = "ebms3";
    EndpointReference targetEPR = new EndpointReference(toURL);
    Options options = new Options();
    options.setTo(targetEPR);
    options.setAction(action);
    getMessageContext().setOptions(options);
    //if ( leg.getWsaAction() != null )
    getMessageContext().setWSAAction(action);
    getMessageContext().setProperty("MESSAGE_INFO_SET", mis);

    String rel = Configuration.getReliability(mis);
    if ( rel != null ) getMessageContext().setProperty("QUALITY", rel);
//    if ( leg.getReliability() != null )
//         getMessageContext().setProperty("QUALITY", leg.getReliability());
    String security = Configuration.getSecurity(mis);
    if ( security != null )
        getMessageContext().setProperty("SECURITY", security);
  }

  public MessageContext inOut(MsgInfoSet mis)
  {
    String[] modules = Constants.engagedModules;

    String toURL = Configuration.getAddress(mis) ;
    String action = Configuration.getSoapAction(mis) ;
//    Leg leg = getLeg(mis, mis.getLegNumber());
//    String toURL = leg.getAddress();
//    String action = leg.getSoapAction();
    if ( action == null || action.trim().equals("") )
         action = Configuration.getWsaAction(mis);
         //action = leg.getWsaAction();
    getMessageContext().setProperty("MESSAGE_INFO_SET", mis);

    String rel = Configuration.getReliability(mis);
    if ( rel != null ) getMessageContext().setProperty("QUALITY", rel);
//    if ( leg.getReliability() != null )
//         getMessageContext().setProperty("QUALITY", leg.getReliability());
    return super.inOut(toURL, action, modules);
  }

  public void inOut(MsgInfoSet mis, AxisCallback callback)
  {
    String[] modules = Constants.engagedModules;

    String toURL = Configuration.getAddress(mis) ;
    String action = Configuration.getSoapAction(mis) ;
//    Leg leg = getLeg(mis, mis.getLegNumber());
//    String toURL = leg.getAddress();
//    String action = leg.getSoapAction();
    if ( action == null || action.trim().equals("") )
         action = Configuration.getWsaAction(mis);
         //action = leg.getWsaAction();
    getMessageContext().setProperty("MESSAGE_INFO_SET", mis);

    String rel = Configuration.getReliability(mis);
    if ( rel != null ) getMessageContext().setProperty("QUALITY", rel);
//    if ( leg.getReliability() != null )
//         getMessageContext().setProperty("QUALITY", leg.getReliability());

    super.inOut(toURL, action, modules, callback);
  }

  public void inOnly(MsgInfoSet mis)
  {
    String[] modules = Constants.engagedModules;

    String toURL = Configuration.getAddress(mis) ;
    String action = Configuration.getSoapAction(mis) ;
    if ( action == null || action.trim().equals("") )
         action = Configuration.getWsaAction(mis);

    getMessageContext().setProperty("MESSAGE_INFO_SET", mis);

    String rel = Configuration.getReliability(mis);
    if ( rel != null ) getMessageContext().setProperty("QUALITY", rel);

    super.inOnly(toURL, action, modules);
  }

  public MessageContext send(MsgInfoSet mis, AxisCallback callback)
  {
    if ( mis == null ) return null;
    String mep = Configuration.getMep(mis);
    int ln = mis.getLegNumber();
    Leg leg = Configuration.getLeg(mis);
    if ( mep.equalsIgnoreCase(Constants.ONE_WAY_PUSH) ||
         mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PUSH) )
    {
      if ( leg != null && leg.getReceiptReply() != null &&
           leg.getReceiptReply().equalsIgnoreCase("Response") )
           return inOut(mis);
      else if ( leg != null && leg.getReliability() != null )
           return inOut(mis);
      else inOnly(mis);
      return null;
    }
    else if ( mep.equalsIgnoreCase(Constants.ONE_WAY_PULL) )
    {
      if ( ln == 1 )
      {
        if ( callback == null ) return inOut(mis);
        else { inOut(mis, callback); return null; }
      }
      else if ( ln == 2 ) { inOnly(mis); return null; }
    }
    else if ( mep.equalsIgnoreCase(Constants.TWO_WAY_SYNC) )
    {
      if ( ln == 1 )
      {
        if ( callback == null ) return inOut(mis);
        else { inOut(mis, callback); return null; }
      }
      else if ( ln == 2 ) { inOnly(mis); return null; }
    }
    else if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PULL) )
    {
      if ( ln == 1 )
      {
        if ( leg != null && leg.getReceiptReply() != null &&
             leg.getReceiptReply().equalsIgnoreCase("Response") )
             return inOut(mis);
        else if ( leg != null && leg.getReliability() != null )
                  return inOut(mis);
        else inOnly(mis);
        return null;
      }
      else if ( ln == 2 )
      {
        if ( callback == null ) return inOut(mis);
        else { inOut(mis, callback); return null; }
      }
      else if ( ln == 3 ) { inOnly(mis); return null; }
    }
    else if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_PUSH) )
    {
      if ( ln == 1 )
      {
        if ( callback == null ) return inOut(mis);
        else { inOut(mis, callback); return null; }
      }
      else if ( ln == 2 ) { inOnly(mis); return null; }
      else if ( ln == 3 )
      {
        if ( leg != null && leg.getReceiptReply() != null &&
           leg.getReceiptReply().equalsIgnoreCase("Response") )
           return inOut(mis);
        else if ( leg != null && leg.getReliability() != null )
                  return inOut(mis);
        else inOnly(mis);
        return null; 
      }
    }
    else if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_Pull) )
    {
      if ( ln == 1 || ln == 3 )
      {
        if ( callback == null ) return inOut(mis);
        else { inOut(mis, callback); return null; }
      }
      else if ( ln == 2 ) { inOnly(mis); return null; }
      else if ( ln == 4 ) { inOnly(mis); return null; }
    }
    return null;
  }
  /*
  private Leg getLeg(MsgInfoSet metadata, int legNumber)
  {
    if ( metadata == null ) return null;
    Map<String, PMode> pmodesMap = Constants.pmodesMap;
    PMode pmode = pmodesMap.get(metadata.getPmode());
    if ( pmode == null ) return null;
    return pmode.getLeg(legNumber, null);
  }
  */
  private int getLegNumber(String mep, String firstLegAddress)
  {
    if ( mep.equalsIgnoreCase(Constants.ONE_WAY_PUSH) ) return 1;
    if ( mep.equalsIgnoreCase(Constants.ONE_WAY_PULL) ) return 2;
    if ( mep.equalsIgnoreCase(Constants.TWO_WAY_SYNC) )
    {
      if ( EbUtil.isLocal(firstLegAddress, configContext) ) return 2;
      else return 1;
    }
    if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PUSH) )
    {
      if ( EbUtil.isLocal(firstLegAddress, configContext) ) return 2;
      else return 1;
    }
    if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PULL) )
    {
      if ( EbUtil.isLocal(firstLegAddress, configContext) ) return 3;
      else return 1;
    }
    if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_PUSH) )
    {
      if ( EbUtil.isLocal(firstLegAddress, configContext) ) return 2;
      else return 3;
    }
    if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_Pull) )
    {
      if ( EbUtil.isLocal(firstLegAddress, configContext) ) return 2;
      else return 4;
    }
    return 1;
  }
  /*
  private Map<String, PMode> getPModesMap()
  {
    if ( getConfigurationContext() == null ) return null;
    AxisConfiguration config =
          getConfigurationContext().getAxisConfiguration();
    if ( config == null ) System.out.println("axis configuration is null");
    return (Map<String, PMode>)config.getParameter(Constants.PMODES_MAP).getValue();
  }

  private Binding getBinding(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    Map<String, PMode> pmodesMap = getPModesMap();
    PMode pmode = pmodesMap.get(metadata.getPmode());
    return pmode.getBinding();
  }
  */
}