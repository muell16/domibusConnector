package org.holodeck.ebms3.consumers;

import org.simpleframework.xml.*;

import java.util.*;
import java.lang.reflect.Constructor;

/**
 * @author Hamid Ben Malek
 */
@Root(name="consumer")
public class ConsumerInfo
{
  @Attribute(name="className")
  protected String className;

  @ElementMap(entry="parameter", key="name", attribute=true, inline=true, required=false)
  protected Map<String, String> parameters;

  public String getClassName() { return className; }
  public void setClassName(String className) { this.className = className; }

  public Map<String, String> getParameters() { return parameters; }
  public void setParameters(Map<String, String> parameters)
  {
    this.parameters = parameters;
  }

  public void addParameter(String name, String value)
  {
    if ( parameters == null ) parameters = new HashMap<String, String>();
    parameters.put(name, value);
  }

  public EbConsumer createInstance()
  {
    if ( className == null || className.trim().equals("") ) return null;

    EbConsumer instance = null;
    try
    {
      int dollarPos = className.indexOf("$");
      if ( dollarPos < 0 )
      {
        //instantiate it as a normal class
        Class instanceClass = Class.forName(className);
        instance = (EbConsumer)instanceClass.newInstance();
      }
      else
      {
        //instantiate it as a normal class
        String containerClassName = className.substring(0, dollarPos);
        Class containerClass = Class.forName(containerClassName);
        Class innerClass = Class.forName(className);
        Object containerInstance = containerClass.newInstance();
        Constructor innerConstructor =
        innerClass.getDeclaredConstructor(new Class[] {containerClass});
        instance =
          (EbConsumer)innerConstructor.newInstance(containerInstance);
      }
    }
    catch (Exception e) { e.printStackTrace(); }
    if ( instance != null ) instance.setParameters(parameters);
    return instance;
  }
}