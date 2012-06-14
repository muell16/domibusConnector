package org.holodeck.ebms3.submit;

import org.simpleframework.xml.*;
import java.util.*;

import org.holodeck.ebms3.module.Property;

/**
 * @author Hamid Ben Malek
 */
@Root
public class Properties implements java.io.Serializable
{
  @ElementMap(entry="Property", key="name", attribute=true, inline=true, required=false)
  protected Map<String, String> properties =
                                   new HashMap<String, String>();

  public Map<String, String> getProperties() { return properties; }
  public void setProperties(Map<String, String> properties)
  {
    this.properties = properties;
  }

  public void addProperty(String name, String value)
  {
    properties.put(name, value);
  }

  public String getProperty(String propertyName)
  {
    return properties.get(propertyName);
  }

  // Only for convertion from Flex 2:
  public void setPropertyArray(Property[] p)
  {
    if ( p == null || p.length == 0 ) return;
    for (Property prop : p)
         addProperty(prop.getName(), prop.getValue());

    // debugging only:
    //printProperties();
  }
  public Property[] getPropertyArray()
  {
    if ( properties == null || properties.keySet() == null )
         return null;
    int size = properties.keySet().size();
    Property[] res = new Property[size];
    int i = 0;
    for (String key : properties.keySet() )
    {
      res[i] = new Property(key, properties.get(key));
      i++;
    }
    return res;
  }
  // Just for debugging:
  private void printProperties()
  {
    if ( properties == null || properties.keySet() == null ) return;
    for (String key : properties.keySet() )
    {
      System.out.println("[==== property " + key + 
                         " has value " + properties.get(key) );
    }
  }
}