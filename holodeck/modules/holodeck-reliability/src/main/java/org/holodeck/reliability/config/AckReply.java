package org.holodeck.reliability.config;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="AckReply", strict=false)
public class AckReply implements java.io.Serializable
{
  private static final long serialVersionUID = -8309116253459687779L;

  @Attribute(required=false)
  protected String ackTo;

  @Text
  protected String value;

  public AckReply() {}

  public String getAckTo() { return ackTo; }
  public void setAckTo(String ackTo) { this.ackTo = ackTo; }

  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }
}