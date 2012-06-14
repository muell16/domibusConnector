package org.holodeck.ebms3.module;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Property")
public class Property implements java.io.Serializable
{
  private static final long serialVersionUID = -154977406873235026L;  

  @Attribute
  protected String name;
  @Text
  protected String value;

  public Property() {}
  public Property(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getValue() { return value; }
  public void setValue(String v) { this.value = v; }
}