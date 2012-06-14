package org.holodeck.ebms3.config;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="PMode")
public class PMode implements java.io.Serializable
{
  private static final long serialVersionUID = -5593318273642220737L;

  @Attribute
  protected String name;

  @Attribute(name="binding", required=false)
  protected String bindingName;

  @ElementList(entry="Producer", inline=true, required=false)
  protected List<Producer> producers = new ArrayList<Producer>();

  @ElementList(entry="UserService", inline=true, required=false)
  protected List<UserService> userServices = new ArrayList<UserService>();

  @ElementList(entry="Binding", inline=true, required=false)
  protected List<Binding> bindings = new ArrayList<Binding>();

  /* This is not to be serialized in XML */
  protected Binding binding;
  protected PModePool pool;
  protected boolean initialized = false;

    public PMode() {}
  public PMode(String name, String bindingName)
  {
    this.name = name;
    this.bindingName = bindingName;
  }
  public PMode(String name, Binding binding)
  {
    this.name = name;
    if ( binding == null ) return;
    if ( bindings == null ) bindings = new ArrayList<Binding>();
    bindings.add(binding);
    bindingName = binding.getName();
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getBindingName()
  {
    if ( bindingName != null && !bindingName.trim().equals("") )
         return bindingName;
    if ( bindings == null || bindings.size() == 0 ) return null;
    bindingName = bindings.get(0).getName();
    return bindingName;
  }
  public void setBindingName(String bindingName)
  {
    this.bindingName = bindingName;
  }

  public List<Producer> getProducers() { return producers; }
  public void setProducers(List<Producer> producers) { this.producers = producers; }

  public List<UserService> getUserServices() { return userServices; }
  public void setUserServices(List<UserService> userServices)
  {
    this.userServices = userServices;
  }

  public List<Binding> getBindings() { return bindings; }
  public void setBindings(List<Binding> bindings) { this.bindings = bindings; }

  public Binding getBinding()
  {
    if ( binding != null ) return binding;
    if ( bindings != null && bindings.size() > 0 )
    {
      if ( getBindingName() == null || getBindingName().trim().equals("") )
      {
        if ( !initialized ) setBinding(bindings.get(0));
        return bindings.get(0);
      }
      else
      {
        for (Binding b : bindings)
        {
          if ( b.getName().equalsIgnoreCase(bindingName) )
          {
            if ( !initialized ) setBinding(b);
            return b;
          }
        }
      }
    }
    if ( pool != null )
    {
      Binding b = pool.getBinding( getBindingName() );
      if ( b != null && !initialized ) setBinding(b);
      return b;
    }
    return null;
  }
  public void setBinding(Binding binding)
  {
    this.binding = binding;
    if ( binding != null )
    {
      bindingName = binding.getName();
      this.binding.setPmode(this);
      initialized = true;
    }
  }

  public PModePool getPool() { return pool; }
  public void setPool(PModePool pool) { this.pool = pool; getBinding(); }

  /* Utility method */
  public Producer getProducer(String producerName)
  {
    if ( producers != null && producers.size() > 0 )
    {
      for (Producer p : producers)
      {
        if ( p.getName().equalsIgnoreCase(producerName) ) return p;
      }
    }
    if ( pool != null && pool.getProducers() != null &&
         pool.getProducers().size() > 0 )
    {
      for (Producer p : pool.getProducers())
      {
        if ( p.getName().equalsIgnoreCase(producerName) ) return p;
      }
    }
    return null;
  }
  public UserService getUserService(String userServiceName)
  {
    if ( userServices != null && userServices.size() > 0 )
    {
      for (UserService us : userServices)
      {
        if ( us.getName().equalsIgnoreCase(userServiceName) ) return us;
      }
    }
    if ( pool != null && pool.getUserServices() != null &&
         pool.getUserServices().size() > 0 )
    {
      for (UserService us : pool.getUserServices())
      {
        if ( us.getName().equalsIgnoreCase(userServiceName) ) return us;
      }
    }
    return null;
  }

  public Leg getLeg(String mep, String mpc, String address)
  {
    if ( getBinding() == null ) return null;
    MEP m = getBinding().getMep();
    if ( m == null ) return null;
    List<Leg> legs = m.getLegs();
    if ( legs == null || legs.size() == 0 ) return null;
    for ( Leg leg : legs )
    {
      if ( leg.getEndpoint() == null )
      {
        if ( same(leg.getMpc(), mpc) &&
             same(mep, m.getName()) &&
             (address == null || address.trim().equals("")) ) return leg;
      }
      else if ( same(leg.getMpc(), mpc) && same(mep, m.getName()) &&
                same(address, leg.getEndpoint().getAddress()) ) return leg;
    }
    return null;
  }
  public Leg getLeg(String mep, String mpc)
  {
    if ( getBinding() == null ) return null;
    MEP m = getBinding().getMep();
    if ( m == null ) return null;
    List<Leg> legs = m.getLegs();
    if ( legs == null || legs.size() == 0 ) return null;
    for ( Leg leg : legs )
    {
      if ( same(leg.getMpc(), mpc) && same(mep, m.getName()) ) return leg;
    }
    return null;
  }
  public Leg getLeg(int legNumber, String mep, String mpc)
  {
    if ( getBinding() == null ) return null;
    MEP m = getBinding().getMep();
    if ( m == null ) return null;
    List<Leg> legs = m.getLegs();
    if ( legs == null || legs.size() == 0 ) return null;
    for ( Leg leg : legs )
    {
      if ( leg.getNumber() == legNumber && same(leg.getMpc(), mpc) && 
           same(mep, m.getName()) ) return leg;
    }
    return null;
  }
  public String getMep()
  {
    if ( getBinding() == null ) return null;
    MEP mep = getBinding().getMep();
    if ( mep == null ) return null;
    return mep.getName();
  }

  private boolean same(String v1, String v2)
  {
    if ( v1 != null ) return v1.equals(v2);
    return v2 == null || v2.equals(v1);
  }

  /* To serialize objects to Flex UI, add below array getter/setter methods */
}