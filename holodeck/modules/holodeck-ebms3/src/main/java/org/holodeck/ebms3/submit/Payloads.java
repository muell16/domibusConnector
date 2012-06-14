package org.holodeck.ebms3.submit;

import org.simpleframework.xml.*;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Payloads")
public class Payloads implements java.io.Serializable
{
  private static final long serialVersionUID = -297660644951243029L;  

  @Element(name="BodyPayload", required=false)
  protected String bodyPayload;

  @ElementList(required=false, inline=true)
  protected List<Payload> payloads = new ArrayList<Payload>();

  public String getBodyPayload() { return bodyPayload; }
  public void setBodyPayload(String bodyPayload)
  {
    this.bodyPayload = bodyPayload;
  }

  public List<Payload> getPayloads() { return payloads; }
  public void setPayloads(List<Payload> payloads)
  {
    this.payloads = payloads;
  }

  public void addPayload(String cid, String payloadURI)
  {
    Payload p = new Payload();
    p.setCid(cid);
    p.setPayload(payloadURI);
    payloads.add(p);
  }

  public String getPayload(String cid)
  {
    if ( payloads == null || payloads.size() <= 0 ) return null;
    for (Payload p : payloads)
    {
      if ( p.getCid().equalsIgnoreCase(cid) ) return p.getPayload();
    }
    return null;
  }

  public String getCID(String payload)
  {
    if ( payloads == null || payloads.size() <= 0 ) return null;
    for (Payload p : payloads)
    {
      if ( p.getPayload().equalsIgnoreCase(payload) )
           return p.getCid();
    }
    return null;
  }

  public void setCID(String cid, String payloadFie)
  {
    if ( cid == null || cid.trim().equals("") || payloadFie == null ) return;
    if ( payloads == null ) payloads = new ArrayList<Payload>();
    for (Payload p : payloads)
    {
      if ( p.getPayload().equalsIgnoreCase(payloadFie) )
      {
        p.setCid(cid);
        return;
      }
    }
    Payload p = new Payload();
    p.setCid(cid);
    p.setPayload(payloadFie);
    payloads.add(p);
  }

  public boolean isCompressed(String fileName)
  {
    if ( fileName == null || fileName.trim().equals("") ) return false;
    if ( payloads == null || payloads.size() == 0 ) return false;
    for (Payload p : payloads)
    {
      if ( p.getPayload().equals(fileName) && p.isCompressed() )
           return true;
    }
    return false;
  }

  public String getDescription(String payloadFile)
  {
    for (Payload p : payloads)
    {
      if ( p.getPayload().equals(payloadFile) ) return p.getDescription();
    }
    return null;
  }

  public String getSchemaLocation(String payloadFile)
  {
    for (Payload p : payloads)
    {
      if ( p.getPayload().equals(payloadFile) ) return p.getSchemaLocation();
    }
    return null;
  }

  // needed only for conversion from Flex 2:
  public void setPayloadArray(Payload[] loads)
  {
    if ( loads == null || loads.length == 0 ) return;
    for (Payload p : loads) payloads.add(p);
  }
  public Payload[] getPayloadArray()
  {
    if ( payloads == null ) return null;
    Payload[] res = new Payload[payloads.size()];
    int i = 0;
    for (Payload p : payloads)
    {
      res[i] = p;
      i++;
    }
    return res;
  }
}