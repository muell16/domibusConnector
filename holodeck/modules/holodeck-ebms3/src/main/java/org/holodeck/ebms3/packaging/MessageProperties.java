package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;

/**
 * @author Hamid Ben Malek
 */
public class MessageProperties extends Element
{
  private static final long serialVersionUID = 6574712004406476903L;  

  public MessageProperties()
  {
    super(Constants.MESSAGE_PROPERTIES, Constants.NS, Constants.PREFIX);
  }

  public MessageProperties(String propertyName, String propertyValue)
  {
    this();
    addProperty(propertyName, propertyValue);
  }

  public MessageProperties(String[] propertyNames, String[] propertyValues)
  {
    this();
    if ( propertyNames == null || propertyNames.length == 0 ) return;
    for (int i = 0; i < propertyNames.length; i++)
    {
      if ( propertyValues != null && propertyValues.length > i )
           addProperty(propertyNames[i], propertyValues[i]);
    }
  }

  public void addProperty(String name, String value)
  {
    if ( name == null ) return;
    Element child = addElement(Constants.PROPERTY, Constants.PREFIX);
    child.setText(value);
    child.addAttribute("name", name);
  }
}