package org.holodeck.ebms3.config;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="ToPartyInfo", strict=false)
public class ToParty implements java.io.Serializable
{
  private static final long serialVersionUID = -5593316571068880737L;

  @Attribute(required=false)
  protected String name;

  @ElementList(inline=true)
  protected List<Party> parties = new ArrayList<Party>();

  @Element(name="Role", required=false)
  protected String role;

  public ToParty() {}
  public ToParty(String name, List<Party> parties, String role)
  {
    this.name = name;
    this.parties = parties;
    this.role = role;
  }

  public void addParty(String type, String partyId)
  {
    Party p = new Party(type, partyId);
    parties.add(p);
  }
  public void addParty(Party party) { parties.add(party); }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public List<Party> getParties() { return parties; }
  public void setParties(List<Party> parties) { this.parties = parties; }

  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }

  public Party[] getPartiesArray()
  {
    if ( parties == null ) return null;
    Party[] res = new Party[parties.size()];
    int i = 0;
    for (Party p : parties)
    {
      res[i] = p;
      i++;
    }
    return res;
  }
  public void setPartiesArray(Party[] list)
  {
    if ( list == null || list.length == 0 )
    {
      if ( parties != null && parties.size() > 0 ) parties.clear();
      return;
    }
    if ( parties == null ) parties = new ArrayList<Party>();
    if ( parties.size() > 0 ) parties.clear();
    for (Party p : list) addParty(p);
  }
}