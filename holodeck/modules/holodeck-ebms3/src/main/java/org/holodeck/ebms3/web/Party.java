package org.holodeck.ebms3.web;

/**
 * @author admin
 */
public class Party implements java.io.Serializable
{
  protected String partyId;
  protected String type;

  public String getPartyId() { return partyId; }
  public void setPartyId(String partyId) { this.partyId = partyId; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
}
