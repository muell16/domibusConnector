package org.holodeck.ebms3.config;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="PartyId", strict=false)
public class Party implements java.io.Serializable
{
  private static final long serialVersionUID = -8309197955520417779L;

  @Text
  protected String partyId;

  @Attribute(required=false)
  protected String type;

  public Party() {}

  public Party(String type, String partyId)
  {
    this.type = type;
    this.partyId = partyId;
  }

  public String getPartyId() { return partyId; }
  public void setPartyId(String partyId) { this.partyId = partyId; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public boolean equals(Object obj)
  {
    if ( obj == null || !(obj instanceof Party) ) return false;
    Party p = (Party)obj;
    return partyId.equalsIgnoreCase(p.getPartyId()) &&
              type.equalsIgnoreCase(p.getType());
  }
}