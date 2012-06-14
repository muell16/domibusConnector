package org.holodeck.ebms3.config;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="MessageProperties", strict=false)
public class MessageProperties implements java.io.Serializable
{
  private static final long serialVersionUID = -5593316571432120737L;

  @ElementList(inline=true)
  protected List<Property> properties = new ArrayList<Property>();

  public MessageProperties() {}
  public MessageProperties(List<Property> properties)
  {
    this.properties = properties;
  }

  public void addProperty(String name, String type, String desc,
                          boolean required)
  {
    Property p = new Property(name, type, desc, required);
    properties.add(p);
  }
  public void addProperty(Property p) { properties.add(p); }
    
  public List<Property> getProperties() { return properties; }
  public void setProperties(List<Property> properties)
  {
    this.properties = properties;
  }

  /* For serialization to Flex UI */
  public Property[] getPropertiesArray()
  {
    if ( properties == null ) return null;
    Property[] res = new Property[properties.size()];
    int i = 0;
    for (Property p : properties)
    {
      res[i] = p;
      i++;
    }
    return res;
  }
  public void setPropertiesArray(Property[] list)
  {
    if ( list == null || list.length == 0 )
    {
      if ( properties != null && properties.size() > 0 ) properties.clear();
      return;
    }
    if ( properties == null ) properties = new ArrayList<Property>();
    if ( properties.size() > 0 ) properties.clear();
    for (Property p : list) addProperty(p);
  }
}