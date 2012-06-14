package org.holodeck.ebms3.config;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.Serializer;

import java.util.*;
import java.io.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="PModes")
public class PModePool implements java.io.Serializable
{
  private static final long serialVersionUID = -5593318201928374737L;

  @ElementList(entry="Producer", inline=true, required=false)
  protected List<Producer> producers = new ArrayList<Producer>();

  @ElementList(entry="UserService", inline=true, required=false)
  protected List<UserService> userServices = new ArrayList<UserService>();

  @ElementList(entry="Binding", inline=true, required=false)
  protected List<Binding> bindings = new ArrayList<Binding>();

  @ElementList(entry="PMode", inline=true, required=false)
  protected List<PMode> pmodes = new ArrayList<PMode>();

  public PModePool() {}

  public List<Producer> getProducers() { return producers; }
  public void setProducers(List<Producer> producers)
  {
    this.producers = producers;
  }

  public List<UserService> getUserServices() { return userServices; }
  public void setUserServices(List<UserService> userServices)
  {
    this.userServices = userServices;
  }

  public List<Binding> getBindings() { return bindings; }
  public void setBindings(List<Binding> bindings) { this.bindings = bindings; }

  public List<PMode> getPmodes() { return pmodes; }
  public void setPmodes(List<PMode> pmodes) { this.pmodes = pmodes; }

  /* Utility method */
  public Binding getBinding(String bindingName)
  {
    if ( bindings == null || bindings.size() == 0 ) return null;
    for ( Binding b : bindings)
    {
      if ( b.getName().equalsIgnoreCase(bindingName) ) return b;
    }
    return null;
  }
  public static PModePool load(String pmodesFileName)
  {
    if ( pmodesFileName == null || pmodesFileName.trim().equals("") )
         return null;
    return load( new File(pmodesFileName) );
  }
  public static PModePool load(File source)
  {
    if ( source == null || !source.exists() ) return null;
    PModePool pool = null;
    try
    {
      Serializer serializer = new Persister();
      pool = serializer.read(PModePool.class, source);
      if ( pool != null ) pool.init();
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return pool;
  }
  protected void init()
  {
    if ( pmodes != null && pmodes.size() > 0 )
    {
      for (PMode pm : pmodes) pm.setPool(this);
    }
  }

  public void reload(File source)
  {
    if ( source == null || !source.exists() ) return;
    PModePool pool;
    try
    {
      Serializer serializer = new Persister();
      pool = serializer.read(PModePool.class, source);
      if ( pool != null )
      {
        pool.init();
        setPmodes(pool.getPmodes());
      }
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
}