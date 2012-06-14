package org.holodeck.ebms3.web;

/**
 * @author Hamid Ben Malek
 */
public class MsgProperty implements java.io.Serializable
{
  protected String name;
  protected String value;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }

}
