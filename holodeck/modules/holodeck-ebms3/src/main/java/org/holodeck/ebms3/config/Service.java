package org.holodeck.ebms3.config;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Service", strict=false)
public class Service implements java.io.Serializable
{
  private static final long serialVersionUID = -8309197955456717779L;

  @Text
  protected String value;

  @Attribute(required=false)
  protected String type;

  public Service() {}

  public Service(String type, String value)
  {
    this.type = type;
    this.value = value;
  }

  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public boolean equals(Object obj)
  {
    if ( obj == null || !(obj instanceof Service) )
         return false;
    Service s = (Service)obj;
    if ( (type == null || type.trim().equals("")) )
    {
      return !(s.getType() != null && !s.getType().trim().equals("")) &&
                value.equalsIgnoreCase(s.getValue());
    }
    else
    {
      return value.equalsIgnoreCase(s.getValue()) &&
              type.equalsIgnoreCase(s.getType());
    }
  }
}