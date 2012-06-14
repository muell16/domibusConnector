package org.holodeck.ebms3.config;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="MEP", strict=false)
public class MEP implements java.io.Serializable
{
  private static final long serialVersionUID = -5593310192832220737L;

  public static final String ONE_WAY_PUSH = "One-Way/Push";
  public static final String ONE_WAY_PULL = "One-Way/Pull";
  public static final String TWO_WAY_SYNC = "Two-Way/Sync";
  public static final String TWO_WAY_PUSH_AND_PUSH = "Two-Way/Push-and-Push";
  public static final String TWO_WAY_PUSH_AND_PULL = "Two-Way/Push-and-Pull";
  public static final String TWO_WAY_PULL_AND_PUSH = "Two-Way/Pull-and-Push";
  public static final String TWO_WAY_PULL_AND_PULL = "Two-Way/Pull-and-Pull";

  @Attribute(required=false)
  protected String name;

  @ElementList(inline=true)
  protected List<Leg> legs = new ArrayList<Leg>();

  public MEP() {}
  public MEP(String name, List<Leg> legs)
  {
    this.name = name;
    this.legs = legs;
  }

  public void addLeg(int number, String us, String mpc, String producer,
                     Endpoint endpoint)
  {
    Leg p = new Leg(number, us, mpc, producer, endpoint);
    legs.add(p);
  }
  public void addLeg(Leg leg) { legs.add(leg); }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public List<Leg> getLegs() { return legs; }
  public void setLegs(List<Leg> legs) { this.legs = legs; }

  public void setPmode(PMode pmode)
  {
    if ( pmode == null ) return;
    if ( legs != null && legs.size() > 0 )
    {
      for (Leg leg : legs) leg.setPmode(pmode);
    }
  }

  /* To serialize objects to Flex UI */
  public Leg[] getLegsArray()
  {
    if ( legs == null ) return null;
    Leg[] res = new Leg[legs.size()];
    int i = 0;
    for (Leg leg : legs)
    {
      res[i] = leg;
      i++;
    }
    return res;
  }
  public void setLegsArray(Leg[] list)
  {
    if ( list == null || list.length == 0 )
    {
      if ( legs != null && legs.size() > 0 ) legs.clear();
      return;
    }
    if ( legs == null ) legs = new ArrayList<Leg>();
    if ( legs.size() > 0 ) legs.clear();
    for (Leg leg : list) addLeg(leg);
  }
}