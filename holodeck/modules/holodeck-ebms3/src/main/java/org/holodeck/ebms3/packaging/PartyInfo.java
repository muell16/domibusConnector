package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.module.*;
import org.apache.axiom.om.OMElement;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class PartyInfo extends Element
{
  private static final long serialVersionUID = 2306154359641479706L;  

  protected Element from =
      new Element(Constants.FROM, Constants.NS, Constants.PREFIX);
  protected Element to =
      new Element(Constants.TO, Constants.NS, Constants.PREFIX);

  public PartyInfo()
  {
    super(Constants.PARTY_INFO, Constants.NS, Constants.PREFIX);
    if ( from == null )
         from = new Element(Constants.FROM, Constants.NS, Constants.PREFIX);
    if ( to == null )
         to = new Element(Constants.TO, Constants.NS, Constants.PREFIX);
    addChild(from);
    addChild(to);
  }

  public PartyInfo(String fromPartyID, String toPartyID)
  {
    this();
    addFromParty(fromPartyID, null, null);
    addToParty(toPartyID, null, null);
  }

  public PartyInfo(String[] fromPartyIDs, String[] toPartyIDs)
  {
    this();
    addFromParties(fromPartyIDs, null);
    addToParties(toPartyIDs, null);
  }

  public void addFromParty(String partyId, String partyIdType, String role)
  {
    if ( partyId == null ) return;
    Element p =
      new Element(Constants.PARTY_ID, Constants.NS, Constants.PREFIX);
    p.setText(partyId);
    if ( partyIdType != null && !partyIdType.trim().equals("") )
         p.addAttribute("type", partyIdType);
    from.addChild(p);
    if ( role != null && !role.trim().equals("") )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      from.addChild(r);
    }
  }

  public void addFromParties(String[] partyIds, String role)
  {
    if ( partyIds == null || partyIds.length == 0 ) return;
    for (String partyId : partyIds)
         addFromParty(partyId, null, null);
    if ( role != null && !role.trim().equals("") )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      from.addChild(r);
    }
  }

  public void addFromParties(String[] partyIds, String[] types, String role)
  {
    if ( partyIds == null || partyIds.length == 0 ) return;
    if ( types != null && types.length > 0 )
    {
      for (int i = 0; i < Math.min(partyIds.length, types.length); i++)
           addFromParty(partyIds[i], types[i], null);
      if ( partyIds.length > types.length )
      {
        for (int j = types.length; j < partyIds.length; j++)
            addFromParty(partyIds[j], null, null);
      }
    }
    else
    {
      for (String partyId : partyIds) addFromParty(partyId, null, null);
    }
    if ( role != null && !role.trim().equals("") )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      from.addChild(r);
    }
  }

  // Similar methods for the to element:
  public void addToParty(String partyId, String partyIdType, String role)
  {
    if ( partyId == null ) return;
    Element p =
      new Element(Constants.PARTY_ID, Constants.NS, Constants.PREFIX);
    p.setText(partyId);
    if ( partyIdType != null && !partyIdType.trim().equals("") )
         p.addAttribute("type", partyIdType);
    to.addChild(p);
    if ( role != null && !role.trim().equals("") )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      to.addChild(r);
    }
  }

  public void addToParties(String[] partyIds, String role)
  {
    if ( partyIds == null || partyIds.length == 0 ) return;
    for (String partyId : partyIds)
         addToParty(partyId, null, null);
    if ( role != null && !role.trim().equals("") )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      to.addChild(r);
    }
  }

  public void addToParties(String[] partyIds, String[] types, String role)
  {
    if ( partyIds == null || partyIds.length == 0 ) return;
    if ( types != null && types.length > 0 )
    {
      for (int i = 0; i < Math.min(partyIds.length, types.length); i++)
           addToParty(partyIds[i], types[i], null);
      if ( partyIds.length > types.length )
      {
        for (int j = types.length; j < partyIds.length; j++)
            addToParty(partyIds[j], null, null);
      }
    }
    else
    {
      for (String partyId : partyIds) addToParty(partyId, null, null);
    }
    if ( role != null && !role.trim().equals("") )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      to.addChild(r);
    }
  }

  public String getFromRole()
  {
    //return Util.getGrandChildValue(from, Constants.ROLE);
    return Util.getGrandChildValue(from.getElement(), Constants.ROLE);
  }

  public void setFromRole(String role)
  {
    if ( role == null || role.trim().equals("") ) return;
    Iterator it = from.getChildElements();
    boolean found = false;
    while ( it != null && it.hasNext() )
    {
      OMElement e = (OMElement)it.next();
      if ( e.getLocalName().equals(Constants.ROLE) )
      {
        e.setText(role);
        found = true;
      }
    }
    if ( !found )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      from.addChild(r);
    }
  }

  public String getToRole()
  {
    //return Util.getGrandChildValue(to, Constants.ROLE);
    return Util.getGrandChildValue(to.getElement(), Constants.ROLE);
  }

  public void setToRole(String role)
  {
    if ( role == null || role.trim().equals("") ) return;
    Iterator it = to.getChildElements();
    boolean found = false;
    while ( it != null && it.hasNext() )
    {
      OMElement e = (OMElement)it.next();
      if ( e.getLocalName().equals(Constants.ROLE) )
      {
        e.setText(role);
        found = true;
      }
    }
    if ( !found )
    {
      Element r = new Element(Constants.ROLE, Constants.NS, Constants.PREFIX);
      r.setText(role);
      to.addChild(r);
    }
  }
}