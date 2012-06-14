package org.holodeck.ebms3.config;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Message", strict=false)
public class MessagePayload implements java.io.Serializable
{
  private static final long serialVersionUID = -1827366571062220737L;

  @Attribute(required=false)
  protected String label;

  @Attribute(required=false)
  protected String maxSize;

  @Element(name="SoapBodySchema", required=false)
  protected String soapBodySchema;

  @ElementList(inline=true, required=false)
  protected List<Part> parts = new ArrayList<Part>();

  public MessagePayload() {}
  public MessagePayload(String label, String maxSize, List<Part> parts)
  {
    this.label = label;
    this.parts = parts;
    this.maxSize = maxSize;
  }

  public void addPart(String cid, String mimeType,
                      String schemaLocation, String desc)
  {
    Part p = new Part(cid, mimeType, schemaLocation, desc);
    parts.add(p);
  }
  public void addPart(Part part) { parts.add(part); }

  public String getLabel() { return label; }
  public void setLabel(String label) { this.label = label; }

  public String getMaxSize() { return maxSize; }
  public void setMaxSize(String maxSize) { this.maxSize = maxSize; }

  public String getSoapBodySchema() { return soapBodySchema; }
  public void setSoapBodySchema(String soapBodySchema)
  {
    this.soapBodySchema = soapBodySchema;
  }

  public List<Part> getParts() { return parts; }
  public void setParts(List<Part> parts) { this.parts = parts; }

  /* Needed to serialize objects to Flex UI */
  public Part[] getPartsArray()
  {
    if ( parts == null ) return null;
    Part[] res = new Part[parts.size()];
    int i = 0;
    for (Part p : parts)
    {
      res[i] = p;
      i++;
    }
    return res;
  }
  public void setPartsArray(Part[] list)
  {
    if ( list == null || list.length == 0 )
    {
      if ( parts != null && parts.size() > 0 ) parts.clear();
      return;
    }
    if ( parts == null ) parts = new ArrayList<Part>();
    if ( parts.size() > 0 ) parts.clear();
    for (Part p : list) addPart(p);
  }
}