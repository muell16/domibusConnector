package org.holodeck.ebms3.config;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Endpoint", strict=false)
public class Endpoint implements java.io.Serializable
{
  private static final long serialVersionUID = -8309197123450417779L;

  @Attribute(required=false)
  protected String address;

  @Attribute(required=false)
  protected String soapVersion = "1.2";

  public Endpoint() {}

  public Endpoint(String address, String soapVersion)
  {
    this.address = address;
    this.soapVersion = soapVersion;
  }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public String getSoapVersion() { return soapVersion; }
  public void setSoapVersion(String version) { this.soapVersion = version; }
}