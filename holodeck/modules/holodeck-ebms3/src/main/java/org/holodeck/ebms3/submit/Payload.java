package org.holodeck.ebms3.submit;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Payload")
public class Payload implements java.io.Serializable
{
  @Attribute(required=false)
  protected String cid;

  @Attribute(required=false)
  protected boolean compressed = false;

  @Attribute(required=false)
  protected String description;

  @Attribute(required=false)
  protected String schemaLocation;

  @Text
  protected String payload;

  public String getCid() { return cid; }
  public void setCid(String cid) { this.cid = cid; }

  public boolean isCompressed() { return compressed; }
  public void setCompressed(boolean compressed)
  {
    this.compressed = compressed;
  }

  public String getDescription() { return description; }
  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getSchemaLocation() { return schemaLocation; }
  public void setSchemaLocation(String schemaLocation)
  {
    this.schemaLocation = schemaLocation;
  }

  public String getPayload() { return payload; }
  public void setPayload(String payload) { this.payload = payload; }
}