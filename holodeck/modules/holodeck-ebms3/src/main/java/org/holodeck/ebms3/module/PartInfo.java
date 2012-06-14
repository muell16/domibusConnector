package org.holodeck.ebms3.module;

/**
 * @author Hamid Ben Malek
 */
public class PartInfo implements java.io.Serializable
{
  private static final long serialVersionUID = 600730050898490680L;  

  protected String href;
  protected String schemaLocation;
  protected String description;
  protected String mimeType;
  protected boolean compressed = false;

  public PartInfo() {}
  public PartInfo(String href, String schemaLocation, String desc)
  {
    this.href = href;
    this.schemaLocation = schemaLocation;
    this.description = desc;
  }

  public String getHref() { return href; }
  public void setHref(String href) { this.href = href; }

  public String getSchemaLocation() { return schemaLocation; }
  public void setSchemaLocation(String schemaLocation)
  {
    this.schemaLocation = schemaLocation;
  }

  public String getDescription() { return description; }
  public void setDescription(String desc) { this.description = desc; }

  public String getMimeType() { return mimeType; }
  public void setMimeType(String mimeType) { this.mimeType = mimeType; }

  public boolean isCompressed() { return compressed; }
  public void setCompressed(boolean compressed)
  {
    this.compressed = compressed;
  }

  // A convenient method:
  public String getCid()
  {
    if ( href == null || href.trim().equals("") ) return null;
    if ( href.startsWith("cid:") ) return href.substring(4);
    else return null;
  }
}