package org.holodeck.security.model;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="user", strict=false)
public class User implements java.io.Serializable
{
  private static final long serialVersionUID = -8309197102938717779L;

  @Attribute(required=false)
  protected String callbackClass;

  @Text
  protected String value;

  public User() {}

  public String getCallbackClass() { return callbackClass; }
  public void setCallbackClass(String callbackClass)
  {
    this.callbackClass = callbackClass;
  }

  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }
}