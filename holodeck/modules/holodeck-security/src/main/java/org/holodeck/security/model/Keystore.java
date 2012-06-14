package org.holodeck.security.model;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="keystore", strict=false)
public class Keystore implements java.io.Serializable
{
  private static final long serialVersionUID = -8309019283738717779L;

  @Attribute
  protected String type;

  @Attribute
  protected String password;
  
  @Text
  protected String value;

  public Keystore() {}

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }
}