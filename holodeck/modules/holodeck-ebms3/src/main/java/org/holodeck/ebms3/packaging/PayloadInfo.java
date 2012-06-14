package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;

import org.apache.axis2.context.MessageContext;

/**
 * @author Hamid Ben Malek
 */
public class PayloadInfo extends Element
{
  private static final long serialVersionUID = -4645523945415810689L;  

  public PayloadInfo()
  {
    super(Constants.PAYLOAD_INFO, Constants.NS, Constants.PREFIX);
  }

  public PayloadInfo(String partID)
  {
    this();
    addPartInfo(partID, null, null, null, null);
  }

  public PayloadInfo(String partID, String schemaLocation,
                      String description)
  {
    this();
    addPartInfo(partID, schemaLocation, description, null, null);
  }

  public PayloadInfo(String[] partIDs)
  {
    this();
    if ( partIDs == null || partIDs.length == 0 ) return;
    for (String cid : partIDs) addPartInfo(cid, null, null, null, null);
  }

  public PayloadInfo(String[] partIDs, String soapPartCid)
  {
    this();
    if ( partIDs == null || partIDs.length == 0 ) return;
    for (String cid : partIDs)
    {
      if ( !cid.equals(soapPartCid) )
           addPartInfo(cid, null, null, null, null);
    }
  }

  public PayloadInfo(String[] partIDs, String soapPartCid, boolean hasBody)
  {
    this();
    if ( partIDs == null || partIDs.length == 0 ) return;
    if ( hasBody ) addPartInfo(null, null, null, null, null); 
    for (String cid : partIDs)
    {
      if ( !cid.equals(soapPartCid) )
           addPartInfo(cid, null, null, null, null);
    }
  }

  public void addPartInfo(String partID, String schemaLocation,
                          String description, String[] propertyNames,
                          String[] propertyValues)
  {
    Element partInfo =
       new Element(Constants.PART_INFO, Constants.NS, Constants.PREFIX);
    if ( partID != null )
    {
      String pId = partID;
      if ( partID.startsWith("<") && partID.endsWith(">") )
           pId = partID.substring(1, partID.length() - 1);
      if ( partID.startsWith("&lt;") && partID.endsWith("&gt;") )
           pId = partID.substring(4, partID.length() - 4);
      if ( pId.startsWith("http://") || pId.startsWith("#") )
           partInfo.addAttribute("href", pId);
      else if (!pId.startsWith("cid") && !pId.startsWith("#"))
      {
        pId = "cid:" + pId;
        partInfo.addAttribute("href", pId);
      }
    }

    if ( schemaLocation != null && !schemaLocation.trim().equals("") )
    {
      Element sch =
         new Element(Constants.SCHEMA, Constants.NS, Constants.PREFIX);
      sch.addAttribute("location", schemaLocation);
      partInfo.addChild(sch);
    }
    if ( description != null )
    {
      Element desc =
        new Element(Constants.DESCRIPTION, Constants.NS, Constants.PREFIX);
      desc.setText(description);
      desc.addAttribute("xml:lang", "en-US");
      partInfo.addChild(desc);
    }

    if ( propertyNames != null && propertyNames.length > 0 &&
         propertyValues != null && propertyValues.length > 0 )
    {
      Element partProperties =
       new Element(Constants.PART_PROPERTIES, Constants.NS, Constants.PREFIX);
      for (int i = 0; i < propertyNames.length; i++)
      {
        Element prop =
          new Element(Constants.PROPERTY, Constants.NS, Constants.PREFIX);
        prop.addAttribute("name", propertyNames[i]);
        if ( i < propertyValues.length && propertyValues[i] != null )
             prop.setText(propertyValues[i]);

        partProperties.addChild(prop);
      }
      partInfo.addChild(partProperties);
    }

    addChild(partInfo);
  }

  public static PayloadInfo createPayloadInfo(MessageContext msgCtx)
  {
    if ( msgCtx == null || msgCtx.getAttachmentMap() == null ) return null;
    String[] cids = msgCtx.getAttachmentMap().getAllContentIDs();
    if ( cids == null || cids.length == 0 ) return null;
    return new PayloadInfo(cids);
  }
}