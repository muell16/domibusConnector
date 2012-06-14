package org.holodeck.ebms3.config;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="As4Receipt", strict=false)
public class As4Receipt implements java.io.Serializable
{
  private static final long serialVersionUID = -8309185769038462379L;

  @Attribute(required=false)
  protected String receiptTo;

  @Text
  protected String value;

  public As4Receipt() {}

  public String getReceiptTo() { return receiptTo; }
  public void setReceiptTo(String receiptTo) { this.receiptTo = receiptTo; }

  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }
}