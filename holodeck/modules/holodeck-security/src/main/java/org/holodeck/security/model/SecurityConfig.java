package org.holodeck.security.model;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * @author Hamid Ben Malek
 */
@Root(name="config", strict=false)
public class SecurityConfig implements java.io.Serializable
{
  private static final long serialVersionUID = -8309026758490342779L;

  @ElementList(entry="security", inline=true)
  protected List<Security> securities = new ArrayList<Security>();

  public SecurityConfig() {}

  public List<Security> getSecurities() { return securities; }
  public void setSecurities(List<Security> securities)
  {
    this.securities = securities;
  }

  public void addSecurity(Security sec)
  {
    if ( sec != null ) securities.add(sec);
  }

  public Security getSecurity(String name)
  {
    if ( name == null || name.trim().equals("") ) return null;
    for ( Security sec : securities )
    {
      if ( sec.getName().equalsIgnoreCase(name) ) return sec;
    }
    return null;
  }

  public static SecurityConfig load(File source)
  {
    if ( source == null || !source.exists() ) return null;
    SecurityConfig config = null;
    try
    {
      Serializer serializer = new Persister();
      config = serializer.read(SecurityConfig.class, source);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return config;
  }
}